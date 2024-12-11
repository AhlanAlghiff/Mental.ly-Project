import express from "express";
import { GetUserByID, Register, Login, Logout } from "../controllers/Users.js";
import { verifyToken } from "../middleware/VerifyToken.js";
import { refreshToken } from "../controllers/RefreshToken.js";
import { ModelDiagnosis } from "../controllers/ModelRunner.js";

const router = express.Router();

// Auth
router.post('/v1/auth/register', Register);
router.get('/v1/auth/token', refreshToken);
router.post('/v1/auth/login', Login);
router.post('/v1/auth/logout', Logout);

// User
router.get('/v1/users/:id', verifyToken, GetUserByID);

// Diagnosis
router.post('/v1/diagnosis',  ModelDiagnosis);

export default router;