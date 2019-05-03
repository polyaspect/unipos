//# sourceURL=pos.hyperDirective.js
define([
    'angular',
], function (angular) {
    return ['$timeout', '$rootScope', function (timeout, rootScope) {
        return {
            restrict: 'A',
            link: function (scope, el, attrs) {
                if (!rootScope.count) {
                    rootScope.count = 0;
                }
                rootScope.count++;
                timeout(function () {
                    rootScope.count--;
                    if (rootScope.count == 0) {
                        angular.element(attrs.hyperSelector).hyphenate('de');
                    }
                }, 500);
            }
        }
    }];
});