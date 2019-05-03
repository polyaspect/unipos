//# sourceURL=taxRateEditorController.js
define([
    'angular',
    'css!data.productEditorController'
], function (angular) {
    return ["$scope", 'taxRateService', 'initOptions', '$uibModal', '$uibModalInstance', 'model', 'create',
        function (scope, taxRateService, initOptions, uibModal, uibModalInstance, model, create) {
            scope.model = model;
            scope.initOptions = initOptions;
            scope.create = create;
            scope.types = [{key: "NORMAL", value: "Normal"}, {key: "DISCOUNT", value: "Rabatt"}];

            scope.save = function () {
                uibModalInstance.close(true);
            };
            scope.delete = function () {
                if (taxRateService.checkIfResourceInstance(scope.model)) {
                    var promise = taxRateService.deleteByGuid(scope.model.guid);
                    promise.$promise.then(function () {
                        uibModalInstance.close(false);
                        swal({
                            title: "Gelöscht",
                            text: "Die Steuerklasse wurde erfolgreich gelöscht!",
                            type: "success",
                            timer: 2000,
                            showConfirmaButton: false
                        });
                    });
                }
            };
        }];
});
