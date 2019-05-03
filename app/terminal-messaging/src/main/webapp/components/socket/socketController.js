//# sourceURL=socketController.js

define([
        'angular',
        'socket.angularResource',
        'socket.sockJs',
        'socket.ngStomp',
        'socket.stompie',
        'socket.stomp'
    ], function () {
        return ['$scope', '$resource', '$stomp', '$http', function ($scope, $resource, $stomp, $http) {
            var asdf;

            var readCookie = function (cookieName) {
                var theCookie = " " + document.cookie;
                var ind = theCookie.indexOf(" " + cookieName + "=");
                if (ind == -1) ind = theCookie.indexOf(";" + cookieName + "=");
                if (ind == -1 || cookieName == "") return "";
                var ind1 = theCookie.indexOf(";", ind + 1);
                if (ind1 == -1) ind1 = theCookie.length;
                return unescape(theCookie.substring(ind + cookieName.length + 2, ind1));
            };

            var authToken = readCookie("AuthToken");

            $scope.test = "asdf";
            var userId;
            var deviceId;
            $.ajax({
                url: '/auth/auth/getUserIdByAuthToken?authTokenString=' + authToken,
                success: function (result) {
                    userId = result;
                },
                async: false
            });
            $http({
                method: 'GET',
                url: '/auth/auth/getUserIdByAuthToken',
                params: {authTokenString: authToken}
            }).then(function (data) {
                userId = data;

                $stomp.connect("/socket/endpoint")

                    .then(function (frame) {
                        deviceId = frame.body;
                        var subscription = $stomp.subscribe('/topic/newOrder', function (payload, headers, res) {
                            $scope.test = payload.payload;
                            $scope.$apply();
                        });

                        var identifier = "nd7654hd";
                        var subscription = $stomp.subscribe('/queue/test' + "-" + identifier, function (payload, headers, res) {
                            $scope.test = payload.payload;
                            $scope.$apply();
                        }, {sessionId: identifier});

                        $stomp.subscribe('/user/'+data.data+'/reply', function (payload, headers, res) {
                            alert("Received: " + res.body);
                            $scope.$apply();
                        });

                        $stomp.subscribe('/device/'+deviceId+'/reply', function (payload, headers, res) {
                            alert("Received: " + res.body);
                            $scope.$apply();
                        });

                        //$stomp.send('/app/chat', 'ASDF');
                    });
            });

        }];
    }
);