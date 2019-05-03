//# sourceURL=pos.splitOrderStrategyFactory.js
define([
    'angular',
    'pos.openOrderChooseOrderStrategy'
], function (angular) {
    return {
        createChooseOrderStrategy: function () {
            return require("pos.openOrderChooseOrderStrategy");
        }
    };
});