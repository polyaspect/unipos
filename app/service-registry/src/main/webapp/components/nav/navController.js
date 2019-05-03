//# sourceURL=core.navController.js
define([
    'angular'
], function (angular) {
    return ['$scope', '$location', function (scope, $location) {
        scope.index = 0;

        scope.go = function (path) {
            window.location = path;
            mdSidenav('left').toggle();
        };
    }];
});