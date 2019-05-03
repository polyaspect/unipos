//# sourceURL=pos.designer.buttonDirectiveSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.buttonDirectiveSettings"
], function (angular) {
    return ['$scope', '$uibModalInstance', 'pos.designer.screenService', '$compile', '$timeout', 'area', 'options',
        function (scope, uibModalInstance, screenService, $compile, timeout, area, options) {
            scope.area = area;
            scope.modelOptions = options;
            scope.options = {
                showTypes: true
            };
            scope.actions = [{
                text: "Produkt bonieren",
                name: "pos.productService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.product-button-settings'
            }, {
                text: "Storno Funktion",
                name: "pos.reversalService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.reversal-button-settings'
            }, {
                text: "Seite wechseln",
                name: "pos.switchScreenService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.switch-screen-button-settings'
            }, {
                text: "Zahlung hinzufügen",
                name: "pos.paymentService",
                action: "addPaymentOrderItem",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.payment-button-settings'
            }, {
                text: "Login-Button",
                name: "pos.loginService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.login-button-settings'
            }, {
                text: "Sub Total Button",
                name: "pos.subTotalService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.sub-total-button-settings'
            }, {
                text: "Add New Device Button",
                name: "pos.addNewDeviceService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.add-new-device-button-settings'
            }, {
                text: "Logout-Button",
                name: "pos.logoutService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.logout-button-settings'
            }, {
                text: "Rechnungen anzeigen",
                name: "pos.showInvoicesService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.show-Invoices-button-settings'
            }, {
                text: "Ins Admin-Menü wechseln",
                name: "pos.adminMenuService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.admin-menu-button-settings'
            }, {
                text: "Discount",
                name: "pos.discountService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.discount-button-settings'
            }, {
                text: "Show Daily Settlement",
                name: "pos.reportService",
                action: "fillDailySettlementData",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.show-daily-settlement-button-settings'
            }, {
                text: "Close Daily Settlement",
                name: "pos.reportService",
                action: "closeDailySettlement",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.close-daily-settlement-button-settings'
            }, {
                text: "Abort Daily Settlement",
                name: "pos.reportService",
                action: "abortDailySettlement",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.abort-daily-settlement-button-settings'
            }, {
                text: "Add Printer To Device",
                name: "pos.printerService",
                action: "addPrinterToDevice",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.add-printer-to-device-button-settings'
            }, {
                text: "Enter Button",
                name: "pos.enterService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.enter-button-settings'
            }, {
                text: "CashBook Button",
                name: "pos.cashbookEntryService",
                action: "addCashBookEntry",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.cash-book-button-settings'
            }, {
                text: "Rechnung splitten",
                name: "pos.splitOrderButtonService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.split-order-button-settings'
            },  {
                text: "Rechnung neu zuordnen",
                name: "pos.reassignOrderService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.reassign-order-button-settings'
            },  {
                text: "Rechnung verschieben",
                name: "pos.moveOrderService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.move-order-button-settings'
            },  {
                text: "Zahlung splitten",
                name: "pos.splitPaymentService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.split-payment-button-settings'
            },{
                text: "Bestellung öffnen",
                name: "pos.openOrderService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.open-order-button-settings'
            }, {
                text: "Offene Bestellungen öffnen",
                name: "pos.showOpenOrdersService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.show-open-orders-button-settings'
            }, {
                text: "Rechnung stornieren",
                name: "pos.revertInvoiceButtonService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.revert-invoice-button-settings'
            }, {
                text: "Rechnung nachdrucken",
                name: "pos.reprintInvoiceButtonService",
                action: "doAction",
                settingsDirective: 'pos.designer.designer-settings.button-directive-settings.reprint-invoice-button-settings'
            }];

            scope.selectAction = function (item) {
                scope.options.showTypes = false;
                let tabs = $compile('<' + item.settingsDirective + "></" + item.settingsDirective + '>')(scope);
                angular.element(".button-directive-settings .directive-tabs").append(tabs);
            };

            if (scope.area && scope.area.data) {
                var action = Enumerable.From(scope.actions).FirstOrDefault(undefined, function (x) {
                    return x.name == scope.area.data.serviceName && x.action == scope.area.data.serviceAction;
                });

                timeout(function () {
                    scope.selectAction(action);
                }, 10);
            }
            scope.hide = function () {
                uibModalInstance.close();
            };
        }];
});