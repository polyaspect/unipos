//# sourceURL=pos.productCustomPriceEnterStrategy.js
define([
    'angular'
], function (angular) {
    return {
        enterButton: function (injector) {
            var productService = injector.get("pos.productService");
            productService.addProductOrderItem(productService.strategyData.enterButton);
        }
    };
});