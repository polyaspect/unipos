//# sourceURL=pos.loadingService.js
define([
    'angular'
], function (angular) {
    return ['$rootScope', '$uibModal',
        function (rootScope, uibModal) {
            this.serviceName = "loadingService";
            this.pendingTasks = [];
            var self = this;
            var modal;
            this.loading = false;

            this.startLoading = function (key, message) {
                this.pendingTasks.forEach(function (pendingTask) {
                    if (pendingTask.key == key) {
                        self.pendingTasks.splice(self.pendingTasks.indexOf(pendingTask), 1);
                    }
                });
                this.pendingTasks.push({key: key, message: message, pending: true});

                if (!self.loading) {
                    var useFullScreen = true;
                    modal = uibModal.open({
                        controller: 'pos.loadingController',
                        templateUrl: 'components/loading/loading.tmpl.html',
                        appendTo: angular.element(document.body),
                        size: 'sm',
                        clickOutsideToClose: false,
                        fullscreen: useFullScreen
                    });
                    self.loading = true;
                } else {
                    rootScope.$emit(self.serviceName + "-" + "loading");
                }
            };

            this.finishLoading = function (key) {
                var areTherePendingTasks = false;
                this.pendingTasks.forEach(function (pendingTask) {
                    if (pendingTask.key == key) {
                        pendingTask.pending = false;
                    }
                    if (pendingTask.pending == true) {
                        areTherePendingTasks = true;
                    }
                });
                rootScope.$emit(self.serviceName + "-" + "loading");

                if (!areTherePendingTasks) {
                    modal.close();
                    self.loading = false;
                }
            };

            this.errorLoading = function (key, error) {
                var areTherePendingTasks = false;
                this.pendingTasks.forEach(function (pendingTask) {
                    if (pendingTask.key == key) {
                        pendingTask.pending = false;
                    }
                    if (pendingTask.pending == true) {
                        areTherePendingTasks = true;
                    }
                });
                rootScope.$emit(self.serviceName + "-" + "loading");

                if (!areTherePendingTasks) {
                    modal.close();
                    self.loading = false;
                    swal({
                        title: 'Fehler',
                        type: 'error',
                        text: error
                    });
                }
            };
        }];
});