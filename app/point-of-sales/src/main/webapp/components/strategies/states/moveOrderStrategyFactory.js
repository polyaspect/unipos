//# sourceURL=pos.splitOrderStrategyFactory.js
define([
    'angular',
    'pos.moveOrderOpenOrderStrategy'
], function (angular) {
    return {
        createOpenOrderItemStrategy: function () {
            return require("pos.moveOrderOpenOrderStrategy");
        }
    };
});