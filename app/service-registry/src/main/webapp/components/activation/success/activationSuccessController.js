//# sourceURL=activationSuccessController.js
define([
    'angular',
    'css!core.activationSuccessController',
    'core.sweetAlert',
    'css!core.sweetAlertStyle'
], function () {
    return ["$scope", "$http", "$rootScope", "activationService", function (scope, http, rootScope, activationService) {
        rootScope.showNav = false;
        scope.loading = true;
        scope.doUpdate = function () {
            scope.loading = false;
            if (scope.productKey && scope.productKey != "") {
                activationService.install(scope.productKey).$promise.then(function (response) {
                    scope.loading = true;
                });
            }
        };
        scope.continueWithoutUpdate = function () {
            window.location.href = "/data/#/companies";
        }
    }];
});