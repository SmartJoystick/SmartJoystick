var net    = require('net');
var os     = require('os');
var uuid   = require('node-uuid');
var StringDecoder = require('string_decoder').StringDecoder;

var networks  = os.networkInterfaces();
var address   = networks.wlan0[0].address;
var port      = 5000;
var qrContent = JSON.stringify({"address" : address,"port" : port });
var decoder   = new StringDecoder('utf8');

var clients   = [];
var gamesList; 

console.log(address);

// create the TCP server
var server = net.createServer(function(socket) {

    // when a new client connect to the server
    // send him back the gameList containing games and categories (JSON format)
    socket.userId = uuid.v4();
    var socketCache = socket;

    clients.push(socket);
    sjCore.addClient(socket);
    sjCore.events.fire('connection', socket);
    sjCore.send(socket.userId, gamesList);

    // when the client send data to server
    socket.on('data', function(data) {
        sjCore.events.fire('data', data, socket);
    });

    // when the client disonnect from the server
    socket.on('end', function () {
        sjCore.removeClient(socketCache);
        clients.splice(clients.indexOf(socketCache), 1);
        sjCore.events.fire('end', socketCache);

    });
});

// get a list containing all the categories and games (JSON format)
// then server listen and new connections can be done

sjGames.getGames(function(err, result){

    if (err){ return;}
    gamesList = JSON.stringify(result);
    server.listen(port, address, function(){
        $('#qrcode').qrcode({text : qrContent, size : 300});
    }); 
        
});

// when socket.on('data') is raised, handle all the icoming data 
// TODO add type (core or api)

sjCore.events.on('data', function(data, socket){
    // data is JSON string

    var decodedData  = decoder.write(data);
    var objFromJson  = JSON.parse(decodedData);

    if(objFromJson.method != 'undefined'){
        var method = objFromJson.method;
        var args   = objFromJson.args;
        // first argument is always playerId
        args.splice(0, 0, socket.userId);
        sjGames[method].apply(this, args);
    }
});

