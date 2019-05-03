//# sourceURL=pos.reprintInvoiceService.js
define([
    'angular'
], function (angular) {
    return ['$http',
        function ($http) {
            this.reprintInvoice = function(invoice) {
                var res = $http.post("/report/invoiceReports/reprintInvoice", invoice);

                res.success(function () {
                    swal({
                        title: "Erfolgreich nachgedruckt",
                        text: "Die Rechnung wurde erfolgreich nachgedruckt",
                        type: "success",
                        timer: 3000
                    });
                });

                res.error(function () {
                        swal({
                            title: "Fehler",
                            text: "Beim Nachdruck der Rechnung ist ein Fehler aufgetreten. Überprüfen Sie, ob der Drucker korrekt angeschlossen ist.",
                            type: "error",
                            timer: 3000
                        });
                    }
                );
            };
        }];
});