//# sourceURL=pos.showInvoiceGridService.js
define([
    'angular'
], function (angular) {
    return ['pos.invoiceService', '$filter', function (invoiceService, $filter) {
        this.getColumnDefs = function () {
            return [{
                name: 'Rechnungsnummer',
                field: 'invoiceId',
                width: "10%",
                cellClass:"align-right font-weight-bold"
            },{
                name: 'Uhrzeit',
                field: 'creationTime',
                width: "20%",
                cellClass:'font-weight-bold'
            }, {
                name: 'Umsatz',
                field: 'turnover',
                width: "20%",
                cellClass:'font-weight-bold',
                cellFilter:"euroFilter"
            },  {
                name: 'Kassier',
                field: 'cashier',
                width: "30%",
                cellClass:"font-weight-bold"
            },  {
                name: 'Bemerkung',
                field: 'comment',
                width: "20%",
                cellClass:"font-weight-bold"
            }];
        };

        this.getData = function (elementData) {
            var invoices = invoiceService.invoices;

            var displayData = [];
            var index = 1;
            angular.forEach(invoices, function (invoice, index) {
                var value = invoice;
                if (!value.css) {
                    value.css = {
                    };
                }

                if(elementData.data.css.evenRows == undefined){
                    value.css.rows = {};
                }else{
                    value.css.rows = JSON.parse(JSON.stringify(elementData.data && elementData.data.css && elementData.data.css.evenRows));
                }
                value.css.selectedRow = elementData.data && elementData.data.css && elementData.data.css.selectedRow;
                value.index = index;
                index++;


                var comment = "-";

                if(invoice.reverted == true){
                    value.css.rows["color"] = "red";
                    comment = "Storniert";
                }

                if(invoice.turnoverGross < 0){
                    value.css.rows["color"] = "grey";
                    comment = "Storno"
                }

                displayData.push({
                    "invoiceId": invoice.invoiceId,
                    "turnover": parseFloat(invoice.turnoverGross.toString()).toFixed(2),
                    "creationTime": $filter('date')(invoice.creationDate, 'HH:mm:ss'),
                    "cashier": invoice.cashier.name,
                    "comment" : comment,
                    "value": value
                });
            });
            return displayData.reverse();
        }
    }];

});