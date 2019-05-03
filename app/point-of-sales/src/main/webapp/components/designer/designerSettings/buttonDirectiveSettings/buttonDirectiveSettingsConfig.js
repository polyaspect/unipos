//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettingsConfig.js
require.config({
    paths: {
        "pos.designer.designerSettings.buttonDirectiveSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonDirectiveSettings",

        "pos.designer.designerSettings.buttonDirectiveSettings.productButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/productButton/productButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.reversalButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/reversalButton/reversalButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.switchScreenButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/switchScreenButton/switchScreenButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.paymentButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/paymentButton/paymentButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.loginButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/loginButton/loginButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.subTotalButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/subTotalButton/subTotalButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.addNewDeviceButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/addNewDeviceButton/addNewDeviceButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.logoutButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/logoutButton/logoutButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.showInvoicesButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/showInvoicesButton/showInvoicesButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.adminMenuButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/adminMenuButton/adminMenuButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.discountButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/discountButton/discountButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.showDailySettlementButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/showDailySettlementButton/showDailySettlementButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.closeDailySettlementButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/closeDailySettlementButton/closeDailySettlementButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.abortDailySettlementButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/abortDailySettlementButton/abortDailySettlementButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.addPrinterToDeviceButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/addPrinterToDeviceButton/addPrinterToDeviceButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.enterButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/enterButton/enterButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.cashBookButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/cashBookButton/cashBookButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.showOpenOrdersButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/showOpenOrdersButton/showOpenOrdersButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.splitOrderButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/splitOrderButton/splitOrderButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.openOrderButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/openOrderButton/openOrderButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.moveOrderButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/moveOrderButton/moveOrderButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.reassignOrderButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/reassignOrderButton/reassignOrderButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.splitPaymentButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/splitPaymentButton/splitPaymentButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.revertInvoiceButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/revertInvoiceButton/revertInvoiceButtonSettings",
        "pos.designer.designerSettings.buttonDirectiveSettings.reprintInvoiceButtonSettings": "components/designer/designerSettings/buttonDirectiveSettings/buttonActions/reprintInvoiceButton/reprintInvoiceButtonSettings",
    }
});
define([
    'angular',
    'pos.designer.designerSettings.buttonDirectiveSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.productButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.reversalButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.switchScreenButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.paymentButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.loginButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.subTotalButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.addNewDeviceButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.logoutButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.showInvoicesButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.adminMenuButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.discountButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.showDailySettlementButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.closeDailySettlementButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.abortDailySettlementButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.addPrinterToDeviceButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.enterButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.cashBookButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.showOpenOrdersButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.splitOrderButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.openOrderButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.moveOrderButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.reassignOrderButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.splitPaymentButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.revertInvoiceButtonSettings',
    'pos.designer.designerSettings.buttonDirectiveSettings.reprintInvoiceButtonSettings'
], function () {
    return function (module) {
        module.controller('pos.designer.designerSettings.buttonDirectiveSettings', require('pos.designer.designerSettings.buttonDirectiveSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.productButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.productButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.reversalButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.reversalButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.switchScreenButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.switchScreenButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.paymentButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.paymentButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.loginButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.loginButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.logoutButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.logoutButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.subTotalButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.subTotalButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.addNewDeviceButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.addNewDeviceButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.showInvoicesButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.showInvoicesButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.adminMenuButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.adminMenuButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.discountButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.discountButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.showDailySettlementButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.showDailySettlementButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.closeDailySettlementButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.closeDailySettlementButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.abortDailySettlementButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.abortDailySettlementButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.addPrinterToDeviceButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.addPrinterToDeviceButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.enterButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.enterButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.cashBookButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.cashBookButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.showOpenOrdersButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.showOpenOrdersButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.splitOrderButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.splitOrderButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.openOrderButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.openOrderButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.moveOrderButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.moveOrderButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.reassignOrderButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.reassignOrderButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.splitPaymentButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.splitPaymentButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.revertInvoiceButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.revertInvoiceButtonSettings'));
        module.directive('pos.designer.designerSettings.buttonDirectiveSettings.reprintInvoiceButtonSettings', require('pos.designer.designerSettings.buttonDirectiveSettings.reprintInvoiceButtonSettings'));
    }
});