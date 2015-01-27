var port = process.argv[2];
var ws = require("nodejs-websocket");
var events = require('events');
var sjEvent = new events.EventEmitter();


// sjEvent is for intern API events
// this.events is events for consumer of API

var waitingPlayers = [];
var activePlayers = [];

module.exports = {

    launchServer : function(){

        var _this = this;

        var server = ws.createServer(function(player) {

            _this.events.fire('connection', player);

            console.log('new connection');

            player.on('text', function(msg){
                var parsed = JSON.parse(msg);
                _this.events.fire('message', player, parsed);
            })

            player.on("close", function (code, reason) {

                var wasActive = false;

                if(waitingPlayers[player]){
                    waitingPlayers.splice(waitingPlayers.indexOf(player), 1);
                }
              /*  else if(activePlayers[player]){

                    console.log('we are here');
                    activePlayers.splice(activePlayers.indexOf(player), 1);
                    wasActive = true;
                } */

                console.log('activePlayers : ',activePlayers.length);
                console.log('waitingPlayers : ',waitingPlayers.length);

                _this.events.fire('close', player, wasActive);
            })

        }).listen(port)
    },

    wait : function(player){
        waitingPlayers.push(player);
        var context = {status : 'waiting'};
        this.send(player, context);
        sjEvent.emit('newWait', waitingPlayers.length);
    },

    watch : function(nbPlayers, callback){
        sjEvent.on('newWait', function(nbWaiting){
            if(nbWaiting == nbPlayers){
                setTimeout(function(){
                    callback(waitingPlayers);
                }, 2000);
            }
        })
    },

    startGame : function(players){
        waitingPlayers = [];

     /*   players.forEach(function(player){
            activePlayers.push(player);
        }); */

        this.events.fire('start', players);
    },

    send : function(player, msg){
        msg = JSON.stringify(msg);
        player.sendText(msg);
    },

    broadcast : function(players, msg, without){
        if(without){
            players.forEach(function(player){
                if(player != without){
                    player.sendText(msg);
                }
            })
            return;
        }
        players.forEach(function(player){
            player.sendText(msg);
        })
    },

    events : function() {
        var _events = {};
        return {
            on: function(event, callback, context) {
                _events.hasOwnProperty(event) || (_events[event] = []);
                _events[event].push([callback, context || this]);
            },

            fire: function(event) {
                var callbacks = _events[event] || [];  
                var tail = Array.prototype.slice.call(arguments, 1);

                for(var i = 0, l = callbacks.length; i < l; i++) {
                    var callback = callbacks[i];
                    callback[0].apply(callback[1], tail); 
                }
            }
        };
    }()
};