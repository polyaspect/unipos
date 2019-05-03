//# sourceURL=pos.discountService.js
define([
    'angular'
], function (angular) {
    return ['$rootScope', 'pos.orderService', 'pos.directiveIdsConst', 'uuid2', 'pos.queueService', 'pos.urlSettings', 'pos.areaService', 'pos.positionService',
        'pos.abstractStrategyFactory', 'pos.quantityService', 'pos.ui.elements.text.textDirectiveService', '$http', '$q',
        function (rootScope, orderService, directiveIdsConst, uuid2, queueService, urlSettings, areaService, positionService, abstractStrategyFactory, quantityService, textDirectiveService, http, q) {
            var discounts = [];
            this.discountCell = undefined;
            var self = this;
            this.doAction = function (cell) {
                if (!cell.serviceData.discountIsFixed) {
                    if (cell.serviceData.discountValueType == "PERCENT") {
                        if (abstractStrategyFactory.getState() != "ENTER_DISCOUNT_PERCENT") {
                            abstractStrategyFactory.changeState("ENTER_DISCOUNT_PERCENT");
                            abstractStrategyFactory.createNumberInputStrategy().numberInput(textDirectiveService, "price", "Bitte geben Sie den Rabatt-Prozentbetrag ein:");
                            self.discountCell = cell;
                            return;
                        }
                    }
                    if (cell.serviceData.discountValueType == "VALUE") {
                        if (abstractStrategyFactory.getState() != "ENTER_DISCOUNT_AMOUNT") {
                            abstractStrategyFactory.changeState("ENTER_DISCOUNT_AMOUNT");
                            abstractStrategyFactory.createNumberInputStrategy().numberInput(textDirectiveService, "price", "Bitte geben Sie den Rabattbetrag ein:");
                            self.discountCell = cell;
                            return;
                        }
                    }
                    return;
                } else {
                    self.addDiscountItem(cell);
                }
            };
            this.addDiscountItem = function (cell) {
                if(orderService.isLocked(orderService.getCurrentOrder())){
                    swal({
                        title: "Rechnung gesperrt",
                        text: "Die aktuelle Rechnung ist gesperrt. Bitte warten Sie bis sie wieder freigegeben ist.",
                        type: "error",
                        timer: 5000
                    });
                    return;
                }
                if (cell.serviceData.discountType == "ORDER") {
                    self.addOrderDiscountItem(cell);
                } else if (cell.serviceData.discountType == "PRODUCT") {
                    var selectedRows = rootScope.jkGridApi[cell.serviceData.orderDirectiveId].selection.getSelectedRows();
                    if (selectedRows.length > 0) {
                        angular.forEach(selectedRows, function (value, key) {
                            self.addOrderItemDiscount(cell, value);
                        });
                    }
                }
            };
            this.addOrderDiscountItem = function (cell) {
                var discountItem = cell.serviceData;
                var actionId = uuid2.newuuid();
                self.calcOrderDiscount(discountItem, undefined).then(function (discount) {
                    if (discount > 0) {
                        var orderDiscountItem = {
                            "receiverOrderId": orderService.currentOrderId,
                            "type": "orderDiscountOrderItem",
                            "discount": discount,
                            "discountId": discountItem.discountId,
                            "id": null,
                            "label": cell.name,
                            "orderItemId": uuid2.newuuid(),
                            "orderId": orderService.currentOrderId,
                            "position": positionService.getMaxPosition(orderService.getCurrentOrder()),
                            "clientCreationDate": new Date().toISOString(),
                            "serverReceiveTime": null,
                            "hash": null,
                            "actionId": actionId
                        };
                        queueService.addToQueue({
                            url: urlSettings.baseUrl + urlSettings.newOrderDiscountOrderItem,
                            method: "post",
                            data: orderDiscountItem
                        });
                        orderService.addOrderItem(orderDiscountItem);
                    }
                });
            };
            this.calcOrderDiscount = function (discountItem, orderItem) {
                var defer = q.defer();
                var amount;
                if (orderItem) {
                    amount = orderService.getTurnoverOfOrderItem(orderService.getCurrentOrder(), orderItem.value.orderItemId);
                    var openAmount = orderService.getOpenAmountOfOrder(orderService.getCurrentOrder());
                    if (amount > openAmount) {
                        amount = openAmount;
                    }
                } else {
                    amount = orderService.getOpenAmountOfOrder(orderService.getCurrentOrder());
                }
                if (angular.isString(amount)) {
                    amount = parseFloat(amount);
                }
                var discountAmount = 0;
                if (!discountItem.discountIsFixed) {
                    discountAmount = quantityService.getPrice();
                } else {
                    discountAmount = discountItem.discountValue;
                }
                if (discountItem.discountValueType == "PERCENT") {
                    var turnover = parseFloat((discountAmount / 100 * amount).toFixed(2))
                    defer.resolve(turnover);
                    return defer.promise;
                } else {
                    if (discountAmount > amount) {
                        if (amount == 0) {
                            swal({
                                title: "Die offene Betrag ist 0",
                                text: "Wenn Sie diesen Rabatt eingeben wird die Summe unter 0 sein",
                                type: "warning",
                                showCancelButton: false,
                                confirmButtonText: "Ok",
                                closeOnConfirm: true
                            }, function () {
                                return defer.resolve(amount);
                            });
                            return defer.promise;
                        } else {
                            swal({
                                title: "Der Rabattbetrag ist zu hoch!",
                                text: "Wenn Sie diesen Rabatt eingeben wird die Summe unter 0 sein",
                                type: "warning",
                                showCancelButton: true,
                                confirmButtonText: "Ja, ändere den Rabattbetrag auf Maximun",
                                closeOnConfirm: true
                            }, function () {
                                return defer.resolve(amount);
                            });
                            return defer.promise;
                        }
                    } else {
                        defer.resolve(discountAmount);
                        return defer.promise;
                    }
                }
            };
            this.addOrderItemDiscount = function (cell, orderItem) {
                var discountItem = cell.serviceData;
                var actionId = uuid2.newuuid();
                self.calcOrderDiscount(discountItem, orderItem).then(function (discount) {
                    if (discount > 0) {
                        var orderDiscountItem = {
                            "receiverOrderItemId": orderItem.value.orderItemId,
                            "type": "orderItemDiscountOrderItem",
                            "discount": discount,
                            "discountId": discountItem.discountId,
                            "id": null,
                            "label": cell.name,
                            "orderItemId": uuid2.newuuid(),
                            "orderId": orderService.currentOrderId,
                            "position": positionService.getMaxPosition(orderService.getCurrentOrder()),
                            "clientCreationDate": new Date().toISOString(),
                            "serverReceiveTime": null,
                            "hash": null,
                            "actionId": actionId
                        };
                        queueService.addToQueue({
                            url: urlSettings.baseUrl + urlSettings.newOrderItemDiscountOrderItem,
                            method: "post",
                            data: orderDiscountItem
                        });
                        orderService.addOrderItem(orderDiscountItem);
                    }
                });
            };
            this.getAllDiscounts = function () {
                http.get(urlSettings.baseUrl + urlSettings.getAllOrderDiscounts).then(function (response) {
                    self.discounts = response.data;
                });
            };
            this.getDiscounts = function () {
                return self.discounts;
            };
            self.getAllDiscounts();
        }];
});