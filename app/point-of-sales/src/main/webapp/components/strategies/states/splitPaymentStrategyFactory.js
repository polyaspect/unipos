//# sourceURL=pos.splitOrderStrategyFactory.js
define([
    'angular',
    'pos.splitPaymentChooseOrderItemStrategy'
], function (angular) {
    return {
        createChooseOrderItemStrategy: function () {
            return require("pos.splitPaymentChooseOrderItemStrategy");
        }
    };
});