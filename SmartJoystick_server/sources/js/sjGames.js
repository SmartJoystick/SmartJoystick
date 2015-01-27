var path  = require('path');
var fs    = require('fs');
var async = require('async');
var os    = require('os');
var networks  = os.networkInterfaces();
var address   = networks.wlan0[0].address;

var gamesPath = path.join(path.dirname(process.execPath), 'games');


var sjGames = (function(){

    var availablePort = 5002;
    var allGames = {};
    var activePlayers = [];
    var runningServers = [];
    var runningPort = [];

 
    // called when TCP server starts
    var getGames = function(callback){
      exploreAndFill(gamesPath, function(err, result){
        if (err) return callback(err);
        // fill allgames : object containing all the games (exclude categories)
        for(category in result){
            var cat = result[category];
            for(game in cat){
                allGames[game] = cat[game]
            }
        }
        return callback(null, result);
      })
    };

    var registerPlayer = function(playerId, gameName){

       var game = allGames[gameName];
       activePlayers.push(playerId);
       
       // Launch game server if it is not running
       if(!runningServers[gameName]){

            console.log('game server is going to be launch');

            var serverFile = path.join(gamesPath, game.category, game.title, 'server.js');

            // launch the game server file in an other node proccess
            var spawn = require('child_process').spawn;
            var child = spawn('node', [serverFile, availablePort]);

            // see the output of the server in the console ( spawn --> streams and exec --> buffer)
            child.stdout.on('data', function(chunk) {
                console.log(chunk.toString());
            });
            
            child.stderr.on('data', function(chunk) {
                console.log(chunk.toString());
            });

            console.log('availablePort',availablePort);
            var indexPath = path.join('http://', address+':5001', 'games', game.category, game.title, 'index.html?port='+availablePort);
            sjCore.send(playerId, indexPath);
            runningPort[gameName] = availablePort;

            console.log(indexPath);

            availablePort++;

            runningServers[gameName] = true;
       }

       else{
            console.log('game server already running');
            console.log('availablePort',runningPort[gameName]);

            var indexPath = path.join('http://', address+':5001', 'games', game.category, game.title, 'index.html?port='+runningPort[gameName]);
            sjCore.send(playerId, indexPath);
       }

    };

    return {
        getGames  : getGames,
        registerPlayer : registerPlayer
    }

})();


/*==========================================
    Explore a folder and build an object which represent the file structure
    the object is available in the callback
============================================*/

function exploreAndFill(firstPath, callback){

    var games = {},
        contextTab = [];
    
    var firstContext = {
            path  : firstPath,
            games : games
        };

    return (function _exploreAndFill(context, done){

        contextTab = [];

        var currentPath = context.path;
        var gamesCursor = context.games;

        // Get an array (entries) of all the files in the current directory (link to the currentPath)
        fs.readdir(currentPath, function(err, entries){
            if (err) return done(err);
            // Transform the array of files name in array of files path
            var entries = entries.map(function(entry){return path.join(currentPath,entry)});

            async.each(entries, function(entry, cb){

                var fileName = path.basename(entry);

                fs.stat(entry, function(err, stat){
                    if(stat.isDirectory()){
                        gamesCursor[fileName] = {}
                        var context = { path : entry, games : gamesCursor[fileName]}
                        contextTab.push(context);
                        cb();
                    }
                    else if(stat.isFile()){              
                        fs.readFile(entry, function(err, data){
                            if(fileName == 'config.json'){
                                config = JSON.parse(data.toString());
                                for(prop in config){
                                    if(config.hasOwnProperty(prop)){
                                        gamesCursor[prop] = config[prop];
                                    }
                                }
                            }
                            cb();
                        }); 
                    }
                })
            }, function(err){
                if(err) return done(err);
                return async.each(contextTab, _exploreAndFill, function(err){
                    if(err) return done(err)
                    return done(null, games);
                });
            });

        }) 

    })(firstContext, callback);
}




































