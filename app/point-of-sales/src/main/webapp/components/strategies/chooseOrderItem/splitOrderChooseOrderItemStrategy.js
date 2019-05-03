//# sourceURL=pos.splitOrderChooseOrderItemStrategy.js
define([
    'angular'
], function (angular) {
    return {
        chooseOrderItem : function(injector, elementData, orderItem){
            injector.get("pos.splitOrderService").chooseOrderItem(elementData, orderItem);
        },
        getViewData : function(injector, elementData){
            return injector.get("pos.splitOrderService").getViewData(elementData);
        }
    };
});