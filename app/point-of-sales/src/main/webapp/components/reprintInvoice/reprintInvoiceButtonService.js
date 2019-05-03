//# sourceURL=pos.reprintInvoiceButtonService.js
define([
    'angular'
], function (angular) {
    return ['pos.areaService', '$rootScope', 'pos.reprintInvoiceService',
        function (screenService, rootScope, reprintInvoiceService) {

            this.doAction = function(element){
                var selectedRows = rootScope.jkGridApi[element.serviceData.invoiceGridId].selection.getSelectedRows();
                if(selectedRows.length == 0){
                    swal({
                        type : "error",
                        title : "Keine Rechnung ausgewählt",
                        text : "Bitte wählen Sie eine Rechnung aus der Rechnungsliste aus!"
                    });
                    return;
                }
                reprintInvoiceService.reprintInvoice(selectedRows[0].value);
            }

        }];
});