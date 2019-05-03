//# sourceURL=prinnterEditorController.js
define([
    'angular',
    'printer.linq',
    'css!printer.printerEditorController'
], function (angular, Enumerable) {
    return ["$scope", 'printerService', 'initOptions', '$uibModal', '$uibModalInstance', 'companyService', 'model', 'create',
        function (scope, printerService, initOptions, uibModal, uibModalInstance, companyService, model, create) {
            scope.obj = {};
            scope.model = model;
            scope.initOptions = initOptions;
            scope.create = create;
            scope.printerTypes = [{key: "NETWORK", value: "Netzwerk Drucker"}, {
                key: "SERIAL",
                value: "Serieller Drucker"
            }, {key: "USB", value: "USB Drucker"}];

            companyService.findAll().$promise.then(function (data) {
                scope.companies = Enumerable.from(data).where(function (x) {
                    return x.stores && x.stores.length > 0
                }).toArray();
                Enumerable.from(scope.companies).forEach(function (x) {
                    Enumerable.from(x.stores).forEach(function (y) {
                        Enumerable.from(model.stores).forEach(function (z) {
                            if (y.guid == z) {
                                scope.onStoreSelected(x, y);
                                y.checked = true;
                            }
                        });
                    });
                });
            });

            scope.onCompanySelected = function (company, event) {
                Enumerable.from(company.stores).forEach(function (x) {
                    x.checked = !company.checked;
                });
                event.stopPropagation();
                event.preventDefault();
            };
            scope.onStoreSelected = function (company, store) {
                company.checked = Enumerable.from(company.stores).any(function (x) {
                    return (x.id == store.id) ? !x.checked : x.checked;
                });
            };
            scope.printCurrentLogo = function () {
                printerService.printCurrentLogo(scope.model).$promise.then(function () {
                }, function () {
                    swal({
                        title: "Logo konnte nicht gedruckt werden",
                        text: "Vergewissen Sie sich dass der Drucker erreichbar ist!",
                        type: "error"
                    })
                });
            };
            scope.testPrinting = function () {
                var promise = printerService.testPrinting(scope.model);
                promise.$promise.then(function (data) {
                        swal({
                            title: "Testdruck erfolgreich",
                            text: "Testseite wurde erfolgreich an Ihren Drucker gesendet!",
                            type: "success",
                            timer: 2000
                        })
                    },
                    function (data) {
                        swal({
                            title: "Testdruck nicht erfolgreich",
                            text: "Die Testseite konnte nicht erfolgreich gedruckt werden. Ist ihr Drucker erreichbar?",
                            type: "error"
                        });
                    });
            };
            scope.save = function () {
                if (scope.obj && scope.obj.flow.files && scope.obj.flow.files.length == 1) {
                    scope.model.printerLogo = scope.obj.flow.files[0].file;
                }
                var stores = [];
                Enumerable.from(scope.companies).forEach(function (company) {
                    Enumerable.from(company.stores).forEach(function (store) {
                        if (store.checked) {
                            stores.push(store.guid);
                        }
                    });
                });
                scope.model.stores = stores;
                uibModalInstance.close(true);
            };
            scope.delete = function () {
                if (printerService.checkIfResourceInstance(scope.model)) {
                    var promise = printerService.deleteByProductNumber(scope.model.id);
                    promise.$promise.then(function () {
                        uibModalInstance.close(false);
                    });
                }
            };
        }];
});
