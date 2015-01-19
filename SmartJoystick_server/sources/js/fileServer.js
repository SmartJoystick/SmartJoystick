var express = require('express');
var path    = require('path');
var app     = express();

var dirname = path.resolve();
var gamesFolder = path.join(dirname, 'games');

console.log(gamesFolder);

app.use(express.static(gamesFolder));

app.listen(5001, function(){
    console.log('file server listen port 5001');
});