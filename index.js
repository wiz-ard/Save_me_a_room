// if this breaks swap const to var
const express = require("express");
const app = express();
const connection = require("./db");
const process = require('process');

// inputs are wrapped in double quotes and string values are wrapped in single quotes
app.get("/", function(req, res) {
    let sql = process.argv[2]
    connection.query(sql, "admin", function(err, results) {
        if (err) throw err;
        res.send(results);
    });
});

app.listen(3000, function() {
    console.log("server listening on port 3000");
    connection.connect(function(err) {
        if (err) throw err;
        console.log("connected to the database");
    });
});