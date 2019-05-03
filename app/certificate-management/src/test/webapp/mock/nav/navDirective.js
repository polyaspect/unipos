//# sourceURL=core.navDirective.js
define([
    'angular',
    'css!core.navDirectiveCss'
], function (angular) {
    return ['$rootScope', function (rootScope) {
        return {
            restrict: 'E',
            templateUrl: 'navDirective.html',
            replace: true
        };
    }];
});