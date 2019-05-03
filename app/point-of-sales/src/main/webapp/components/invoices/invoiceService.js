//# sourceURL=pos.invoiceService.js
define([
    'angular'
], function (angular) {
    return ['$rootScope', 'pos.urlSettings', '$http',
        function (rootScope, urlSettings, http) {
            this.serviceName = "showInvoicesGridService";
            this.invoices = [];
            var self = this;

            this.addInvoice = function (invoice) {
                self.invoices.push(invoice);
                rootScope.$emit(self.serviceName + "-" + "invoices");
                rootScope.$emit("pos.cashbookEntryService-");
            };

            this.syncInvoicesFromServer = function(){
                http.get(urlSettings.baseUrl + "/pos/invoices/findAllSinceLastClosedDailySettlement").then(function (response) {
                    self.invoices = response.data;
                    rootScope.$emit(self.serviceName + "-" + "invoices");
                });
            }
        }];
});