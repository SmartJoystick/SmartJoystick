var sj = require('../../../sources/js/sjServer.js');

// launch the web server
sj.launchServer();

// launch the watcher (first param is the number of player required to start a game)
sj.watch(2, function(players){
    sj.startGame(players);
});

sj.events.on('connection', function(player){
    sj.wait(player);
    // + DO what you want (ex : send a message to the client to tell a new player is here
    // with broadcast to all activePlayers)
});

sj.events.on('start', function(players){

    // game logic

    var nbPlayer      = players.length;
    var firstPlayer   = players[0];
    var secondPlayer  = players[1];

    var firstPlayerInit = {status : "start", currentPlayer : true, idPlayer : 1};
    var secondPlayerInit = {status : "start", currentPlayer : false, idPlayer : 2};


    var context = {
        board : {
            a1 : 0,
            b1 : 0,
            c1 : 0,
            a2 : 0,
            b2 : 0,
            c2 : 0,
            a3 : 0,
            b3 : 0,
            c3 : 0
        }
    }

    // initialization
    sj.send(firstPlayer, firstPlayerInit);
    sj.send(secondPlayer, secondPlayerInit);


    sj.events.on('message', function(player, contextClient){

        var response1,
            response2;
        // update the grid server side
        var context = contextClient;
        var winner = checkWinner(context);


        // degeu a refaire
        if(player == firstPlayer && !winner.isAWinner){
            response1 = {status : "running", currentPlayer : false, context : context};
            response2 = {status : "running", currentPlayer : true, context : context};
        }
        else if(player != firstPlayer && !winner.isAWinner){
            response1 = {status : "running", currentPlayer : true, context : context};
            response2 = {status : "running", currentPlayer : false, context : context};
        }
        else if(winner.isAWinner){
            if(winner.player == 1){
                response1 = {status : "finish", winner : 1, context : context}
                response2 = {status : "finish", winner : 1, context : context}
            }
            else if(winner.player == 2){
                response1 = {status : "finish", winner : 2, context : context}
                response2 = {status : "finish", winner : 2, context : context}
            }
            else if(winner.player == 0){
                response1 = {status : "finish", winner : 0, context : context}
                response2 = {status : "finish", winner : 0, context : context}
            }
        }
        

        sj.send(firstPlayer, response1);
        sj.send(secondPlayer, response2);
    })
});



sj.events.on('close', function(player, wasActive){
    console.log('one player leave the game');
});

sj.events.on('end', function(){
    console.log('end of the game');
});



function checkWinner(context){

    var winner = {};

    var a1 = context.board.a1;
    var b1 = context.board.b1;
    var c1 = context.board.c1;

    var a2 = context.board.a2;
    var b2 = context.board.b2;
    var c2 = context.board.c2;

    var a3 = context.board.a3;
    var b3 = context.board.b3;
    var c3 = context.board.c3;



    if(a1==1&&b1==1&&c1==1){winner.isAWinner = true; winner.player = 1}
    else if(a1==1&&b2==1&&c3==1){winner.isAWinner = true; winner.player = 1}
    else if(a1==1&&a2==1&&a3==1){winner.isAWinner = true; winner.player = 1}
    else if(a2==1&&b2==1&&c2==1){winner.isAWinner = true; winner.player = 1}
    else if(b1==1&&b2==1&&b3==1){winner.isAWinner = true; winner.player = 1}
    else if(c1==1&&c2==1&&c3==1){winner.isAWinner = true; winner.player = 1}
    else if(a3==1&&b3==1&&c3==1){winner.isAWinner = true; winner.player = 1}
    else if(c1==1&&b2==1&&a3==1){winner.isAWinner = true; winner.player = 1}

    else if(a1==2&&b1==2&&c1==2){winner.isAWinner = true; winner.player = 2}
    else if(a1==2&&b2==2&&c3==2){winner.isAWinner = true; winner.player = 2}
    else if(a1==2&&a2==2&&a3==2){winner.isAWinner = true; winner.player = 2}
    else if(a2==2&&b2==2&&c2==2){winner.isAWinner = true; winner.player = 2}
    else if(b1==2&&b2==2&&b3==2){winner.isAWinner = true; winner.player = 2}
    else if(c1==2&&c2==2&&c3==2){winner.isAWinner = true; winner.player = 2}
    else if(a3==2&&b3==2&&c3==2){winner.isAWinner = true; winner.player = 2}
    else if(c1==2&&b2==2&&a3==2){winner.isAWinner = true; winner.player = 2}


    else if(a1!=0 && b1!=0 && c1!=0 && a2!=0 && b2!=0 && c2!=0 && a3!=0 && b3!=0 && c3!=0){
        winner.isAWinner = true; winner.player = 0;
    }

    else{
        winner.isAWinner = false;
        winner.player = false;
    }

    return winner;
}

