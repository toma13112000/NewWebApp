const express = require('express');
const bodyParser = require('body-parser');
const bcrypt = require('bcrypt');
const mysql = require('mysql2');
const multer = require('multer');
const path = require('path');

const app = express();
const port = 3000;

// Создание соединения с MySQL
const connection = mysql.createConnection({
    host: 'localhost',
    user: 'rootroot',
    password: 'rootroot',
    database: 'jumystap'
});

// Подключение к MySQL
connection.connect((err) => {
    if (err) {
        console.error('Ошибка подключения к базе данных:', err.stack);
        return;
    }
    console.log('Подключено к базе данных');
});

// Middleware для парсинга JSON-запросов
app.use(bodyParser.json());

// Настройка `multer` для хранения загруженных файлов
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'uploads/');
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + path.extname(file.originalname));
    }
});

const upload = multer({ storage });

// Маршрут для обработки POST-запроса на /submit-form
app.post('/submit-form', upload.single('photo'), (req, res) => {
    const { name, phone, email, password, type } = req.body;
    const photoPath = req.file ? req.file.path : null;

    if (!name || !phone || !email || !password) {
        return res.status(400).send('Все поля обязательны для заполнения');
    }

    const checkUserQuery = 'SELECT * FROM users WHERE phone_number = ?';
    connection.query(checkUserQuery, [phone], (err, results) => {
        if (err) {
            console.error('Ошибка при проверке пользователя:', err);
            return res.status(500).send('Ошибка при проверке пользователя');
        }

        if (results.length > 0) {
            return res.status(400).send('Пользователь с этим номером телефона уже существует');
        }

        bcrypt.hash(password, 10, (err, hash) => {
            if (err) {
                console.error('Ошибка хэширования пароля:', err);
                return res.status(500).send('Ошибка хэширования пароля');
            }

            const user = {
                name,
                phone_number: phone,
                email,
                password: hash,
                user_type: type,
                photo_path: photoPath
            };

            const insertUserQuery = 'INSERT INTO users SET ?';
            connection.query(insertUserQuery, user, (err, result) => {
                if (err) {
                    console.error('Ошибка при сохранении пользователя:', err);
                    return res.status(500).send('Ошибка при сохранении пользователя');
                }
                console.log('Пользователь зарегистрирован:', result);
                res.status(201).send('Пользователь зарегистрирован');
            });
        });
    });
});

// Запуск сервера
app.listen(port, () => {
    console.log(`Сервер запущен на порту ${port}`);
});
