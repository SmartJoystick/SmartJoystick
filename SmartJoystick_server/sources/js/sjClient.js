var url = document.URL;
var address = location.hostname;
var port = getURLParameter('port');

var sjClient = (function(){

    // connect and init

    var connect = function(callback){
        var ws = new WebSocket("ws://"+address+":"+port);

        _this = this;

        ws.onmessage = function(e){
            var parsed = JSON.parse(e.data);
            _this.events.fire('message', parsed);
        }
      
        this.send = function(data){
            var stringify = JSON.stringify(data);
            ws.send(stringify);
        }

        callback(this);
    }

    // Waiting Page

    var getWaitingPage = function(waitingPage){
        $.ajax({
          url:  waitingPage
        })
        .done(function(html) {
            var $waitingPage = $('<div id="sjWaiting"></div>');
            $('#sjGame').hide();
            $waitingPage.html(html);
            $('body').prepend($waitingPage)
        });
    }

    var removeWaitingPage = function(){
        $('#sjWaiting').remove();
        $('#sjGame').show();
    }




    // Events
   
    var events = function() {

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



    return {
        events : events,
        connect : connect,
        getWaitingPage : getWaitingPage,
        removeWaitingPage : removeWaitingPage
    }

    
    
})();

function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null
}
