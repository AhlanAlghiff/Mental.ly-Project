import dotenv from "dotenv";
import cookieParser from "cookie-parser";
import cors from "cors";
import express from "express";
import db from "./config/Database.js";
import router from './routes/index.js';
dotenv.config();
const app = express();

try {
    await db.authenticate();
    console.log('Database Connected..');

} catch (error) {
    console.error(error);
    process.exit(1);
}

app.use(cors({ credentials:true, origin:'http://localhost:3000'}));
app.use(express.urlencoded({ extended: true }));
app.use(cookieParser());
app.use(express.json());
app.use(router);

app.listen(5000, ()=> console.log('Server running at port 5000'));
