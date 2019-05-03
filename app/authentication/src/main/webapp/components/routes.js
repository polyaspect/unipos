define([
        'angular',
        'auth.angularRoute'
    ], function () {
        return function (baseUrl, baseDir) {
            return ['$routeProvider', function ($routeProvider) {
                $routeProvider.when(baseUrl + '/users', {
                        templateUrl: baseDir + 'components/user/userIndex.html',
                        controller: 'userIndexController'
                }).when(baseUrl + '/loginByUsername', {
                    templateUrl: baseDir + 'components/user/auth/usernamePassword/usernamePassword.html',
                    controller: 'usernamePasswordController'
                }).when(baseUrl + "/loginByPIN", {
                    templateUrl: baseDir + "components/user/auth/mitarbeiterPin/mitarbeiterPin.html",
                    controller: "mitarbeiterPinController"
                }).when(baseUrl + "/logout", {
                    templateUrl: baseDir + "components/user/auth/logout/logout.html",
                    controller: "logoutController"
                }).otherwise({ redirectTo: '/users' });
            }];
        }
    }
);

