//# sourceURL=pos.doNothingStrategyFactory.js
define([
    'angular',
    'pos.doNothingInputStrategy',
    'pos.doNothingEnterStrategy'
], function (angular) {
    return {
        createNumberInputStrategy: function () {
            return require("pos.doNothingInputStrategy");
        },
        createEnterButtonStrategy: function () {
            return require("pos.doNothingEnterStrategy");
        },
        getFilter: function () {
            return require("pos.doNothingFilter");
        }
    };
});