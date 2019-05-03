//# sourceURL=pos.panelDirectiveSettingsConfig.js
require.config({
    paths: {
        "pos.designer.designerSettings.panelDirectiveSettings": "components/designer/designerSettings/panelDirectiveSettings/panelDirectiveSettings",

        "pos.designer.designerSettings.panelDirectiveSettings.numpadPanelSettings": "components/designer/designerSettings/panelDirectiveSettings/panelTypes/numpad/numpadPanelSettings",
        "pos.designer.designerSettings.panelDirectiveSettings.keyboardPanelSettings": "components/designer/designerSettings/panelDirectiveSettings/panelTypes/keyboard/keyboardPanelSettings",
        "pos.designer.designerSettings.panelDirectiveSettings.categoryPanelSettings": "components/designer/designerSettings/panelDirectiveSettings/panelTypes/category/categoryPanelSettings",

        "pos.designer.designerSettings.panelDirectiveSettings.productPanelSettingsConfig": "components/designer/designerSettings/panelDirectiveSettings/panelTypes/product/productPanelSettingsConfig"
    }
});
define([
    'angular',
    'pos.designer.designerSettings.panelDirectiveSettings',
    'pos.designer.designerSettings.panelDirectiveSettings.numpadPanelSettings',
    'pos.designer.designerSettings.panelDirectiveSettings.keyboardPanelSettings',
    'pos.designer.designerSettings.panelDirectiveSettings.categoryPanelSettings',
    "pos.designer.designerSettings.panelDirectiveSettings.productPanelSettingsConfig"
], function () {
    return function (module) {
        require('pos.designer.designerSettings.panelDirectiveSettings.productPanelSettingsConfig')(module);


        module.controller('pos.designer.designerSettings.panelDirectiveSettings', require('pos.designer.designerSettings.panelDirectiveSettings'));
        module.directive('pos.designer.designerSettings.panelDirectiveSettings.numpadPanelSettings', require('pos.designer.designerSettings.panelDirectiveSettings.numpadPanelSettings'));
        module.directive('pos.designer.designerSettings.panelDirectiveSettings.keyboardPanelSettings', require('pos.designer.designerSettings.panelDirectiveSettings.keyboardPanelSettings'));
        module.directive('pos.designer.designerSettings.panelDirectiveSettings.categoryPanelSettings', require('pos.designer.designerSettings.panelDirectiveSettings.categoryPanelSettings'));
    }
});