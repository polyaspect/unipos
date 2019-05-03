//# sourceURL=pos.loadingController.js
define([
    'angular',
    'pos.linq'
], function (angular) {
    return ['$scope', '$rootScope', 'pos.dataService', 'pos.loadingService', '$uibModalInstance', function (scope, rootScope, dataService, loadingService, uibModalInstance) {
        scope.pendingTasks = loadingService.pendingTasks;
        rootScope.$on(dataService.serviceName + "-" + "loading", function () {
            scope.pendingTasks = loadingService.pendingTasks;
            var areTherePendingTasks = Enumerable.From(scope.pendingTasks).Any(function (x) {
                return x.pending == true;
            });
            if (!areTherePendingTasks) {
                uibModal.hide();
                self.loading = false;
            }
        });
    }];
});