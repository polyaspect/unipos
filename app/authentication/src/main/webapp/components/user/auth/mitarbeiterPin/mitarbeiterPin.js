//# sourceURL=mitarbeiterPinController.js
define([
        'angular',
        'auth.linq',
        'css!auth.mitarbeiterPinController'
    ], function (angular, Enumerable) {
        return ["$rootScope", "$scope", 'initOptions', "mitarbeiterPinService",
            function (rootScope, scope, initOptions, mitarbeiterIdPinService) {
                rootScope.showNav = false;
                scope.pinLogin = function () {
                    if (scope.user && scope.user.mitarbeiternr && scope.user.pin) {
                        var mitarbeiternr = scope.user.mitarbeiternr;
                        var pin = scope.user.pin;
                        mitarbeiterIdPinService.pinLogin(mitarbeiternr, pin).then(function (response) {
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
                };
            }];
    }
);