//# sourceURL=pos.openOrderService.js
define([
    'angular'
], function (angular) {
    return ['pos.orderService','pos.abstractStrategyFactory', 'pos.areaService', '$rootScope',
        function (orderService, abstractStrategyFactory, areaService, rootScope) {
            var self = this;
            this.tagKey = "";
            this.tagValue = "";

            this.openOrder = function(elementData, tagKey, tagValue){
                var orders = orderService.findOrdersByTag(tagKey, tagValue);

                var orderTags = [];
                if(tagKey != undefined && tagKey != ""){
                    orderTags.push({key: tagKey, value: tagValue});
                }

                self.tagKey = tagKey;
                self.tagValue = tagValue;

                if(orders.length == 1){
                    orderService.openOrder(orders[0]);
                }
                else if(orders.length == 0){
                    var newOrder = {orderTags:orderTags};
                    orderService.openNewOrder(newOrder);
                }
                else{
                    abstractStrategyFactory.changeState("OPEN_ORDER");
                    areaService.toScreen(elementData.serviceData.screenName);
                    rootScope.$emit("chooseOrderGridService-order");
                }
            };

            this.chooseOrder = function(elementData, order){
                areaService.toScreen(areaService.areaNames[0]);
                orderService.openOrder(order);
            };

            this.getViewData = function(elementData){
                return orderService.findOrdersByTag(self.tagKey, self.tagValue);
            };
        }];
});