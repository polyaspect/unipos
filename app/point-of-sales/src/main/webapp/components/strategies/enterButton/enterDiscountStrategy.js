//# sourceURL=pos.enterDiscountStrategy.js
define([
    'angular'
], function (angular) {
    return {
        enterButton: function (injector) {
            var discountService = injector.get("pos.discountService");
            var abstractStrategyFactory = injector.get("pos.abstractStrategyFactory");
            var textDirectiveService = injector.get("pos.ui.elements.text.textDirectiveService");
            discountService.addDiscountItem(discountService.discountCell);
            abstractStrategyFactory.createNumberInputStrategy().numberInput(textDirectiveService, "price", "");
            abstractStrategyFactory.changeState("ORDER_OPENED");
            discountService.discountCell = undefined;
        }
    };
});