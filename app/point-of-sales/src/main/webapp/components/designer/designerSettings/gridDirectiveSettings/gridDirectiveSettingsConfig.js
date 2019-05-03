//# sourceURL=pos.gridDirectiveSettingsConfig.js
require.config({
    paths: {
        "pos.designer.designerSettings.gridDirectiveSettings": "components/designer/designerSettings/gridDirectiveSettings/gridDirectiveSettings",

        "pos.designer.designerSettings.gridDirectiveSettings.orderGridSettings": "components/designer/designerSettings/gridDirectiveSettings/gridTypes/orderGrid/orderGridSettings",
        "pos.designer.designerSettings.gridDirectiveSettings.invoicesGridSettings": "components/designer/designerSettings/gridDirectiveSettings/gridTypes/invoicesGrid/invoicesGridSettings",
        "pos.designer.designerSettings.gridDirectiveSettings.storesGridSettings": "components/designer/designerSettings/gridDirectiveSettings/gridTypes/storesGrid/storesGridSettings",
        "pos.designer.designerSettings.gridDirectiveSettings.reportPreviewGridSettings": "components/designer/designerSettings/gridDirectiveSettings/gridTypes/reportPreviewGrid/reportPreviewGridSettings",
        "pos.designer.designerSettings.gridDirectiveSettings.printerGridSettings": "components/designer/designerSettings/gridDirectiveSettings/gridTypes/printerGrid/printerGridSettings",
        "pos.designer.designerSettings.gridDirectiveSettings.cashbookEntryGridSettings": "components/designer/designerSettings/gridDirectiveSettings/gridTypes/cashbookEntryGrid/cashbookEntryGridSettings",
        "pos.designer.designerSettings.gridDirectiveSettings.chooseOrderItemGridSettings": "components/designer/designerSettings/gridDirectiveSettings/gridTypes/chooseOrderItemGrid/chooseOrderItemGridSettings",
        "pos.designer.designerSettings.gridDirectiveSettings.chooseOrderGridSettings": "components/designer/designerSettings/gridDirectiveSettings/gridTypes/chooseOrderGrid/chooseOrderGridSettings",
        "pos.designer.designerSettings.gridDirectiveSettings.usersGridSettings": "components/designer/designerSettings/gridDirectiveSettings/gridTypes/usersGrid/usersGridSettings"
    }
});
define([
    'angular',
    'pos.designer.designerSettings.gridDirectiveSettings',
    'pos.designer.designerSettings.gridDirectiveSettings.orderGridSettings',
    'pos.designer.designerSettings.gridDirectiveSettings.invoicesGridSettings',
    'pos.designer.designerSettings.gridDirectiveSettings.storesGridSettings',
    'pos.designer.designerSettings.gridDirectiveSettings.reportPreviewGridSettings',
    'pos.designer.designerSettings.gridDirectiveSettings.printerGridSettings',
    'pos.designer.designerSettings.gridDirectiveSettings.cashbookEntryGridSettings',
    'pos.designer.designerSettings.gridDirectiveSettings.chooseOrderItemGridSettings',
    'pos.designer.designerSettings.gridDirectiveSettings.chooseOrderGridSettings',
    'pos.designer.designerSettings.gridDirectiveSettings.usersGridSettings'
], function () {
    return function (module) {
        module.controller('pos.designer.designerSettings.gridDirectiveSettings', require('pos.designer.designerSettings.gridDirectiveSettings'));
        module.directive('pos.designer.designerSettings.gridDirectiveSettings.orderGridSettings', require('pos.designer.designerSettings.gridDirectiveSettings.orderGridSettings'));
        module.directive('pos.designer.designerSettings.gridDirectiveSettings.invoicesGridSettings', require('pos.designer.designerSettings.gridDirectiveSettings.invoicesGridSettings'));
        module.directive('pos.designer.designerSettings.gridDirectiveSettings.storesGridSettings', require('pos.designer.designerSettings.gridDirectiveSettings.storesGridSettings'));
        module.directive('pos.designer.designerSettings.gridDirectiveSettings.reportPreviewGridSettings', require('pos.designer.designerSettings.gridDirectiveSettings.reportPreviewGridSettings'));
        module.directive('pos.designer.designerSettings.gridDirectiveSettings.printerGridSettings', require('pos.designer.designerSettings.gridDirectiveSettings.printerGridSettings'));
        module.directive('pos.designer.designerSettings.gridDirectiveSettings.cashbookEntryGridSettings', require('pos.designer.designerSettings.gridDirectiveSettings.cashbookEntryGridSettings'));
        module.directive('pos.designer.designerSettings.gridDirectiveSettings.chooseOrderItemGridSettings', require('pos.designer.designerSettings.gridDirectiveSettings.chooseOrderItemGridSettings'));
        module.directive('pos.designer.designerSettings.gridDirectiveSettings.chooseOrderGridSettings', require('pos.designer.designerSettings.gridDirectiveSettings.chooseOrderGridSettings'));
        module.directive('pos.designer.designerSettings.gridDirectiveSettings.usersGridSettings', require('pos.designer.designerSettings.gridDirectiveSettings.usersGridSettings'));
    }
});