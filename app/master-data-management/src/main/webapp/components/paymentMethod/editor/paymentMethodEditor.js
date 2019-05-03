//# sourceURL=paymentMethodEditorController.js
define([
    'angular',
    'css!data.productEditorController'
], function (angular) {
    return ["$scope", 'paymentMethodService', 'initOptions', '$uibModal', '$uibModalInstance', 'model', 'create',
        function (scope, paymentMethodService, initOptions, uibModal, uibModalInstance, model, create) {
            scope.model = model;
            scope.initOptions = initOptions;
            scope.create = create;
            scope.types = [{key: "BAR", value: "Bar"}, {key: "BANKOMAT", value: "Bankomat"}, {
                key: "KREDITKARTE",
                value: "Kreditkarte"
            }, {key: "GUTSCHEIN", value: "Gutschein"}, {key: "ESSENSBON", value: "Essensbon"}, {key: "RECHNUNG", value: "Rechnung"}];

            scope.saveProduct = function () {
                uibModalInstance.close(true);
            };
            scope.deleteProduct = function () {
                if (paymentMethodService.checkIfResourceInstance(scope.model)) {
                    var promise = paymentMethodService.deleteByPaymentMethodIdentifier(scope.model.paymentMethodIdentifier);
                    promise.$promise.then(function () {
                        uibModalInstance.close(false);
                        swal({
                            title: "Gelöscht",
                            text: "Die Zahlungsart wurde erfolgreich gelöscht! Um die Änderungen zu aktivieren, drücken Sie bitte auf 'Freigeben'.",
                            type: "success",
                            timer: 5000,
                            showConfirmaButton: false
                        });
                    });
                }
            };
        }];
});
