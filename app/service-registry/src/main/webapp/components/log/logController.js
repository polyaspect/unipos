//# sourceURL=logController.js
define([
    'angular',
    'core.linq',
    'css!core.logController'
], function (angular, Enumerable, moment) {
    return ["$scope", "logService", "$uibModal", function ($scope, logService, uibModal) {
        var $this = this;
        $scope.data = [];
        $scope.options = {
            size: 100,
            page: -1,
            startDate: new Date(new Date().setTime(new Date().getTime() + -7 * 86400000)),
            endDate: new Date()
        };
        $scope.loadMore = function (replace) {
            $scope.options.page++;
            if (replace) {
                $scope.options.page = 0;
            }
            logService.showLogs($scope.options).$promise.then(function (resData) {
                if (replace) {
                    $scope.data = resData;
                } else {
                    $scope.data = $scope.data.concat(resData);
                }
            });
        };
        $scope.showLog = function (ev, log) {
            var text = angular.toJson(log, true);
            uibModal.open({
                templateUrl: "logRowContorller.tmpl.html",
                controller: LogRowController,
                resolve: {
                    log: log
                },
                size: 'lg',
                windowClass: 'log-controller-modal'
            });
        };

        function LogRowController(scope, log) {
            scope.log = log;
        }
        LogRowController.$inject = ['$scope','log'];
    }]
});