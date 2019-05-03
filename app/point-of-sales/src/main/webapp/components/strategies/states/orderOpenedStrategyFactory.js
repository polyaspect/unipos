//# sourceURL=pos.orderOpenedStrategyFactory.js
define([
    'angular',
    'pos.quantityInputStrategy'
], function (angular) {
    return {
        createNumberInputStrategy: function () {
            return require("pos.quantityInputStrategy");
        },
        getFilter: function () {
            return require("pos.numberFilter");
        }
    };
});