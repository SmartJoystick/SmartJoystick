var url = document.URL;
var address = location.hostname;
var port = getURLParameter('port');

var ws = new WebSocket("ws://"+address+":"+port);

ws.onmessage = function(e)
{
    if(e.data == "waiting"){
        var baseUrl = url.substring(0, url.lastIndexOf("/"));
        window.location.href = baseUrl + '/waiting.html'
    }
}


function getURLParameter(name) {
  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null
}