//# sourceURL=pos.chooseOrderGridService.js
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

        this.getColumnDefs = function () {
            return [{
                name: 'Umsatz',
                field: 'turnover',
                width: "20%"
            }];
        };

        this.getData = function (elementData) {
            //setSizes();

            var orders = abstractStrategyFactory.chooseOrderStrategy().getViewData(injector, elementData);
            var displayData = [];
            var index = 1;
            angular.forEach(orders, function (order, index) {
                var value = order;
                if (!value.css) {
                    value.css = {};
                }
                value.css.evenRows = elementData.data && elementData.data.css && elementData.data.css.evenRows;
                value.css.oddRows = elementData.data && elementData.data.css && elementData.data.css.oddRows;
                value.css.selectedRow = elementData.data && elementData.data.css && elementData.data.css.selectedRow;
                value.index = index;
                index++;

                displayData.push({
                    "turnover": parseFloat(orderService.getOpenAmountOfOrder(order).toString()).toFixed(2),
                    "value": order
                });
            });
            return displayData;
        };

        this.click = function(elementData, row, col){
            abstractStrategyFactory.chooseOrderStrategy().chooseOrder(injector, elementData, row.entity.value);
        }
    }];

});