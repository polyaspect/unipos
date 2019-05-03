//# sourceURL=pos.splitPaymentChooseOrderItemStrategy.js
define([
    'angular'
], function (angular) {
    return {
        chooseOrderItem : function(injector){
            injector.get("pos.splitPaymentService").chooseOrderItem();
        }
    };
});