//# sourceURL=usernamePasswordController.js
define([
        'angular',
        'auth.linq',
        'auth.sweetAlert',
        'css!auth.usernamePasswordController'
    ], function (angular, Enumerable) {
        return ["$rootScope", "$scope", "usernamePasswordService",
            function (rootScope, scope, usernamePasswordService) {
                rootScope.showNav = false;
                scope.passwordLogin = function () {
                    if (scope.user && scope.user.username && scope.user.password) {
                        var username = scope.user.username;
                        var password = scope.user.password;
                        usernamePasswordService.passwordLogin(username, password).then(function (response) {
                            rootScope.showNav = true;
                            if(window.location.href.replace(/%2F/g, "/").split("redirect=")[1] === undefined){
                                window.location.href = "/data/#/products/";
                            }else{
                                window.location.href = window.location.href.replace(/%2F/g, "/").split("redirect=")[1] ;
                                rootScope.reloadNav = true;
                                rootScope.showNav = true;
                            }
                        }, function(error){
                            swal("Login fehlgeschlagen", "Die eingegebenen Anmeldedaten sind nicht korrekt. Bitte versuchen Sie es erneut.", "error");
                        });
                    }
                }
            }];
    }
);