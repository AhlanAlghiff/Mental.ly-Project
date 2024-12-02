import express from "express";
import { Register, Login, Logout } from "../controllers/Users.js";
import { verifyToken } from "../middleware/VerifyToken.js";
import { refreshToken } from "../controllers/RefreshToken.js";

const router = express.Router();

router.post('/register', Register);  // Register tidak memerlukan otorisasi
router.post('/login', Login);        // Login mengembalikan detail user + accessToken
router.get('/token', refreshToken);  // Mendapatkan accessToken baru
router.delete('/logout', Logout);    // Logout user

export default router;
