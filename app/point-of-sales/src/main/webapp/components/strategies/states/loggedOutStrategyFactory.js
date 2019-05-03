//# sourceURL=pos.loggedOutStrategyFactory.js
define([
    'angular',
    'pos.numpadInputStrategy'
], function (angular) {
    return {
        createNumberInputStrategy: function () {
            return require("pos.numpadInputStrategy");
        },
        getFilter: function () {
            return require("pos.numberFilter");
        },
        createEnterButtonStrategy: function () {
            return require("pos.loggedOutStrategy");
        }
    };
});