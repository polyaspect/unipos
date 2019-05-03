//# sourceURL=pos.chooseOrderItemGridService.js
define([
    'angular'
], function (angular) {
    return ['pos.orderService', 'pos.discountService', 'pos.abstractStrategyFactory', '$injector', function (orderService, discountService, abstractStrategyFactory, injector) {
        var width;
        var quantityLength = 5;
        var priceLength = 9;
        var productLength;
        var orderDiscountLength;
        var orderItemDiscountLength;
        var orderItemDiscountPaddingLeft;


        function setSizes() {
            width = Math.floor((angular.element(".posorderDirectiveService").width() - 40) / getTextWidth("a", "13pt monospace"));
            productLength = width - quantityLength - priceLength;
            orderDiscountLength = width - quantityLength - priceLength;
            orderItemDiscountPaddingLeft = "  ";
            orderItemDiscountLength = width - quantityLength - priceLength - orderItemDiscountPaddingLeft.length;
        }

        function getTextWidth(text, font) {
            var canvas = getTextWidth.canvas || (getTextWidth.canvas = angular.element("<canvas></canvas>")[0]);
            var context = canvas.getContext("2d");
            context.font = font;
            var metrics = context.measureText(text);
            angular.element("canvas").remove();
            return metrics.width;
        }

        function addOrderItem(displayData, value) {
            if (value.turnover == undefined) {
                orderService.deleteOrderItem(value.orderItemId);
                swal("Fehler auf Rechnung", "Beim Verarbeiten einer offenen Rechnung ist ein Fehler aufgetreten: Eine Rechnungsposition hat keinen Umsatz hinterlegt. Um die Weiterarbeit zu ermöglichen, wurde die Rechnungsposition storniert. Überprüfen Sie" +
                    " ob die Rechnung alle Positionen enthält.", "error");
                return;
            }
            displayData.push({
                "Name": value.label,
                "Quantity": value.quantity.toString(),
                "Turnover": parseFloat(value.turnover.toString()).toFixed(2),
                "value": value
            });
        };

        this.getColumnDefs = function () {
            return [{
                name: 'Name',
                field: 'Name',
                width: "60%"
            }, {
                name: 'Quantity',
                field: 'Quantity',
                width: "20%"
            }, {
                name: 'Turnover',
                field: 'Turnover',
                width: "20%"
            }];
        };

        this.getData = function (elementData) {
            setSizes();

            var productOrderItems = abstractStrategyFactory.chooseOrderItemStrategy().getViewData(injector, elementData);
            var displayData = [];
            var index = 1;
            angular.forEach(productOrderItems, function (value, index) {
                if (!value.css) {
                    value.css = {};
                }
                value.css.evenRows = elementData.data && elementData.data.css && elementData.data.css.evenRows;
                value.css.oddRows = elementData.data && elementData.data.css && elementData.data.css.oddRows;
                value.css.selectedRow = elementData.data && elementData.data.css && elementData.data.css.selectedRow;
                value.index = index;
                index++;
                addOrderItem(displayData, value);
            });
            return displayData;
        };

        this.click = function(elementData, row, col){
            abstractStrategyFactory.chooseOrderItemStrategy().chooseOrderItem(injector, elementData, row.entity);
        }
    }];

});