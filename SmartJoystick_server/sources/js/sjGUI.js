/*
    Handle everything about the application GUI

*/

var sjApp = angular.module('sjApp', []);

sjApp.controller('mainCtrl', function($scope){

    $scope.clients = sjCore.getClients(); 
    $scope.nbClients = $scope.clients.length;

    sjCore.events.on('connection', function(socket){
        console.log(socket.userId + 'join the app');
        $scope.nbClients++;
        $scope.$apply();
    });

    sjCore.events.on('end', function(socket){
        console.log(socket.userId + 'leave the app');
        $scope.nbClients--;
        $scope.$apply();
    })


});


