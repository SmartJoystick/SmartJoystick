var express = require('express');
var path    = require('path');
var app     = express();

var dirname = path.resolve();

app.use(express.static(dirname));

app.listen(5001, function(){
    console.log('file server listen port 5001');
});