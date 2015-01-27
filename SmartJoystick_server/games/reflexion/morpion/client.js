
sjClient.connect(function(client){

    var isCurrenPlayer = false;
    var idPlayer = null;
    var colorPlayer;

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

    // exec when message is receive from server    
    client.events.on('message', function(data){

        if(data.status == "waiting"){
            client.getWaitingPage('waiting.html');
        }
        else if(data.status == "start"){
            client.removeWaitingPage();

            //set initialization data
            isCurrenPlayer = data.currentPlayer;
            idPlayer = data.idPlayer;
            colorPlayer =  idPlayer == 1 ? 'red' : 'yellow';

            if(isCurrenPlayer){
                alert('you start');
            }
            else{
                alert('waiting for other player to play');
            }

        }
        else if(data.status == "running"){
            isCurrenPlayer = data.currentPlayer;
            //TODO updtae context 
            context = data.context;
            draw(data.context.board);
        }
        else if(data.status == "finish"){


            if(data.winner == idPlayer){
                context = data.context;
                draw(data.context.board);
                alert('You win');
            }
            else if(data.winner == 0){
                context = data.context;
                draw(data.context.board);
                alert('It\'s a draw')
            }
            else{
                context = data.context;
                draw(data.context.board);
                alert('You lose');
            }
        }

    });


    // exec when user click on the grid
    $('.tictacdiv').click(function(){

        var area = this.getAttribute("name");

        if(!isCurrenPlayer){
            alert('Not your turn to play')
        }
        else{
            if(context.board[area] != 0){
                alert('case already hit')
            }
            else{
                this.style.background = colorPlayer;
                context.board[area] = idPlayer;

                isCurrenPlayer = false;
                client.send(context);
            }
        }
    });

})




function draw(obj){
    for(key in obj){

        var elem = document.getElementsByName(key)[0];

        if(obj[key] == 1){
          elem.style.background = 'red';  
        }
        else if(obj[key] == 2){
            elem.style.background = 'yellow'; 
        } 
    }
}


