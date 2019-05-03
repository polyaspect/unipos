//# sourceURL=reviewEditorController.js
define([
    'signature.sweetAlert',
    'angular',
    'css!signature.zeroInvoiceEditorController'
], function (swal, angular) {
    return ["$scope", 'zeroInvoiceService', 'initOptions', '$uibModal', '$uibModalInstance', 'model',
        function (scope, zeroInvoiceService, initOptions, uibModal, uibModalInstance, model) {
            scope.model = model;
            scope.initOptions = initOptions;
            scope.showLoading = true;
            var promise = zeroInvoiceService.getInvoicePreviewText(scope.model);
            promise.then(function (data) {
                scope.previewData = data.data.trim();
                scope.showLoading = false;
            });
            scope.save = function () {
                uibModalInstance.close(true);
            };
            scope.reprintInvoice = function () {
                var promise = zeroInvoiceService.reprintInvoice(scope.model);
                promise.then(function (data) {
                        swal({
                            title: "Rechnung erneut gedruckt",
                            text: "Ihre Rechnung wurde an ihren Standarddrucker gesendet.",
                            type: "success",
                            timer: 3000
                        })
                    },
                    function (error) {
                        swal({
                            title: "Rechnungsdruck fehlgeschlagen",
                            text: "Ihre Rechnung konnte leider nicht gedruckt werden. Überprüfen Sie bitte die Druckeinstellungen.",
                            type: "error"
                        })
                    })
            };
        }];
});
