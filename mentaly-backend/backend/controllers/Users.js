import { error } from "console";
import UsersRepository from "../repository/Users.js";
import { constants as httpConstants } from 'http2';

export const GetUserByID = async (req, res) => {
    const user = await UsersRepository.GetUserByID(req.params.id);
    if (!user) return res.status(404).json({ msg: "User not found" });
    res.json(user);
};

export const Register = async(req,res)=> {
    const { name, email, password, confPassword } = req.body; // req.body berisi data dari request

    // Check Request Body Fields
    const missingFields = [];
    if (!name) missingFields.push('name');
    if (!email) missingFields.push('email');
    if (!password) missingFields.push('password');
    if (!confPassword) missingFields.push('confPassword');
    if (missingFields.length > 0) {
        return res.status(httpConstants.HTTP_STATUS_BAD_REQUEST).json({
            error: 'Validation failed',
            message: `Missing fields required: ${missingFields.join(', ')}`,
            missingFields: missingFields,
        });
    }

    // Check Email using Regex
    if (!email.match(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)) {
        return res.status(httpConstants.HTTP_STATUS_BAD_REQUEST).json({
            error: 'Validation failed',
            message: 'Email is invalid',
        });
    }

    // Check Passwords
    if (password !== confPassword) {
        return res.status(httpConstants.HTTP_STATUS_BAD_REQUEST).json({
            error: 'Validation failed',
            message: 'Password and confirm password do not match',
        });
    }

    // Check if user already exists
    const user = await UsersRepository.GetUserByEmail(email);
    if (user) {
        return res.status(httpConstants.HTTP_STATUS_BAD_REQUEST).json({
            error: 'Validation failed',
            message: 'Email already exists',
        });
    }

    try {
        await UsersRepository.Register(name, email, password); // Register the user
        return res.status(200).json({
            message: 'Register successful',
        });
    } catch (error) {
        console.error(error);
        return res.status(500).json({
            message: 'Internal server error',
        });
    }
};
    

//     const result = await UsersRepository.Register(name, email, password); // Mencoba mendaftarkan data ke database
//     if(!result) return res.status(500).json({msg: "Internal server error", error: result}); // Jika gagal mendaftarkan data ke database, return 500 dengan pesan error

//     return res.status(httpConstants.HTTP_STATUS_OK).json({
//         data: result,
//         message: 'Register Success',
//     });

// };


export const Login = async(req,res) => {
    const { email, password } = req.body; // req.body berisi data dari request
    try {
        const { accessToken, refreshToken, user } = await UsersRepository.Login(email, password);

        if (!accessToken || !refreshToken) {
            return res.status(400).json({ msg: "Email atau Password salah" });
        }

        // Add refreshToken to cookie
        res.cookie('refreshToken', refreshToken, {
            httpOnly: true,
            secure: true,
            maxAge: 24 * 60 * 60 * 1000,
        });

        // Return user details and access token in response
        return res.json({
            user: {
                id: user.id,
                name: user.name,
                email: user.email,
            },
            accessToken: accessToken,
        });
    } catch (error) {
        console.error(error);
        return res.status(500).json({ msg: "Internal server error" });
    }
};
//     const { accessToken, refreshToken } = await UsersRepository.Login(email, password); // Mencoba login ke database
//     if(!accessToken || !refreshToken) return res.status(400).json({msg: "Email atau Password salah"}); // Jika email atau password salah, return 400 dengan pesan error
//     res.json({ accessToken, refreshToken }); // Jika berhasil, return 200 dengan pesan success
//     // Menambahkan refreshToken ke cookie
//     res.cookie('refreshToken', refreshToken, {
//         httpOnly: true,
//         secure: true,
//         maxAge: 24 * 60 * 60 * 1000,
//     });
//     // Mengirimkan respons dengan ID, nama, email, dan token
//     // res.json({
//     //     id: userId,
//     //     name: name,
//     //     email: email,
//     //     token: accessToken
//     // });
// };

// export const Login = async(req,res) => {
//     try {
//         const user = await Users.findAll({
//             where:{
//                 email: req.body.email
//             }
//         });
//         const match = await bcrypt.compare(req.body.password, user[0].password);
//         if(!match) return res.status(400).json({msg:"Wrong Password"});
//         const userId = user[0].id;
//         const name = user[0].name;
//         const email = user[0].email;
//         const accessToken = jwt.sign({userId, name, email}, process.env.ACCESS_TOKEN_SECRET,{
//             expiresIn: '1m'
//         });
//         const refreshToken = jwt.sign({userId, name, email}, process.env.REFRESH_TOKEN_SECRET,{
//             expiresIn: '1d'
//         });
//         await Users.update({refresh_token:refreshToken},{
//             where:{
//                 id: userId
//             }
//         });
//         res.cookie('refreshToken', refreshToken, {
//             httOnly: true,
//             maxAge: 24 * 60 * 60 * 1000,
//             secure: true
//         });
//         res.json({ accessToken });
//     } catch (error) {
//         res.status(404).json({msg:"email tidak ditemukan"});
//     }
// }

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
