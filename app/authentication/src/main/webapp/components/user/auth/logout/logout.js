//# sourceURL=logoutController.js
define([
        'angular'
    ], function (angular) {
        return ["$rootScope", "$scope", "$cookies",
            function (rootScope, scope, cookies) {
                cookies.remove("AuthToken", {path:"/"});
                window.location.href = "/auth/#/loginByPIN";
            }];
    }
);