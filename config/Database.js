import { Sequelize } from "sequelize";

// const db = new Sequelize('menta-ly','root','',{
//     host: "localhost",
//     dialect: "mysql"
// });

// export default db;

const db = new Sequelize('menta-ly', 'root', 'root123', { // Ganti '' dengan password
    host: "34.101.145.44",
    dialect: "mysql",
    port: 3306, 
    logging: false 
});

export default db;