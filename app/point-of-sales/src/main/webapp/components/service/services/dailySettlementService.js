//# sourceURL=pos.services.dailySettlementService.js
define([
    'angular'
], function (angular) {
    return [
        '$http',

        'pos.ui.dataSourceService',
        'pos.invoiceService',
        'pos.services.storeService',
        'pos.socketService',
        '$rootScope',
        function ($http,
                  dataSourceService,
                  invoiceService,
                  storeService,
                  socketService,
                  rootScope) {

            this.dailySettlementGuid = undefined;
            var self = this;

            this.loadDailySettlement = function (callback) {
                $http.get("/pos/dailySettlements/isCreationAllowed").then(function (response) {
                    if (response.data == true) {
                        $http.post("/report/dailySettlementReports/previewFinancialReport").then(function (response) {
                            dataSourceService.setByElementTypeAndRole("reportPreviewGrid", "dailySettlement", response.data);
                        });
                        $http.post("/pos/dailySettlements").then(function (response) {
                            self.dailySettlementGuid = response.data;
                        });
                        callback();
                    } else {
                        swal({
                            title: "Tagesabschluss bereits durchgeführt",
                            text: "Sie haben den Tagesabschluss bereits durchgeführt.",
                            type: "error",
                            timer: 5000
                        });
                    }
                });
            };

            this.loadMonthlySettlement = function (callback) {
                $http.post("/report/dailySettlementReports/previewMonthlyReport").then(function (response) {
                    dataSourceService.setByElementTypeAndRole("reportPreviewGrid", "dailySettlement", response.data);
                });
                $http.post("/pos/dailySettlements/monthly").then(function (response) {
                    self.dailySettlementGuid = response.data;
                });
                callback();
            };

            this.closeDailySettlement = function (callback) {
                if (self.dailySettlementGuid) {
                    $http.get("/pos/cashbook/isAdjustmentNecessary?storeGuid=" + storeService.currentStore.guid).then(function (response) {
                        if (response.data != null && response.data.type == "OUT") {
                            swal({
                                title: "Entnahme notwendig!",
                                text: "Bevor Sie den Tagesabschluss durchführen können, müssen Sie € " + response.data.amount.toFixed(2) + " entnehmen.",
                                type: "warning",
                                cancelButtonText: 'Abbrechen',
                                confirmButtonText: 'Entnehmen',
                                showCancelButton: true
                            }, function () {
                                $http.post("/pos/cashbook/adjustCashStatus?storeGuid=" + storeService.currentStore.guid).then(function () {
                                    $http.post("/report/dailySettlementReports/previewFinancialReport").then(function (response) {
                                        dataSourceService.setByElementTypeAndRole("reportPreviewGrid", "dailySettlement", response.data);
                                        self.finishDailySettlement(callback);
                                    });
                                });
                            });
                        }
                        else if (response.data != null && response.data.type == "IN") {
                            swal({
                                title: "Einlage notwendig!",
                                text: "Bevor Sie den Tagesabschluss durchführen können, müssen Sie € " + response.data.amount.toFixed(2) + " einlegen.",
                                type: "warning",
                                cancelButtonText: 'Abbrechen',
                                confirmButtonText: 'Einlegen',
                                showCancelButton: true
                            }, function () {

                                $http.post("/pos/cashbook/adjustCashStatus?storeGuid=" + storeService.currentStore.guid).then(function () {
                                    $http.post("/report/dailySettlementReports/previewFinancialReport").then(function (response) {
                                        dataSourceService.setByElementTypeAndRole("reportPreviewGrid", "dailySettlement", response.data);
                                        self.finishDailySettlement(callback);
                                    });
                                });
                            });
                        }
                        else {
                            self.finishDailySettlement(callback);
                        }
                    });

                }
            };

            this.closeMonthlySettlement = function (callback) {
                setTimeout(function () {
                    swal({
                        title: "Monatsabschluss durchführen?",
                        type: "warning",
                        cancelButtonText: 'Abbrechen',
                        confirmButtonText: 'Durchführen',
                        showCancelButton: true
                    }, function () {
                        $http.post("/pos/dailySettlements/closeMonthly?guid=" + self.dailySettlementGuid).then(function () {
                            swal({
                                title: "Monatsabschluss durchgeführt",
                                type: "success",
                                timer: 5000
                            });
                            rootScope.$emit("showInvoicesGridService-" + "invoices");
                            callback();
                        });
                    });
                }, 500);
            };

            this.finishDailySettlement = function (callback) {
                setTimeout(function () {
                    swal({
                        title: "Tagesabschluss durchführen?",
                        text: "Nachdem der Tagesabschluss durchgeführt wurde können Sie bis zum Betriebsschluss keine neuen Rechnungen mehr erstellen!",
                        type: "warning",
                        cancelButtonText: 'Abbrechen',
                        confirmButtonText: 'Durchführen',
                        showCancelButton: true
                    }, function () {
                        $http.post("/pos/dailySettlements/close?guid=" + self.dailySettlementGuid).then(function () {
                            swal({
                                title: "Tagesabschluss durchgeführt",
                                type: "success",
                                timer: 5000
                            });
                            invoiceService.invoices = [];
                            rootScope.$emit("showInvoicesGridService-" + "invoices");
                            callback();
                            setTimeout(socketService.reloadPossible(), 1000);
                        });
                    });
                }, 500);
            };

            this.abortDailySettlement = function () {
                if (self.dailySettlementGuid) {
                    $http.delete("/pos/dailySettlements/delete", {data: self.dailySettlementGuid});
                }
            };

            this.abortMonthlySettlement = function () {
                if (self.dailySettlementGuid) {
                    $http.delete("/pos/dailySettlements/deleteMonthly", {data: self.dailySettlementGuid});
                }
            };
        }];
});