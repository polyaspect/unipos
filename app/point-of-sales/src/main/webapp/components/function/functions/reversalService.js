//# sourceURL=pos.reversalService.js
define([
        'angular'
    ], function (angular) {
        return ['$rootScope', 'pos.orderService', 'uuid2', 'pos.queueService', 'pos.urlSettings', 'pos.areaService', 'pos.quantityService', 'pos.positionService', 'pos.productService' ,'pos.ui.elements.text.textDirectiveService',
            'pos.abstractStrategyFactory',
            function (rootScope, orderService, uuid2, queueService, urlSettings, areaService, quantityService, positionService, productService, textDirectiveService, abstractStrategyFactory) {
                var self = this;
                this.doAction = function (cell) {
                    var gridApi = rootScope.jkGridApi[cell.serviceData.orderDirectiveId];
                    var selectedRows = gridApi.selection.getSelectedRows();

                    if (selectedRows.length > 0) {
                        angular.forEach(selectedRows, function (item, key) {
                            self.addReversalOrderItem(item.value.orderId, item.value.orderItemId);
                            textDirectiveService.setText(textDirectiveService.textModes.info, item.Quantity + "x " + item.Name + " storniert");
                        });
                    }
                };
                this.addReversalOrderItem = function (orderId, orderItemId) {
                    if(orderService.isLocked(orderService.getCurrentOrder())){
                        swal({
                            title: "Rechnung gesperrt",
                            text: "Die aktuelle Rechnung ist gesperrt. Bitte warten Sie bis sie wieder freigegeben ist.",
                            type: "error",
                            timer: 5000
                        });
                        return;
                    }

                    var order = orderService.getOrderById(orderId);
                    // Wenn der offene Rechnungsbetrag minus dem Storno weniger als 0 wäre, kann man nicht stornieren!
                    if (orderService.getItemById(order, orderItemId) && orderService.getOpenAmountOfOrder(order) - orderService.getTurnoverOfOrderItem(order, orderItemId) < 0) {
                        swal("Storno nicht möglich!", "Wenn Sie den Storno durchführen würden, würde die Rechnungssumme unter 0 fallen", "error");
                        return;
                    } else {
                        var orderItem = orderService.getItemById(orderService.getCurrentOrder(), orderItemId);

                        if(orderItem.type == "productOrderItem"){
                            var reversalOrderItems = orderService.getOrderItemsFromType(orderService.getCurrentOrder(), "reversalOrderItem");
                            var stepOut = false;
                            angular.forEach(reversalOrderItems, function (value, i) {
                                if(value.receiverOrderItem == orderItemId)
                                    stepOut = true;
                            });
                            if(stepOut){
                                return;
                            }

                            abstractStrategyFactory.changeState("ORDER_OPENED");

                            var actionUuid = uuid2.newuuid();
                            var reversalOrderItem = {
                                "reason": "Storno",
                                "orderItem": null,
                                "type": "reversalOrderItem",
                                "id": null,
                                "orderItemId": uuid2.newuuid(),
                                "receiverOrderItem": orderItemId,
                                "orderId": orderId,
                                "position": positionService.getMaxPosition(orderService.getCurrentOrder()),
                                "clientCreationDate": new Date().toISOString(),
                                "serverReceiveTime": null,
                                "hash": null,
                                "actionId": actionUuid
                            };
                            queueService.addToQueue({
                                url: urlSettings.baseUrl + urlSettings.newReversalOrderItem,
                                method: "post",
                                data: reversalOrderItem
                            });
                            orderService.addOrderItem(reversalOrderItem);

                            var product = Enumerable.From(productService.products).FirstOrDefault(undefined, function (x) {
                                return x.productIdentifier == orderItem.productNumber;
                            });
                            if (product && product.stockAmount != undefined && product.stockAmount >= 0) {
                                queueService.addToQueue({
                                    url: urlSettings.baseUrl + urlSettings.reduceStockAmountForProductGuid,
                                    method: "post",
                                    data: {
                                        id: product.id,
                                        stockAmount: orderItem.quantity * (-1)
                                    }
                                });
                            }
                        }
                        else if(orderItem.type == "paymentOrderItem"){
                            /*abstractStrategyFactory.changeState("ORDER_OPENED");
                            queueService.addToQueue({
                                url: urlSettings.baseUrl + "/pos/paymentOrderItem/delete",
                                method: "post",
                                data: orderItem
                            });

                            var indexToRemove = -1;
                            angular.forEach(orderService.getCurrentOrder().orderItems, function (value, i) {
                                if(value.orderItemId == orderItem.orderItemId)
                                    indexToRemove = i;
                            });
                            if (indexToRemove >= 0) {
                                orderService.getCurrentOrder().orderItems.splice(indexToRemove, 1);
                            }
                            rootScope.$emit("orderService-" + "orders");*/
                        }
                    }
                }
            }];
    }
)
;