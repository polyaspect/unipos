//# sourceURL=pos.revertInvoiceButtonService.js
define([
    'angular'
], function (angular) {
    return ['pos.areaService', '$rootScope', 'pos.revertInvoiceService',
        function (screenService, rootScope, revertInvoiceService) {

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
                revertInvoiceService.revertInvoice(selectedRows[0].value);
            }

        }];
});