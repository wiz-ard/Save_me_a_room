var mysql = require("mysql");

var connection = mysql.createConnection({
    host: 'mysql-dvpc.cn0riqiyz9gv.us-east-2.rds.amazonaws.com',
    user: 'admin',
    password: 'daspassword',
    database: 'Capstone'
});

module.exports = connection;