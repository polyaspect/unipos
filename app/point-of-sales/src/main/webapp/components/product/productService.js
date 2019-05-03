//# sourceURL=pos.productService.js
define([
    'angular',
], function (angular) {
    return ['pos.quantityService', 'pos.queueService', 'pos.urlSettings', 'pos.positionService', 'uuid2', 'pos.orderService', 'pos.ui.elements.grid.gridDirectiveService',
        '$http', 'pos.textModes', '$stomp', "pos.loadingService", 'pos.ui.elements.text.textDirectiveService', 'pos.abstractStrategyFactory', '$rootScope',
        function (quantityService, queueService, urlSettings, positionService, uuid2, orderService, gridDirectiveService, http, textModes, stomp, loadingService, textService,
                  abstractStrategyFactory, rootScope) {
            this.products = [];
            this.serviceName = "pos.productService";
            this.strategyData = {enterButton: {}};
            var self = this;
            this.doAction = function (cell) {
                self.addProductOrderItem(cell);
            };
            this.addProductOrderItem = function (cell) {
                if(orderService.isLocked(orderService.getCurrentOrder())){
                    swal({
                        title: "Rechnung gesperrt",
                        text: "Die aktuelle Rechnung ist gesperrt. Bitte warten Sie bis sie wieder freigegeben ist.",
                        type: "error",
                        timer: 5000
                    });
                    return;
                }

                var quantity = quantityService.getQuantity();
                var product = self.getProductById(cell.serviceData.id);

                if(abstractStrategyFactory.getState() == "PRODUCT_CUSTOM_PRICE" && self.strategyData.enterButton.productIdentifier != product.productIdentifier){
                    abstractStrategyFactory.changeState("ORDER_OPENED");
                }

                if (!product.stockAmount && product.stockAmount < 1 && product.stockAmount < quantity) {
                    sweetAlert("Oops...", "Es sind nicht genügend Artikel lagernd!", "error");
                    return;
                }
                if (product.customPriceInputAllowed && abstractStrategyFactory.getState() != "PRODUCT_CUSTOM_PRICE") {
                    abstractStrategyFactory.changeState("PRODUCT_CUSTOM_PRICE");
                    abstractStrategyFactory.createNumberInputStrategy().numberInput(textService, "price", "0,00");
                    self.strategyData.enterButton = cell;
                    self.strategyData.enterButton.productIdentifier = product.productIdentifier;
                } else {
                    var actionUuid = uuid2.newuuid();
                    if (product && !angular.equals(product, {})) {
                        var turnover = parseFloat((product.price * quantity).toFixed(2));
                        if (product.customPriceInputAllowed) {
                            if (abstractStrategyFactory.getState() == "PRODUCT_CUSTOM_PRICE") {
                                var enterdPrice = textService.getText("price");
                                if (angular.isString(enterdPrice)) {
                                    enterdPrice = parseFloat(enterdPrice.replace(',', '.'));
                                }
                                if (isNaN(enterdPrice)) {
                                    return;
                                }
                                turnover = parseFloat((enterdPrice * quantity).toFixed(2));
                                abstractStrategyFactory.createNumberInputStrategy().numberInput(textService, "price", "");
                                abstractStrategyFactory.changeState("ORDER_OPENED");
                            }
                        } else {
                            abstractStrategyFactory.createNumberInputStrategy().numberInput(textService, "price", "");
                            abstractStrategyFactory.changeState("ORDER_OPENED");

                        }
                        var productOrderItem = {
                            "label": cell.name,
                            "quantity": quantity,
                            "turnover": turnover,
                            "type": "productOrderItem",
                            "orderItemId": uuid2.newuuid(),
                            "orderId": orderService.currentOrderId,
                            "clientCreationDate": new Date().toISOString(),
                            "actionId": actionUuid,
                            "productNumber": product.productIdentifier,
                            "position": positionService.getMaxPosition(orderService.getCurrentOrder())
                        };
                        queueService.addToQueue({
                            url: urlSettings.baseUrl + urlSettings.newProductOrderItem,
                            method: "post",
                            data: productOrderItem
                        });
                        orderService.addOrderItem(productOrderItem);

                        // this might be an error
                        if (product.stockAmount >= 0) {
                            queueService.addToQueue({
                                url: urlSettings.baseUrl + urlSettings.reduceStockAmountForProductGuid,
                                method: "post",
                                data: {
                                    id: product.id,
                                    stockAmount: quantity
                                }
                            });
                        }

                        //ZURUECKSTELLEN
                        textService.setText(textService.textModes.quantity, "");
                        textService.setText(textService.textModes.info, quantity + "x " + cell.name + " boniert");

                    } else {
                        sweetAlert("Oops...", "Das Produkt wurde leider nicht gefunden!", "error");
                    }
                }
            };

            this.getProductById = function (productId) {
                return Enumerable.From(self.products).FirstOrDefault({}, function (x) {
                    return x.id == productId;
                });
            };
            this.getAllProducts = function () {
                var products = Enumerable.From(self.products).OrderBy(function (x) {
                    return x.number
                }).ToArray();
                return products;
            };
            this.setAllProducts = function (loading) {
                if (!loading) {
                    loadingService.startLoading("products", "Artikel laden...");
                }
                http.get(urlSettings.baseUrl + urlSettings.getAllProducts).then(function (response) {
                    self.products = response.data;
                    rootScope.$emit(self.serviceName + "-products");
                    if (!loading) {
                        setTimeout(function () {
                            loadingService.finishLoading("products")
                        }, 100);
                    }
                }, function (response) {
                    if (!loading) {
                        setTimeout(function () {
                            loadingService.errorLoading("products", "Fehler beim Laden der Artikel");
                        }, 100);
                    }
                });
            };
        }];
});