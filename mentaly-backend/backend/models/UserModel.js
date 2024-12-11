import { Sequelize } from "sequelize";
import db from "../config/Database.js";

const { DataTypes } = Sequelize;

const Users = db.define('users', {
    name:{
        type: DataTypes.STRING
    },
    email:{
        type: DataTypes.STRING
    },
    password:{
        type: DataTypes.STRING
    },
    refresh_token:{
        type: DataTypes.TEXT
    },
    created_at:{
        type: DataTypes.DATE
    },
    updated_at:{
        type: DataTypes.DATE
    }
},{
    freezeTableName:true
});

export default Users;