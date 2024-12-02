import Users from "../models/UserModel.js";
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

// Register
export const Register = async (req, res) => {
  const { name, email, password, confPassword } = req.body;

  // Check Missing Fields
  const missingFields = [];
  if (!name) missingFields.push("name");
  if (!email) missingFields.push("email");
  if (!password) missingFields.push("password");
  if (!confPassword) missingFields.push("confPassword");
  if (missingFields.length > 0) {
    return res.status(400).json({
      error: "Validation failed",
      message: `Missing fields required: ${missingFields.join(", ")}`,
    });
  }

  // Validate Email with Regex
  const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!emailRegex.test(email)) {
    return res.status(400).json({
      error: "Validation failed",
      message: "Email is invalid",
    });
  }

  // Validate Passwords
  if (password !== confPassword) {
    return res.status(400).json({
      error: "Validation failed",
      message: "Password and Confirm Password do not match",
    });
  }

  // Hash Password
  try {
    const salt = await bcrypt.genSalt();
    const hashPassword = await bcrypt.hash(password, salt);

    // Check if Email Already Exists
    const existingUser = await Users.findOne({ where: { email } });
    if (existingUser) {
      return res.status(400).json({
        error: "Validation failed",
        message: "Email is already registered",
      });
    }

    await Users.create({
      name: name,
      email: email,
      password: hashPassword,
    });
    res.json({ message: "Register successful" });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Internal server error" });
  }
};

// Login
export const Login = async (req, res) => {
    try {
      // Cari user berdasarkan email
      const user = await Users.findOne({
        where: {
          email: req.body.email,
        },
      });
  
      // Jika email tidak ditemukan
      if (!user) {
        return res.status(404).json({ error: "Email not found" });
      }
  
      // Verifikasi password
      const match = await bcrypt.compare(req.body.password, user.password);
      if (!match) {
        return res.status(400).json({ error: "Wrong password" });
      }
  
      // Buat accessToken dan refreshToken
      const userId = user.id;
      const name = user.name;
      const email = user.email;
      const accessToken = jwt.sign({ userId, name, email }, process.env.ACCESS_TOKEN_SECRET, {
        expiresIn: "1m",
      });
      const refreshToken = jwt.sign({ userId, name, email }, process.env.REFRESH_TOKEN_SECRET, {
        expiresIn: "1d",
      });
  
      // Simpan refreshToken di database
      await Users.update({ refresh_token: refreshToken }, {
        where: {
          id: userId,
        },
      });
  
      // Kirim refreshToken sebagai cookie
      res.cookie("refreshToken", refreshToken, {
        httpOnly: true,
        maxAge: 24 * 60 * 60 * 1000,
        secure: true, // Ganti dengan false jika tidak menggunakan HTTPS
      });
  
      // Respons berisi detail pengguna dan accessToken
      res.status(200).json({
        user: {
          id: userId,
          name: name,
          email: email,
        },
        accessToken,
      });
    } catch (error) {
      console.error(error);
      res.status(500).json({ error: "Internal server error" });
    }
  };
  

// Logout
export const Logout = async (req, res) => {
  const refreshToken = req.cookies.refreshToken;
  if (!refreshToken) return res.sendStatus(204);
  const user = await Users.findAll({
    where: {
      refresh_token: refreshToken,
    },
  });
  if (!user[0]) return res.sendStatus(204);
  const userId = user[0].id;
  await Users.update({ refresh_token: null }, {
    where: {
      id: userId,
    },
  });
  res.clearCookie("refreshToken");
  return res.sendStatus(200);
};
