import { Sequelize } from "sequelize";

// const db = new Sequelize('postgres','postgres','postgresql',{
//     host: "localhost",
//     dialect: "postgres",
//     define: {
//         timestamps: false,
//     },
// });

// Koneksi ke PostgreSQL di GCP
const db = new Sequelize(
    'db_predict',  // Nama database yang sudah dibuat di GCP
    'postgres',      // Username PostgreSQL
    'mentaly',  // Password untuk user PostgreSQL
    {
      host: '34.50.66.92',  // Ganti dengan Public IP Address dari instance GCP
      port: 5432,                 // Port default PostgreSQL (bisa diubah jika perlu)
      dialect: 'postgres',
      define: {
        timestamps: false,       // Matikan timestamps jika tidak diperlukan
      },
      logging: false,            // Matikan logging SQL (opsional)
    }
  );

export default db;