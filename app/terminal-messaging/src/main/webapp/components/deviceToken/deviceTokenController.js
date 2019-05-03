//# sourceURL=socketController.js

define([
        'angular',
        'socket.angularResource',
        'socket.sockJs',
        'socket.ngStomp',
        'socket.stompie',
        'socket.stomp'
    ], function () {
        return ['$scope', function ($scope) {
            function setCookie(name, value, expires, path, domain, secure) {
                var cookieStr = name + "=" + escape(value) + "; ";

                if (expires) {
                    expires = setExpiration(expires);
                    cookieStr += "expires=" + expires + "; ";
                }
                if (path) {
                    cookieStr += "path=" + path + "; ";
                }
                if (domain) {
                    cookieStr += "domain=" + domain + "; ";
                }
                if (secure) {
                    cookieStr += "secure; ";
                }

                document.cookie = cookieStr;
            }

            function setExpiration(cookieLife) {
                var today = new Date();
                var expr = new Date(today.getTime() + cookieLife * 24 * 60 * 60 * 1000);
                return expr.toGMTString();
            }

            setCookie('DeviceToken', '', -1, "/");
            window.location = "/pos/";
        }];
    }
);