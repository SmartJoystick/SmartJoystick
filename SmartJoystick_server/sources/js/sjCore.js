/* CORE SJ APP FUNCTIONS (NOT DEVELOPERS API)
    Handle everything about the application lifetime
*/

var sjCore = (function(){

	var clients = [];

    /*===============
    Clients functions
    ================*/

	var addClient = function(socket){
		clients.push(socket);
        console.log('in sjcore',clients);
	};

	var removeClient = function(socket){
		clients.splice(clients.indexOf(socket), 1);
        console.log('in sjcore',clients);
	};

    var getClients = function(){
        return clients;
    };

    /*=====================
    Communication functions
    ======================*/

	var broadcast = function(userId, message){
		clients.forEach(function (client) {
	      if (client.userId === userId) return;
	      client.write(message);
	    });
	};

    var send = function(userId, message){
        clients.forEach(function(client){
            if(client.userId == userId){
                client.write(message + '\n');
                return;
            }
        })
    };


    /*==============
    Events functions
    ===============*/

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
	}();

	return {
		addClient    : addClient,
        removeClient : removeClient,
        getClients   : getClients,
		broadcast    : broadcast,
        send         : send,
        events       : events
	}

})()

