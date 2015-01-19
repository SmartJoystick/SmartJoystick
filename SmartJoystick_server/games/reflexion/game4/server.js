
var port = process.argv[2];
var ws = require("nodejs-websocket");

var nbUser = 0;

var server = ws.createServer(function (conn) {
    console.log("New connection");

    if(nbUser < 2){
        console.log('la waiting page apparait');
        conn.sendText("waiting")
    }

    conn.on("text", function (str) {
        console.log("Received "+str)
        conn.sendText(str.toUpperCase()+"!!!")
    })

    conn.on("close", function (code, reason) {
        console.log("Connection closed : " + reason)
    })

    
}).listen(port)