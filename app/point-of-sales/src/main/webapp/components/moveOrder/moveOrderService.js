//# sourceURL=pos.moveOrderService.js
define([
    'angular'
], function (angular) {
    return ['pos.orderService', 'pos.queueService', 'pos.urlSettings',
        function (orderService, queueService, urlSettings) {
            this.openOrder = function(elementData, tagKey, tagValue){
                var containsKey = false;
                var order = orderService.getCurrentOrder();
                angular.forEach(order.orderTags, function(orderTag){
                   if(orderTag.key == tagKey){
                       containsKey = true;
                       orderTag.value = tagValue;
                   }
                });

                if(!containsKey){
                    if(order.orderTags == undefined){
                        order.orderTags = [];
                    }
                    order.orderTags.push({key: tagKey, value: tagValue});
                }

                queueService.addToQueue({
                    url: urlSettings.baseUrl + "/pos/orders/moveOrder",
                    method: "post",
                    data: {
                        sourceOrderId: orderService.getCurrentOrder().orderId,
                        sourceTagKey: tagKey,
                        destTagValue: tagValue
                    }
                });

                orderService.openOrder(order);
            }
        }];
});