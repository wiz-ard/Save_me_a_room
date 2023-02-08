// if this breaks swap const to var
const express = require("express");
const app = express();
const connection = require("./db");
const process = require('process');

// inputs are wrapped in double quotes and string values are wrapped in single quotes
app.get("/", function(req, res) {
    res.send("Hello!")
});

app.get("/search", function(req, res) {
    // req.params.query has %20 instead of spaces
    // need to find all instances of %20 and replace them with spaces
    // then I can set the new req to sql and it should fire
    let sql = req.query.query;

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
