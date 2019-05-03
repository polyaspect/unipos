//# sourceURL=pos.enterDiscountStrategyFactory.js
define([
    'angular',
    'pos.invoiceCreatedInputStrategy',
    'pos.productCustomPriceEnterStrategy',
    'pos.enterDiscountStrategy'
], function (angular) {
    return {
        createNumberInputStrategy: function () {
            return require("pos.invoiceCreatedInputStrategy");
        },
        createEnterButtonStrategy: function () {
            return require("pos.enterDiscountStrategy");
        },
        getFilter: function () {
            return require("pos.priceFilter");
        }
    };
});