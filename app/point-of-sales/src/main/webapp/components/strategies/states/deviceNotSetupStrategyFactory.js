//# sourceURL=pos.deviceNotSetupStrategyFactory.js
define([
    'angular',
    'pos.keyboardInputStrategy'
], function (angular) {
    return {
        createNumberInputStrategy: function () {
            return require("pos.keyboardInputStrategy");
        },
        getFilter: function () {
            return require("pos.everyThingFilter");
        },
        createEnterButtonStrategy: function () {
            return require("pos.deviceNotSetupStrategy");
        }
    };
});