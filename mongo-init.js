// Script de inicialización de MongoDB
db = db.getSiblingDB('franchise_db');

// Crear usuario para la aplicación
db.createUser({
    user: 'franchise_user',
    pwd: 'franchise_password',
    roles: [
        {
            role: 'readWrite',
            db: 'franchise_db'
        }
    ]
});

// Crear colecciones
db.createCollection('franchises');
db.createCollection('branches');
db.createCollection('products');

print('Base de datos franchise_db inicializada correctamente');

