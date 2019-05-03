//# sourceURL=pos.splitOrderStrategyFactory.js
define([
    'angular',
    'pos.splitOrderChooseOrderItemStrategy',
    'pos.splitOrderEnterStrategy'
], function (angular) {
    return {
        createChooseOrderItemStrategy: function () {
            return require("pos.splitOrderChooseOrderItemStrategy");
        },
        createEnterButtonStrategy: function () {
            return require("pos.splitOrderEnterStrategy");
        }
    };
});