//# sourceURL=pos.paymentService.js
define([
    'angular',
], function (angular) {
    return ['pos.quantityService', 'pos.areaNames', 'pos.directiveIdsConst', 'pos.orderService', 'uuid2', '$http', 'pos.urlSettings', 'pos.queueService',
        '$timeout', 'pos.positionService', 'pos.areaService', 'pos.ui.elements.text.textDirectiveService', 'pos.invoiceService', 'pos.abstractStrategyFactory', '$rootScope','$q', 'pos.cashbookEntryService',
        function (quantityService, areaNames, directiveIdsConst, orderService, uuid2, http, urlSettings, queueService, timeout, positionService, areaService, textService, invoiceService, abstractStrategyFactory, rootScope, q, cashbookEntryService) {
            this.paymentMethods = [];
            var self = this;

            this.doAddPaymentOrderItem = function(cell, price){
                if (orderService.getProductOrderItems(orderService.getCurrentOrder()).length > 0) {
                    var actionId = uuid2.newuuid(); //TODO: Die ActionId muss noch aus einer eigenen Service kommen
                    var paymentItem = {
                        "paymentMethodGuid": cell.serviceData.paymentId,
                        "label": cell.name,
                        "turnover": price,
                        "type": "paymentOrderItem",
                        "id": null,
                        "orderItemId": uuid2.newguid(),
                        "orderId": orderService.currentOrderId,
                        "position": positionService.getMaxPosition(orderService.getCurrentOrder()),
                        "clientCreationDate": new Date().toISOString(),
                        "serverReceiveTime": null,
                        "hash": null,
                        "actionId": actionId
                    };
                    var openAmountBeforePayment = orderService.getOpenAmountOfOrder(orderService.getCurrentOrder());

                    orderService.addOrderItem(paymentItem);
                    queueService.addToQueue({
                        url: urlSettings.baseUrl + urlSettings.newPaymentOrderItem,
                        method: "post",
                        data: paymentItem
                    });
                    var openAmount = orderService.getOpenAmountOfOrder(orderService.getCurrentOrder());
                    if (openAmount < 0 || openAmount == 0) {
                        self.addChangeOrderItem(cell, price - openAmountBeforePayment);
                    } else {
                        quantityService.setSum(orderService.getOpenAmountOfOrder(orderService.getCurrentOrder()), self.hasPaymentItems());
                    }
                }
                else {
                    swal("Keine Rechnungspositionen", "Bitte fügen Sie zumindest eine Position zur Rechnung hinzu, bevor Sie diese abschließen!", "info");
                }
            };

            this.addPaymentOrderItem = function (cell) {
                if(orderService.isLocked(orderService.getCurrentOrder())){
                    swal({
                        title: "Rechnung gesperrt",
                        text: "Die aktuelle Rechnung ist gesperrt. Bitte warten Sie bis sie wieder freigegeben ist.",
                        type: "error",
                        timer: 5000
                    });
                    return;
                }
                if (!self.hasChangeItems()) {
                    var price = quantityService.getPrice();
                    textService.setText(textService.textModes.price, "");
                    var openAmount = parseFloat(orderService.getOpenAmountOfOrder(orderService.getCurrentOrder()));

                    if (price <= 0) {
                        price = openAmount;
                    }

                    // PREVENT UNREALISTIC INPUTS
                    if(price >= 100000){
                        swal({
                            title: "Rechnungsbetrag zu hoch!",
                            text: "Sie können keine Rechnungen mit einem Rechnungsbetrag über € 100.000 erstellen!",
                            type: "error"
                        });
                        textService.setText(textService.textModes.quantity, "");
                        textService.setText(textService.textModes.price, "");
                    }
                    else if(price >= 1000){
                        swal({
                            title: "Hoher Zahlungsbetrag!",
                            text: "Möchten Sie diese Rechnung mit dem Zahlungsbetrag €" + price.toFixed(2) + " abschliessen?",
                            type: "warning",
                            cancelButtonText: 'Abbrechen',
                            confirmButtonText: 'Abschliessen',
                            showCancelButton: true
                        }, function(){
                            self.doAddPaymentOrderItem(cell, price);

                        });
                    }
                    else{
                        self.doAddPaymentOrderItem(cell, price);
                    }
                }
                else {
                    this.createInvoice();
                }
            };

            this.addChangeOrderItem = function (cell, change) {
                abstractStrategyFactory.changeState("INVOICE_CREATED");
                var actionId = uuid2.newuuid();
                var changeOrderItem = {
                    "label": "BAR",
                    "paymentMethod": cell.id,
                    "turnover": parseFloat(change).toFixed(2),
                    "type": "changeOrderItem",
                    "id": null,
                    "orderItemId": uuid2.newguid(),
                    "orderId": orderService.currentOrderId,
                    "position": positionService.getMaxPosition(orderService.getCurrentOrder()),
                    "clientCreationDate": new Date().toISOString(),
                    "serverReceiveTime": null,
                    "hash": null,
                    "actionId": actionId
                };
                orderService.addOrderItem(changeOrderItem);
                queueService.addToQueue({
                    url: urlSettings.baseUrl + urlSettings.newChangeOrderItem,
                    method: "post",
                    data: changeOrderItem,
                    callback: function (data) {
                        self.createInvoice()
                    }
                });
                textService.setText(textService.textModes.quantity, "");
                textService.setText(textService.textModes.price, "");
                quantityService.setSum(change.toFixed(2), self.hasPaymentItems(), self.hasChangeItems(), orderService.getTotalAmountOfPaymentOrderItems(orderService.getCurrentOrder()));
                //this.createInvoice();
            };
            this.createInvoice = function () {
                orderService.lockOrder(orderService.getCurrentOrder());
                queueService.addToQueue({
                    url: urlSettings.baseUrl + urlSettings.createFromOrder,
                    method: "post",
                    data: {
                        clientOrderId: orderService.currentOrderId,
                        cashierId:  rootScope.pos.user.id
                    },
                    callback: function (data) {
                        /*if(data.qrCode.indexOf("uYKu+RCUq0g=_U2ljaGVyaGVpdHNlaW5yaWNodHVuZyBhdXNnZWZhbGxlbg==") > 0){
                            swal({
                                title: "Fehler bei Signaturerstellung",
                                text: "Bei der Erstellung der Signatur für diesen Beleg ist ein Fehler aufgetreten. Sollte dieser Fehler weiterhin auftreten, starten Sie den Server neu.",
                                type: "warning"
                            });
                        }*/
                        invoiceService.syncInvoicesFromServer();
                        cashbookEntryService.getAllCashbookEntries();
                        orderService.openNewOrder({});
                    },
                    errorCallback: function(response){
                        orderService.unlockOrder(orderService.getCurrentOrder());
                        if(response.status == 423){
                            swal({
                                title: "Tagesabschluss bereits durchgeführt",
                                text: "Sie haben den Tagesabschluss bereits durchgeführt. Neue Rechnungen können erst ab dem nächsten Tag erstellt werden.",
                                type: "error",
                                timer: 5000
                            });
                            orderService.openNewOrder({});
                        }
                        if(response.status == 400){
                            if(response.data == "AUTH_TOKEN_INVALID"){
                                window.location.reload();
                            }

                            if(response.data == "SAMMELBELEG_REQUIRED"){
                                swal({
                                    title: "Zuerst Sammelbeleg erstellen",
                                    text: "Ihre Signaturerstellungseinrichtung ist ausgefallen und während des Ausfalls wurden Rechnungen erstellt. Bevor Sie weitere Rechnungen erstellen können, müssen Sie einen Sammelbeleg erstellen!",
                                    type: "warning",
                                    cancelButtonText: 'Abbrechen',
                                    confirmButtonText: 'Jetzt erstellen',
                                    showCancelButton: true
                                }, function(){
                                    queueService.addToQueue({
                                        url: urlSettings.baseUrl + "/signature/signatures/createNullInvoice",
                                        method: "post",
                                        data: "invoiceType=SAMMEL",
                                        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                                        callback: function (data) {
                                            self.createInvoice();
                                        },
                                        errorCallback: function(data) {
                                            setTimeout(
                                            function(){swal({
                                                title: "Signatur-Lesegerät abgesteckt",
                                                text: "Es konnte kein Sammelbeleg erstellt werden. Bitte entfernen Sie das Signaturkarten-Lesegerät und verbinden Sie es erneut!",
                                                type: "error"
                                            })}, 1000);
                                        }
                                    });
                                });
                            }
                        }
                        if(response.status == 500){
                            swal({
                                title: "Rechnungs-Erstellung fehlgeschlagen",
                                text: "Die Rechnung konnte aufgrund eines technischen Fehlers nicht erstellt werden. Bitte geben Sie die Rechnung erneut ein.",
                                type: "error",
                                timer: 5000
                            });
                            orderService.openNewOrder({});
                        }
                    }
                });
                areaService.toScreen(areaService.areaNames[0]);
            };
            this.hasPaymentItems = function () {
                return orderService.getPaymentOrderItems(orderService.getCurrentOrder()).length > 0;
            };
            this.hasChangeItems = function () {
                return orderService.getChangeOrderItems(orderService.getCurrentOrder()).length > 0;
            };
            this.getAllPaymentMethods = function () {
                return http.get(urlSettings.baseUrl + urlSettings.getAllPaymentMethods).then(function (response) {
                    self.paymentMethods = response.data;
                });
            };
            this.getPaymentMethods = function () {
                var defer = q.defer();
                if (!self.paymentMethods) {
                    self.getAllPaymentMethods().then(function () {
                        defer.resolve(self.paymentMethods);
                    });
                } else {
                    defer.resolve(self.paymentMethods);
                }
                return defer.promise;
            };
            self.getAllPaymentMethods();
        }];
});