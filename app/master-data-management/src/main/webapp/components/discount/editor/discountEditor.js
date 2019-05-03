//# sourceURL=discountEditorController.js
define([
    'angular',
    'css!data.productEditorController'
], function (angular) {
    return ["$scope", 'discountService', 'initOptions', '$uibModal', '$uibModalInstance', 'model', 'create',
        function (scope, discountService, initOptions, uibModal, uibModalInstance, model, create) {
            scope.model = model;
            scope.initOptions = initOptions;
            scope.create = create;
            scope.discountUsages = [{key: "SINGLE", value: "Bonierung"}, {key: "ORDER", value: "Bestellung"}];
            scope.discountTypes = [{key: "VALUE", value: "Wert"}, {key: "PERCENTAGE", value: "Prozent"}];

            scope.save = function () {
                uibModalInstance.close(true);
            };
            scope.delete = function () {
                if (discountService.checkIfResourceInstance(scope.model)) {
                    var promise = discountService.deleteByDiscountIdentifier(scope.model.discountIdentifier);
                    promise.$promise.then(function () {
                        uibModalInstance.close(false);
                        swal({
                            title: "Gelöscht",
                            text: "Der Rabatt wurde erfolgreich gelöscht! Um die Änderungen zu aktivieren, drücken Sie bitte auf 'Freigeben'.",
                            type: "success",
                            timer: 5000,
                            showConfirmaButton: false
                        });
                    });
                }
            };
        }];
});
