//# sourceURL=pos.splitOrderService.js
define([
    'angular'
], function (angular) {
    return ['pos.ui.elements.grid.gridDirectiveService', 'pos.queueService', 'pos.directiveIdsConst', 'pos.urlSettings', 'uuid2',  '$rootScope', 'pos.orderService',
        function (gridDirectiveService, queueService, directiveIdsConst, urlSettings, uuid2, rootScope, orderService) {
            this.sourceOrderItems = [];
            this.destOrderItems = [];

            this.chooseOrderItemGridFromId = "";
            this.chooseOrderItemGridToId = "";
            var self = this;

            this.getViewData = function (elementData) {
                var source = [];

                if (elementData.data.id == self.chooseOrderItemGridFromId) {
                    source = self.sourceOrderItems;
                }
                else if (elementData.data.id == self.chooseOrderItemGridToId) {
                    source = self.destOrderItems;
                }

                return source;
            };

            this.chooseOrderItem = function (elementData, orderItem) {
                var source = [];
                var dest = [];

                if (elementData.data.id == self.chooseOrderItemGridFromId) {
                    source = self.sourceOrderItems;
                    dest = self.destOrderItems;
                }
                else if (elementData.data.id == self.chooseOrderItemGridToId) {
                    source = self.destOrderItems;
                    dest = self.sourceOrderItems;
                }

                for (var i = 0; i < source.length; i++) {
                    if (source[i].id === orderItem.value.id) {
                        source.splice(i, 1);
                        break;
                    }
                }
                dest.push(orderItem.value);
                rootScope.$emit("chooseOrderItemGridService-orderItem");
            };

            this.confirm = function(){
                var order = orderService.createNewOrder({});
                order.orderItems = self.destOrderItems;

                queueService.addToQueue({
                    url: urlSettings.baseUrl + "/pos/orders/splitOrder",
                    method: "post",
                    data: {
                        sourceOrderId: orderService.getCurrentOrder().orderId,
                        destOrderId: order.orderId,
                        orderItemIds: Enumerable.From(this.destOrderItems).Select("$.orderItemId").ToArray()
                    }
                });
            };

        }];
});