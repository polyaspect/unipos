//# sourceURL=pos.posController.js
define([
    'angular',
    'pos.angularResource',
    'css!pos.posCss'
], function (angular) {
    return ['$scope', 'pos.orderService', 'pos.areaService', '$rootScope', 'pos.directiveIdsConst', 'pos.areaNames', 'pos.quantityService', '$stomp', 'pos.dataService',
        '$routeParams', 'pos.deviceService', '$location', '$cookies', 'pos.initOptions', 'pos.productService', '$http', 'pos.urlSettings', 'pos.socketService',
        'pos.abstractStrategyFactory', 'pos.ui.elements.text.textDirectiveService', '$q', 'pos.cashbookEntryService', 'pos.productSocketHandler', 'pos.loginService', 'pos.categoryService', 'pos.addNewDeviceService',
        'pos.invoiceService', 'pos.ui.screenService', 'pos.services.storeService',
        function (scope, orderService, areaService, rootScope, directiveIdsConst, areaNames, quantityService, stomp, dataService, routeParams, deviceService, location,
                  cookies, initOptions, productService, http, urlSettings, socketService, abstractStrategyFactory, textService, q, cashbookEntryService, productSocketHandler,
                  loginService, categoryService, addNewDeviceService, invoiceService, screenService, storeService) {
            if(rootScope.showNav == true){
                rootScope.showNav = false;
                return;
            }
            q.all([http.get("/auth/auth/checkAuthToken"), http.get("/socket/checkDeviceToken"), http.get("/socket/device/hasDefaultPrinter")]).then(function (results) {
                if (!cookies.get("AuthToken")) {
                    location.path(initOptions.baseUrl + "/login");
                } else if (!cookies.get("DeviceToken")) {
                    location.path(initOptions.baseUrl + "/addNewDevice");
                } else if (!results[2].data) {
                    location.path(initOptions.baseUrl + "/addPrinterToDevice");
                } else {
                    http.get("/socket/isStoreWithTokenAvailable").then(function (response) {
                        if (response.data != true) {
                            location.path(initOptions.baseUrl + "/addNewDevice");
                        }
                        else {
                            loginService.setInitValues().then(function () {
                                addNewDeviceService.setCashier().then(function () {
                                    orderService.syncOrdersFromServer(function () {
                                        var orderToOpen;
                                        angular.forEach(orderService.orders, function(order){
                                            if(order.currentDevice.deviceId == cookies.get("DeviceToken")){
                                                orderToOpen = order;
                                            }
                                        });
                                        if(orderToOpen != undefined){
                                            orderService.openOrder(orderToOpen);
                                        }else{
                                             orderService.openNewOrder({});
                                        }
                                    });

                                    http.get("/data/stores/getByUserAndDevice").then(function(response){
                                        storeService.currentStore = response.data;
                                    });

                                    invoiceService.syncInvoicesFromServer();

                                    cashbookEntryService.getAllCashbookEntries();
                                    productService.setAllProducts();
                                    categoryService.setAllCategories();
                                    abstractStrategyFactory.changeState(abstractStrategyFactory.states.orderOpened);

                                    socketService.subscribeDevice("updateScreenSets", function (data) {
                                        screenService.updateScreenCollection(data, scope);
                                        http.get("/auth/auth/getUserByAuthToken/" + cookies.get("AuthToken")).then(function (response) {
                                            textService.setText("loggedInUser", "Angemeldeter Benutzer: " + response.data.name + " " + response.data.surname);
                                        });
                                    });

                                    socketService.subscribeBroadcast("updateScreenSets", function (data) {
                                        screenService.updateScreenCollection(data, scope);
                                    });

                                    socketService.subscribeDevice("info", function (data) {
                                        swal({
                                            title: 'Information',
                                            type: 'info',
                                            text: data
                                        });
                                    });

                                    socketService.subscribeDevice("warning", function (data) {
                                        swal({
                                            title: 'Warnung',
                                            type: 'warning',
                                            text: data
                                        });
                                    });

                                    socketService.subscribeDevice("error", function (data) {
                                        swal({
                                            title: 'Fehler',
                                            type: 'error',
                                            text: data
                                        });
                                    });

                                    socketService.subscribeBroadcast("smartCardInserted", function (data) {
                                        var insertedSerialsNo = JSON.parse(data.payload);
                                        swal({
                                            title: 'Smart-Card in Lesegerät erkannt',
                                            type: 'info',
                                            text: insertedSerialsNo.join("\n")
                                        });
                                    });

                                    socketService.subscribeBroadcast("smartCardRemoved", function (data) {
                                        var removedSmartCards = JSON.parse(data.payload);
                                        swal({
                                            title: 'Smart-Card aus Lesegerät entfernt',
                                            type: 'warning',
                                            text: removedSmartCards.join("\n")
                                        });
                                    });

                                    socketService.subscribeBroadcast("smartCardDetached", function (data) {
                                        var smartCardDetached = JSON.parse(data.payload);
                                        swal({
                                            title: 'Smart-Card Lesegerät getrennt',
                                            type: 'warning',
                                            text: smartCardDetached.join("\n")
                                        });
                                    });

                                    socketService.subscribeBroadcast("reload", function (data) {
                                        window.location.reload();
                                    });

                                    socketService.connect()
                                });
                            });
                        }
                    });
                }
            });
            history.pushState(null, document.title, location.href);
            window.addEventListener('popstate', function (event)
            {
                history.pushState(null, document.title, location.href);
            });
        }
    ];
});

function noScreensFound() {
    swal("Keine Oberfläche zur Bonierung gefunden", "Es wurde keine Oberfläche für dieses Design mit dem Typ 'Bonierung' gefunden. Bitte legen Sie diese im Designer an!", "error");
}