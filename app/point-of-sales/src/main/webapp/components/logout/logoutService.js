//# sourceURL=pos.logoutService.js
define([
    'angular'
], function (angular) {
    return ['pos.initOptions', 'pos.ui.elements.text.textDirectiveService', '$cookies', '$location', 'pos.loginService','pos.socketService', function (initOptions, textService, cookies, location, loginService,socketService) {
        var self = this;
        this.doAction = function (cell) {
            cookies.remove("AuthToken", {path:"/"});
            socketService.disconnect();
            loginService.clearLoginData();
            location.path("login/");
        }
    }];
});