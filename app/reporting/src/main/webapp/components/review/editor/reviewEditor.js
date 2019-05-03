//# sourceURL=reviewEditorController.js
define([
    'angular',
    'css!report.reviewEditorController'
], function (angular) {
    return ["$scope", 'reviewService', 'initOptions', '$uibModal', '$uibModalInstance', 'model',
        function (scope, reviewService, initOptions, uibModal, uibModalInstance, model) {
            scope.model = model;
            scope.initOptions = initOptions;
            scope.showLoading = true;
            var promise = reviewService.getInvoicePreviewText(scope.model);
            promise.then(function (data) {
                scope.previewData = data.data.trim();
                scope.showLoading = false;
            });
            scope.save = function () {
                uibModalInstance.close(true);
            };
            scope.reprintInvoice = function () {
                var promise = reviewService.reprintInvoice(scope.model);
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

            scope.revertInvoiceButton = function (invoiceNo) {
                swal({
                        title: "Sind Sie sicher?",
                        text: "Wollen Sie die Rechung mit der Rechnungsnummer " + scope.model.invoiceId + " wirklich stornieren?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55",
                        confirmButtonText: "Ja, ich bin mir sicher",
                        cancelButtonText: "Abbrechen",
                        closeOnConfirm: false
                    },
                    function () {
                        var promise = reviewService.revertInvoice(scope.model.guid)
                        promise.then(function () {
                                swal({
                                    title: "Erfolgreich storniert",
                                    text: "Die Rechnung wurde erfolgreich storniert und ein Storno-Beleg gedruckt",
                                    type: "success",
                                    timer: 3000
                                });
                                $this.updateUiGridDataFromDatabase();
                            },
                            function () {
                                swal({
                                    title: "Fehler",
                                    text: "Beim stornieren der Rechnung ist ein Fehler aufgetreten. Dieser Text kann auch dann auftreten, wenn ihr Drucker nicht angeschlossen war!",
                                    type: "error"
                                });
                            }
                        )
                    }
                );
            };
        }];
});
