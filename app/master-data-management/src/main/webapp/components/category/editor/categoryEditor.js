//# sourceURL=categoryEditorController.js
define([
    'angular',
    'css!data.productEditorController'
], function (angular) {
    return ["$scope", 'categoryService', 'taxRateService', 'initOptions', '$uibModal', '$uibModalInstance', 'model', 'create',
        function (scope, categoryService, taxRateService, initOptions, uibModal, uibModalInstance, model, create) {
            var self = this;
            scope.model = model;
            scope.initOptions = initOptions;
            scope.create = create;

            taxRateService.findAll().$promise.then(function (data) {
                scope.taxRates = data;
            });

            scope.saveProduct = function () {
                uibModalInstance.close(true);
            };
            scope.deleteProduct = function () {
                if (categoryService.checkIfResourceInstance(scope.model)) {
                    var promise = categoryService.deleteByGuid(scope.model.guid);
                    promise.$promise.then(function () {
                        uibModalInstance.close(false);
                        swal({
                            title: "Gelöscht",
                            text: "Die Warengruppe wurde erfolgreich gelöscht!",
                            type: "success",
                            timer: 2000,
                            showConfirmaButton: false
                        });
                    });
                }
            };
        }];
});
