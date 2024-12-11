import jwt from "jsonwebtoken";
import Users from "../models/UserModel.js";

export const refreshToken = async (req, res) => {
    const refreshToken = req.cookies.refreshToken;
    if (!refreshToken) return res.sendStatus(401);
  
    try {
      const user = jwt.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET); // Pastikan ini menggunakan REFRESH_TOKEN_SECRET
      const accessToken = jwt.sign(
        { userId: user.id, name: user.name, email: user.email },
        process.env.ACCESS_TOKEN_SECRET, // Pastikan ini menggunakan ACCESS_TOKEN_SECRET
        { expiresIn: "15m" }
      );
      res.json({ accessToken });
    } catch (err) {
      console.error(err);
      res.sendStatus(403);
    }
  };

// export const refreshToken = async (req, res) => {
//     const refreshToken = req.cookies.refreshToken;
//     if (!refreshToken) return res.sendStatus(401);

//     try {
//         const user = await Users.findOne({
//             where: { refresh_token: refreshToken },
//             attributes: ['id', 'name', 'email'], // Ambil atribut yang diperlukan
//         });

//         if (!user) return res.sendStatus(403);

//         const accessToken = jwt.sign(
//             { userId: user.id, name: user.name, email: user.email },
//             process.env.ACCESS_TOKEN_SECRET,
//             { expiresIn: '15m' }
//         );

//         res.json({
//             id: user.id,
//             name: user.name,
//             email: user.email,
//             accessToken,
//         });
//     } catch (err) {
//         console.error(err);
//         res.sendStatus(403);
//     }
// };
