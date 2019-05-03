//# sourceURL=pos.enterService.js
define([
    'angular'
], function (angular) {
    return ['pos.discountService', 'pos.abstractStrategyFactory', 'pos.ui.elements.text.textDirectiveService', '$injector',
    function (discountService, abstractStrategyFactory, textDirectiveService, injector) {
        this.doAction = function (cell) {
            abstractStrategyFactory.createEnterButtonStrategy().enterButton(injector);
            /*if (abstractStrategyFactory.getState() == "ENTER_DISCOUNT") {
             discountService.addDiscountItem(discountService.discountCell);
             abstractStrategyFactory.createNumberInputStrategy().numberInput(textDirectiveService, "price", "");
             abstractStrategyFactory.changeState("ORDER_OPENED");
             discountService.discountCell = undefined;
             }*/
        };
    }

    ]
    ;
});