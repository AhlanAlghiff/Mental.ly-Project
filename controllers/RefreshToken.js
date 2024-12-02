import jwt from "jsonwebtoken";
import Users from "../models/UserModel.js";

export const refreshToken = async (req, res) => {
  try {
    const refreshToken = req.cookies.refreshToken;
    if (!refreshToken) return res.sendStatus(401); // Tidak ada token
    
    const user = await Users.findOne({
      where: { refresh_token: refreshToken },
    });
    if (!user) return res.sendStatus(403); // Token tidak valid
    
    // Verifikasi Refresh Token
    jwt.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET, (err, decoded) => {
      if (err) return res.sendStatus(403); // Token tidak valid
      
      const userId = user.id;
      const name = user.name;
      const email = user.email;
      
      // Buat Access Token Baru
      const accessToken = jwt.sign(
        { userId, name, email },
        process.env.ACCESS_TOKEN_SECRET,
        { expiresIn: "1m" }
      );
      res.status(200).json({ accessToken });
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Internal server error" });
  }
};