import Users from '../models/UserModel.js';
import bcrypt from 'bcrypt';
import jwt from 'jsonwebtoken';
import { v4 as uuidv4 } from 'uuid';

class UsersRepository {
    async GetUserByID(id) {
        try {
            return await Users.findOne({
                where: { id: id },
                attributes: ['id', 'name', 'email'],
            });
        } catch (error) {
            console.error(error);
            return {
                err: error,
                message: 'Internal server error',
                status: 500,
            }
        }
    }

    async GetUserByEmail(email) {
        return await Users.findOne({
            where: { email: email },
            attributes: ['id', 'name', 'email'], 
        });
    }

    async Register(name, email, password)  {
        const salt = await bcrypt.genSalt();
        const hashPassword = await bcrypt.hash(password, salt);
        var id = uuidv4();
        var timeNow = new Date();
        try {
            await Users.create({
                id: id,
                name: name,
                email: email,
                password: hashPassword,
                created_at: timeNow,
            });
            return {
                id: id,
                name: name,
                email: email,
                created_at: timeNow,
            };
        } catch (error) {
            console.error(error);
            return {
                err: error,
                message: 'Internal server error',
                status: 500,
            }
        }
    }

    async Login(_email, password) {
        try {
            const user = await Users.findOne({
                //semula findAll
                where:{
                    email: _email
                }
            });
            //before
            // const match = await bcrypt.compare(password, user[0].password);
            //after
            const match = await bcrypt.compare(password, user.password);
            if(!match) return false;

            // if (user[0].email === null) {
            //     return false;
            // }

            // const userId = user[0].id;
            // const name = user[0].name;
            // const email = user[0].email;

            const { id: userId, name, email } = user;
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
            return { accessToken, refreshToken, user: { id: userId, name, email } };
        } catch (error) {
            console.error(error);
            return false;
        }
    }
}

export default new UsersRepository();
