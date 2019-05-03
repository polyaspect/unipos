//# sourceURL=pos.subtotalCalculatedStrategyFactory.js
define([
    'angular',
    'pos.subtotalCalculatedInputStrategy',
    'pos.priceFilter'
], function (angular) {
    return {
        createNumberInputStrategy: function () {
            return require("pos.subtotalCalculatedInputStrategy");
        },
        getFilter: function () {
            return require("pos.priceFilter");
        }
    };
});