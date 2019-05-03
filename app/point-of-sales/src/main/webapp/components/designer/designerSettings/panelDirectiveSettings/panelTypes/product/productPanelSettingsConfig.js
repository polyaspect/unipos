//# sourceURL=pos.designer.designerSettings.panelDirectiveSettings.productPanelSettingsConfig.js
require.config({
    paths: {
        "pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings": "components/designer/designerSettings/panelDirectiveSettings/panelTypes/product/productPanelSettings",

        "pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.products": "components/designer/designerSettings/panelDirectiveSettings/panelTypes/product/types/products/productTypeProductPanelSettings",
        "pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.productsPerCategory": "components/designer/designerSettings/panelDirectiveSettings/panelTypes/product/types/productsPerCategory/productsPerCategoryTypeProductPanelSettings",
        "pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.productsSingleCategory": "components/designer/designerSettings/panelDirectiveSettings/panelTypes/product/types/productsSingleCategory/productsSingleCategoryTypeProductPanelSettings",
    }
});
define([
    'angular',
    "pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings",
    "pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.products",
    "pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.productsPerCategory",
    "pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.productsSingleCategory"
], function () {
    return function (module) {
        module.directive('pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings', require('pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings'));
        module.directive('pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.products', require('pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.products'));
        module.directive('pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.productsPerCategory', require('pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.productsPerCategory'));
        module.directive('pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.productsSingleCategory', require('pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.productsSingleCategory'));
    }
});