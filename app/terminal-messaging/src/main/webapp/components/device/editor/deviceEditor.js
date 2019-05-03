//# sourceURL=deviceEditorController.js
define([
    'angular',
    'socket.linq',
    'css!socket.deviceEditorController'
], function (angular, Enumerable) {
    return ["$scope", 'deviceService', 'printerService', 'socket.storeService', 'initOptions', '$uibModal', '$uibModalInstance', 'model', 'create',
        function (scope, deviceService, printerService, storeService, initOptions, uibModal, uibModalInstance, model, create) {
            scope.model = model;
            scope.initOptions = initOptions;
            scope.create = create;

            printerService.findAvailablePrinter().$promise.then(function (printers) {
                scope.printers = printers;
                printerService.findDefaultPrinterOfDeviceId(scope.model.deviceId).$promise.then(function (defaultPrinter) {
                    scope.defaultPrinter = defaultPrinter;
                    angular.forEach(printers, function (data) {
                        if (data.guid === defaultPrinter.printerGuid) {
                            data.checked = true;
                            scope.model.selectedPrinter = data;
                        }
                    });
                });
            });

            storeService.findAvailableStores().$promise.then(function (stores) {
                scope.stores = stores;
                storeService.findDefaultStoreOfDeviceId(scope.model.deviceId).$promise.then(function (defaultStore) {
                    scope.defaultStore = defaultStore;
                    angular.forEach(stores, function (data) {
                        if (data.guid === defaultStore.guid) {
                            data.checked = true;
                            scope.model.selectedStore = data;
                        }
                    });
                });
            });

            scope.save = function () {
                scope.model.printers = Enumerable.from(scope.printers).select(function (x) {
                    return {printerGuid:x.guid};
                }).toArray();
                Enumerable.from(scope.model.printers).forEach(function (x) {
                    if(x.printerGuid == scope.model.selectedPrinter.guid){
                        x.defaultPrinter = true;
                    }else{
                        x.defaultPrinter = false;
                    }
                });
                uibModalInstance.close(true);
            };
            scope.onPrinterSelected = function (printer, event) {
                Enumerable.from(scope.printers).forEach(function (x) {
                    if (x.guid != printer.guid) {
                        x.checked = false;
                    }
                });
                scope.model.selectedPrinter = printer;
                event.stopPropagation();
                event.preventDefault();
            };
            scope.onStoreSelected = function (store, event) {
                Enumerable.from(scope.stores).forEach(function (x) {
                    if (x.guid != store.guid) {
                        x.checked = false;
                    }
                });
                scope.model.selectedStore = store;
                event.stopPropagation();
                event.preventDefault();
            };
            scope.delete = function () {
                if (deviceService.checkIfResourceInstance(scope.model)) {
                    var promise = deviceService.deleteByGuid(scope.model.deviceId);
                    promise.$promise.then(function () {
                        uibModalInstance.close(false);
                    });
                }
            };
        }];
});
