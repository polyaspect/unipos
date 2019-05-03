//# sourceURL=pos.orderDirectiveService.js
define([
    'angular'
], function (angular) {
    return ['pos.orderService', 'pos.discountService', 'pos.ui.elements.text.textDirectiveService', 'pos.abstractStrategyFactory', function (orderService, discountService, textService, abstractStrategyFactory) {
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
            var orderItemDiscount = Enumerable.From(orderService.getOrderItemDiscountItem(orderService.getCurrentOrder())).Where(function (x) {
                return x.receiverOrderItemId == value.orderItemId;
            }).ToArray();
            if (orderItemDiscount && orderItemDiscount.length > 0) {
                angular.forEach(orderItemDiscount, function (value, key) {
                    displayData.push({
                        "Quantity": "1",
                        "Name": "    " + value.label,
                        "Turnover": "-" + value.discount.toFixed(2),
                        "value": value
                    });
                });

            }
        }

        function addDiscountOrderItem(displayData, value) {
            displayData.push({
                "Quantity": "1",
                "Name": "    " + value.label,
                "Turnover": "-" + value.discount.toFixed(2),
                "value": value
            });
        }

        function addPaymentOrderItem(displayData, value) {
            displayData.push({
                "Quantity": "1",
                "Name": value.label,
                "Turnover": "-" + value.turnover.toFixed(2),
                "value": value
            });
        }

        function setViewData(areaData) {
            setSizes();
            var productOrderItems = orderService.getProductOrderItems(orderService.getCurrentOrder());
            var orderDiscountItems = orderService.getOrderDiscountItem(orderService.getCurrentOrder());
            var paymentOrderItems = orderService.getPaymentOrderItems(orderService.getCurrentOrder());
            var displayData = [];
            var index = 1;
            angular.forEach(productOrderItems, function (value) {
                if (!value.css) {
                    value.css = {};
                }
                value.css.selectedRow = areaData.data && areaData.data.css && areaData.data.css.selectedRow;
                value.index = index;
                index++;
                addOrderItem(displayData, value);
            });
            angular.forEach(orderDiscountItems, function (value) {
                if (!value.css) {
                    value.css = {};
                }
                value.css.selectedRow = areaData.data && areaData.data.css && areaData.data.css.selectedRow;
                value.index = index;
                index++;
                addDiscountOrderItem(displayData, value);
            });
            angular.forEach(paymentOrderItems, function (value) {
                if (!value.css) {
                    value.css = {};
                }
                value.css.selectedRow = areaData.data && areaData.data.css && areaData.data.css.selectedRow;

                if(value.css.evenRows == undefined){
                    value.css.rows = {};
                }

                value.css.rows["color"] = "green";

                value.index = index;
                index++;
                addPaymentOrderItem(displayData, value);
            });
            return displayData;
        }

        this.getColumnDefs = function () {
            return [{
                name: 'Quantity',
                field: 'Quantity',
                width: "20%",
                cellFilter: 'quantityFilter',
                cellClass:'font-weight-bold'
            }, {
                name: 'Name',
                field: 'Name',
                width: "50%",
                cellClass:'font-weight-bold'
            },  {
                name: 'Turnover',
                field: 'Turnover',
                width: "30%",
                cellClass:"align-right font-weight-bold",
                cellFilter:"euroFilter"
            }];
        };
        this.getData = function (areaData) {
            return setViewData(areaData);
        };

        this.click = function(elementData, row, col){
            abstractStrategyFactory.changeState("ORDER_OPENED");
            textService.setText(textService.textModes.info, row.entity.Quantity + "x " + row.entity.Name + " ausgewählt");
        }
    }];

});