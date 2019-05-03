//# sourceURL=pos.productCustomPriceStrategyFactory.js
define([
    'angular',
    'pos.productCustomPriceInputStrategy',
    'pos.productCustomPriceEnterStrategy'
], function (angular) {
    return {
        createNumberInputStrategy: function () {
            return require("pos.productCustomPriceInputStrategy");
        },
        createEnterButtonStrategy: function () {
            return require("pos.productCustomPriceEnterStrategy");
        },
        getFilter: function () {
            return require("pos.priceFilter");
        }
    };
});