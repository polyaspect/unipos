define([
    'angular',
    'socket.angularRoute'
], function(){
        return function(baseUrl, baseDir) {
            return ['$routeProvider', function ($routeProvider) {
                $routeProvider.when(baseUrl + '/sockets', {
                    templateUrl: baseDir + 'components/socket/socketView.html',
                    controller: 'SocketController'
                }).when(baseUrl + '/devices', {
                    templateUrl: baseDir + 'components/device/deviceView.html',
                    controller: 'DeviceController'
                }).when(baseUrl + '/removeDeviceToken', {
                    templateUrl: baseDir + 'components/deviceToken/deviceTokenView.html',
                    controller: 'DeviceTokenController'
                })
            }];
        }
    }
);

