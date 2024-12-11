import { Sequelize } from "sequelize";
import db from "../config/Database.js";

const { DataTypes } = Sequelize;

const Depression = db.define('depression', {
    id : {
        type: DataTypes.UUID,
        primaryKey: true,
        defaultValue: DataTypes.UUIDV4
    },
    user_id:{
        type: DataTypes.STRING,
        primaryKey: true
    },
    age:{
        type: DataTypes.FLOAT
    },
    gender:{
        type: DataTypes.FLOAT
    },
    work_pressure:{
        type: DataTypes.FLOAT
    },
    job_satisfaction:{
        type: DataTypes.FLOAT
    },
    sleep_duration:{
        type: DataTypes.FLOAT
    },
    dietary_habits:{
        type: DataTypes.FLOAT
    },
    have_you_ever_had_suicidal_thoughts:{
        type: DataTypes.BOOLEAN
    },
    work_hours:{
        type: DataTypes.FLOAT
    },
    financial_stress:{
        type: DataTypes.FLOAT
    },
    family_history_of_mental_illness:{
        type: DataTypes.BOOLEAN
    },
    output:{
        type: DataTypes.FLOAT
    },
    created_at:{
        type: DataTypes.DATE,
        defaultValue: DataTypes.NOW
    }
},{
    freezeTableName:true
});

export default Depression;