//# sourceURL=pos.loggedOutStrategy.js
define([
    'angular'
], function (angular) {
    return {
        enterButton: function (injector) {
            var loginService = injector.get("pos.loginService");
            loginService.doAction();
        }
    };
});