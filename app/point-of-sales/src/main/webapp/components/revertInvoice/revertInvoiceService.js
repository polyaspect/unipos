//# sourceURL=pos.revertInvoiceService.js
define([
    'angular'
], function (angular) {
    return ['pos.queueService', 'pos.invoiceService', 'pos.urlSettings', '$rootScope',
        function (queueService, invoiceService, urlSettings, rootScope) {

            this.revertInvoice = function (invoice) {
                queueService.addToQueue({
                    url: urlSettings.baseUrl + "/pos/invoices/revertInvoice",
                    method: "post",
                    data: "invoiceGuid=" + invoice.guid,
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    callback: function (data) {
                        invoice.reverted = true;
                        invoiceService.addInvoice(data);
                        swal({
                            title: "Erfolgreich storniert",
                            text: "Die Rechnung wurde erfolgreich storniert und ein Storno-Beleg gedruckt",
                            type: "success",
                            timer: 3000
                        });
                    },
                    errorCallback: function (response) {
                        swal({
                            title: "Fehler",
                            text: "Beim Stornieren der Rechnung ist ein Fehler aufgetreten. Überprüfen Sie, ob der Drucker korrekt angeschlossen ist.",
                            type: "error",
                            timer: 3000
                        });
                    }
                });


            };
        }];
});