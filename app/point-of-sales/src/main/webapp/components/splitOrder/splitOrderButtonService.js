//# sourceURL=pos.splitOrderButtonService.js
define([
    'angular'
], function (angular) {
    return ['pos.areaService', '$rootScope', 'pos.splitOrderService', 'pos.abstractStrategyFactory', 'pos.orderService',
        function (screenService, rootScope, splitOrderService, abstractStrategyFactory, orderService) {

            this.doAction = function(element){
                var elementData = element.serviceData;
                splitOrderService.chooseOrderItemGridFromId = elementData.chooseOrderItemGridFrom;
                splitOrderService.chooseOrderItemGridToId = elementData.chooseOrderItemGridTo;

                splitOrderService.sourceOrderItems = orderService.getCurrentOrder().orderItems;
                screenService.toScreen(elementData.screenName);
                abstractStrategyFactory.changeState("SPLIT_ORDER");
                rootScope.$emit("chooseOrderItemGridService-orderItem");
            }

        }];
});