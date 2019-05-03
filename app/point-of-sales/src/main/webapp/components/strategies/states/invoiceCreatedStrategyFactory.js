//# sourceURL=pos.orderOpenedStrategyFactory.js
define([
    'angular',
    'pos.invoiceCreatedInputStrategy'
], function (angular) {
    return {
        createNumberInputStrategy: function () {
            return require("pos.invoiceCreatedInputStrategy");
        },
        getFilter: function () {
            return require("pos.numberFilter");
        }
    };
});