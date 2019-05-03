//# sourceURL=pos.orderService.js
define([
    'angular'
], function (angular) {
    return ['pos.ui.elements.grid.gridDirectiveService', 'pos.ui.elements.text.textDirectiveService', 'pos.queueService', 'pos.directiveIdsConst', 'pos.urlSettings', 'uuid2', '$http', '$rootScope', 'pos.abstractStrategyFactory',
        function (gridDirectiveService, textService, queueService, directiveIdsConst, urlSettings, uuid2, http, rootScope, abstractStrategyFactory) {
            this.serviceName = "orderService";
            this.orders = [];
            this.currentOrderId = -1;
            var self = this;

            this.createNewOrder = function (baseOrder) {
                var order = baseOrder;
                var orderId = uuid2.newuuid();

                if(order.orderItems == undefined)
                    order.orderItems = [];

                if(order.orderTags == undefined)
                    order.orderTags = [];

                order.orderId = orderId;
                order.isActive = true;
                self.orders.push(order);
                rootScope.$emit(self.serviceName + "-" + "orders");
                return order;
            };

            this.sendOrderToServer = function(order){
                queueService.addToQueue({
                    url: urlSettings.baseUrl + urlSettings.createNewOrder,
                    method: "post",
                    data: order
                });
            };

            this.syncOrdersFromServer = function (callback) {
                http.get(urlSettings.baseUrl + urlSettings.getAllOrders).then(function (response) {
                    self.setOrdersData(response.data);
                    callback();
                });
            };

            this.openNewOrder = function(baseOrder){
                var order = self.createNewOrder(baseOrder);
                abstractStrategyFactory.changeState("ORDER_OPENED");
                self.sendOrderToServer(order);
                self.openOrder(order);
            };

            this.openOrder = function(order){
                self.currentOrderId = order.orderId;
                rootScope.$emit(self.serviceName + "-" + "orders");
                abstractStrategyFactory.changeState("ORDER_OPENED");
            };

            this.findOrdersByTag = function(tagKey, tagValue){
                return Enumerable.From(self.orders).Where(function (order) {
                    var orderTag = Enumerable.From(order.orderTags).FirstOrDefault(undefined, function(orderTag){
                        return orderTag.key = tagKey;
                    });

                    if(orderTag != undefined){
                        return orderTag.value == tagValue;
                    }
                    else return true;
                }).ToArray();
            };

            this.getOrderById = function(orderId){
                return Enumerable.From(self.orders).FirstOrDefault(undefined, function (order) {
                    return order.orderId == orderId;
                });
            };

            this.getCurrentOrder = function () {
                return Enumerable.From(self.orders).FirstOrDefault(undefined, function (x) {
                    return x.orderId == self.currentOrderId
                });
            };
            this.setOrdersData = function (ordersData) {
                self.orders = ordersData;

                var firstActiveOrder = Enumerable.From(self.orders).FirstOrDefault(function (x) {
                    return x.active;
                });

                if(firstActiveOrder != undefined){
                    self.openOrder(firstActiveOrder);
                }

                rootScope.$emit(self.serviceName + "-" + "orders");
            };
            this.addOrderItem = function (orderItem) {
                self.getCurrentOrder().orderItems.push(orderItem);
                rootScope.$emit(self.serviceName + "-" + "orders");
            };
            //endregion

            //region GetOrderItems
            this.getOrderItemsFromType = function (order, type) {
                return Enumerable.From(order.orderItems).Where(function (x) {
                    return x.type == type;
                }).ToArray();
            };
            this.getProductOrderItems = function (order) {
                var productOrderItems = self.getOrderItemsFromType(order, "productOrderItem");
                var reversalOrderItems = self.getOrderItemsFromType(order, "reversalOrderItem");
                angular.forEach(reversalOrderItems, function (value, i) {
                    var indexToRemove = -1;
                    angular.forEach(productOrderItems, function (item, index) {
                        if (value.receiverOrderItem == item.orderItemId) {
                            indexToRemove = index;
                        }
                    });
                    if (indexToRemove >= 0) {
                        productOrderItems.splice(indexToRemove, 1);
                    }
                });
                return productOrderItems;
            };

            this.deleteOrderItem = function (orderItemId) {
                queueService.addToQueue({
                    url: urlSettings.baseUrl + urlSettings.deleteOrderItem + "/" + orderItemId,
                    method: "delete"
                });
            };

            this.getPaymentOrderItems = function (order) {
                return self.getOrderItemsFromType(order, "paymentOrderItem");
            };
            this.getChangeOrderItems = function (order) {
                return self.getOrderItemsFromType(order, "changeOrderItem");
            };
            this.getOrderDiscountItem = function (order) {
                var orderDiscountItems = self.getOrderItemsFromType(order, "orderDiscountOrderItem");
                var reversalOrderItems = self.getOrderItemsFromType(order, "reversalOrderItem");
                angular.forEach(reversalOrderItems, function (value, i) {
                    var indexToRemove = -1;
                    angular.forEach(orderDiscountItems, function (item, index) {
                        if (value.receiverOrderItem == item.orderItemId) {
                            indexToRemove = index;
                        }
                    });
                    if (indexToRemove >= 0) {
                        orderDiscountItems.splice(indexToRemove, 1);
                    }
                });
                return orderDiscountItems;
            };
            this.getOrderItemDiscountItem = function (order) {
                var orderItemDiscountsItems = self.getOrderItemsFromType(order, "orderItemDiscountOrderItem");
                var reversalOrderItems = self.getOrderItemsFromType(order, "reversalOrderItem");
                angular.forEach(reversalOrderItems, function (value, i) {
                    var indexToRemove = -1;
                    angular.forEach(orderItemDiscountsItems, function (item, index) {
                        if (value.receiverOrderItem == item.orderItemId) {
                            indexToRemove = index;
                        }
                    });
                    if (indexToRemove >= 0) {
                        orderItemDiscountsItems.splice(indexToRemove, 1);
                    }
                });
                return orderItemDiscountsItems;
            };
            //endregion

            //region GetOrderItemsById
            this.getItemById = function (order, orderItemId) {
                return Enumerable.From(order.orderItems).FirstOrDefault(undefined, function (x) {
                    return x.orderItemId == orderItemId;
                });
            };
            this.getProductItemById = function (order, productOrderItemId) {
                return Enumerable.From(self.getProductOrderItems(order)).FirstOrDefault(undefined, function (x) {
                    return x.orderItemId == productOrderItemId;
                });
            };
            //endregion

            //region GetTotal
            this.getTotalAmountOfOrder = function (order) {
                var orderItemDiscountOrderItems = Enumerable.From(self.getOrderItemDiscountItem(order));
                var f = Enumerable.From(self.getProductOrderItems(order)).Sum(function (x) {
                    var orderItemDiscount = orderItemDiscountOrderItems.Where(function (y) {
                        return y.receiverOrderItemId == x.orderItemId;
                    }).ToArray();
                    var price = x.turnover;
                    angular.forEach(orderItemDiscount, function (value, index) {
                        price -= value.discount;
                    });
                    return price;
                }).toFixed(2);
                return f;
            };


            this.getOpenAmountOfOrder = function (order) {
                var total = self.getTotalAmountOfOrder(order);
                var paymentSum = self.getTotalAmountOfPaymentOrderItems(order);
                var orderDiscountSum = self.getTotalAmountOfOrderDiscountItem(order);
                return (total - orderDiscountSum - paymentSum).toFixed(2);
            };
            this.getTotalAmountOfChangeOrder = function (order) {
                return Enumerable.From(self.getChangeOrderItems(order)).Sum(function (x) {
                    return x.turnover;
                }).toFixed(2);
            };
            this.getTotalAmountOfPaymentOrderItems = function (order) {
                return Enumerable.From(self.getPaymentOrderItems(order)).Sum(function (x) {
                    return x.turnover;
                }).toFixed(2);
            };
            this.getTotalAmountOfOrderDiscountItem = function (order) {
                return Enumerable.From(self.getOrderDiscountItem(order)).Sum(function (x) {
                    return x.discount;
                }).toFixed(2);
            };
            this.getTotalAmountOfOrderItemDiscountItem = function (order) {
                return Enumerable.From(self.getOrderItemDiscountItem(order)).Sum(function (x) {
                    return x.turnover;
                }).toFixed(2);
            };

            this.getTurnoverOfOrderItem = function (order, orderItemId) {
                var product = Enumerable.From(self.getProductOrderItems(order)).FirstOrDefault(undefined, function (x) {
                    return x.orderItemId == orderItemId;
                });
                var orderItemDiscount = Enumerable.From(self.getOrderItemDiscountItem(order)).Where(function (x) {
                    return x.receiverOrderItemId == orderItemId;
                }).ToArray();
                if (product) {
                    var price = product.turnover;
                    angular.forEach(orderItemDiscount, function (value, index) {
                        price -= value.discount;
                    });
                    return price;
                } else {
                    return 0;
                }
            };

            this.addOrderToCache = function(order){
                self.orders.push(order);
            };

            this.lockOrder = function(order){
                order.locked = true;
            };

            this.unlockOrder = function(order){
                order.locked = false;
            };

            this.isLocked = function(order){
                if(order.locked == undefined)
                    return false;
                else
                    return order.locked;
            };

            //endregion
        }];
});