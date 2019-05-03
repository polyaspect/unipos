//# sourceURL=core.routes.js
define([
        'angular',
        'core.angularRoute'
    ], function () {
        return ['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/logs/showLogs', {
                templateUrl: 'components/log/logView.html',
                controller: 'logController'
            }).when('/nav', {
                templateUrl: 'components/nav/navView.html',
                controller: 'navController'
            }).when('/', {
                templateUrl: 'components/nav/redirectView.html',
                controller: 'redirectController'
            }).when('/license/enterActivationCode', {
                templateUrl: 'components/activation/activationView.html',
                controller: 'ActivationController'
            }).when('/license/activateSuccess', {
                templateUrl: 'components/activation/success/activationSuccessController.html',
                controller: 'ActivationSuccessController'
            }).when('/update', {
                templateUrl: 'components/update/updateView.html',
                controller: 'UpdateController'
            }).otherwise({
                redirectTo: '/'
            });
        }];
    }
);
