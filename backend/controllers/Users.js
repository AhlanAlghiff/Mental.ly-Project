import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";
import Users from "../models/UserModel.js";

export const getUsers = async (req, res) => {
    try {
      const user = await Users.findOne({
        where: { id: req.userId }, // Gunakan userId dari token
        attributes: ['id', 'name', 'email'], // Hanya ambil atribut tertentu
      });
  
      if (!user) return res.status(404).json({ msg: "User not found" });
      res.json(user);
    } catch (error) {
      console.log(error);
      res.status(500).json({ msg: "Internal server error" });
    }
  };

export const Register = async(req,res)=> {
    const { name, email, password, confPassword } = req.body;
    if(password !== confPassword) return res.status(400).json({msg: "Password dan Confirm Password tidak cocok"});

    const salt = await bcrypt.genSalt();
    const hashPassword = await bcrypt.hash(password, salt);

    try {
        await Users.create({
            name: name,
            email: email,
            password: hashPassword
        });
        res.json({msg: "Register Berhasil"});
    } catch (error) {
        console.log(error);
    }
}

export const Login = async(req,res) => {
    try {
        const user = await Users.findAll({
            where:{
                email: req.body.email
            }
        });
        const match = await bcrypt.compare(req.body.password, user[0].password);
        if(!match) return res.status(400).json({msg:"Wrong Password"});
        const userId = user[0].id;
        const name = user[0].name;
        const email = user[0].email;
        const accessToken = jwt.sign({userId, name, email}, process.env.ACCESS_TOKEN_SECRET,{
            expiresIn: '1m'
        });
        const refreshToken = jwt.sign({userId, name, email}, process.env.REFRESH_TOKEN_SECRET,{
            expiresIn: '1d'
        });
        await Users.update({refresh_token:refreshToken},{
            where:{
                id: userId
            }
        });
        res.cookie('refreshToken', refreshToken, {
            httOnly: true,
            maxAge: 24 * 60 * 60 * 1000,
            secure: true
        });
        res.json({ accessToken });
    } catch (error) {
        res.status(404).json({msg:"email tidak ditemukan"});
    }
}

// export const Login = async (req, res) => {
//     try {
//         const user = await Users.findOne({
//             where: { email: req.body.email }
//         });

//         if (!user) return res.status(404).json({ msg: "Email tidak ditemukan" });

//         const match = await bcrypt.compare(req.body.password, user.password);
//         if (!match) return res.status(400).json({ msg: "Password salah" });

//         const userId = user.id;
//         const name = user.name;
//         const email = user.email;

//         const accessToken = jwt.sign(
//             { userId, name, email },
//             process.env.ACCESS_TOKEN_SECRET,
//             { expiresIn: '15s' }
//         );
//         const refreshToken = jwt.sign(
//             { userId, name, email },
//             process.env.REFRESH_TOKEN_SECRET,
//             { expiresIn: '1d' }
//         );

//         await Users.update({ refresh_token: refreshToken }, { where: { id: userId } });

//         res.cookie('refreshToken', refreshToken, {
//             httpOnly: true,
//             secure: true,
//             maxAge: 24 * 60 * 60 * 1000,
//         });

//         // Mengirimkan respons dengan ID, nama, email, dan token
//         res.json({
//             id: userId,
//             name: name,
//             email: email,
//             token: accessToken
//         });
//     } catch (error) {
//         console.error(error);
//         res.status(500).json({ msg: "Internal server error" });
//     }
// };



export const Logout = async(req, res) => {
    const refreshToken = req.cookies.refreshToken;
        if(!refreshToken) return res.sendStatus(204);
        const user = await Users.findAll({
            where: {
                refresh_token : refreshToken
            }
        });
        if(!user[0]) return res.sendStatus(204);
        const userId = user[0].id;
        await Users.update({refresh_token: null}, {
            where:{
                id: userId
            }
        });
        res.clearCookie('refreshToken');
        return res.sendStatus(200);
}
