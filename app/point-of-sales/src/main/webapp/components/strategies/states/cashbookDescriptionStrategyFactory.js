//# sourceURL=pos.cashbookDescriptionStrategyFactory.js
define([
    'angular',
    'pos.cashbookDescriptionInputStrategy',
    'pos.cashBookDescriptionStrategy',
    'pos.everyThingFilter'

], function (angular) {
    return {
        createNumberInputStrategy: function () {
            return require("pos.cashbookDescriptionInputStrategy");
        },
        createEnterButtonStrategy: function () {
            return require("pos.cashBookDescriptionStrategy");
        },
        getFilter: function () {
            return require("pos.everyThingFilter");
        }
    };
});