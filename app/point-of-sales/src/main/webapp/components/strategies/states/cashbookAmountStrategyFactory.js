//# sourceURL=pos.cashbookAmountStrategyFactory.js
define([
    'angular',
    'pos.cashbookAmountInputStrategy',
    'pos.priceFilter',
    'pos.cashBookAmountStrategy'
], function (angular) {
    return {
        createNumberInputStrategy: function () {
            return require("pos.cashbookAmountInputStrategy");
        },
        createEnterButtonStrategy: function () {
            return require("pos.cashBookAmountStrategy");
        },
        getFilter: function () {
            return require("pos.priceFilter");
        }
    };
});