//# sourceURL=pos.splitOrderChooseOrderItemStrategy.js
define([
    'angular'
], function (angular) {
    return {
        chooseOrder : function(injector, elementData, order){
            injector.get("pos.openOrderService").chooseOrder(elementData, order);
        },
        getViewData : function(injector, elementData){
            return injector.get("pos.openOrderService").getViewData(elementData);
        }
    };
});