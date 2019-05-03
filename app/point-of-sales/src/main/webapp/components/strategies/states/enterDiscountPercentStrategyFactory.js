//# sourceURL=pos.enterDiscountStrategyFactory.js
define([
    'angular',
    'pos.percentInputStrategy',
    'pos.enterDiscountStrategy'
], function (angular) {
    return {
        createNumberInputStrategy: function () {
            return require("pos.percentInputStrategy");
        },
        createEnterButtonStrategy: function () {
            return require("pos.enterDiscountStrategy");
        },
        getFilter: function () {
            return require("pos.percentFilter");
        }

    };
});