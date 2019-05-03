//# sourceURL=pos.cashBookDescriptionStrategy.js
define([
    'angular'
], function (angular) {
    return {
        enterButton: function (injector) {
            var cashBookService = injector.get("pos.cashbookEntryService");
            var abstractStrategyFactory = injector.get("pos.abstractStrategyFactory");
            cashBookService.toNextStep(abstractStrategyFactory.getState());
        }
    };
});