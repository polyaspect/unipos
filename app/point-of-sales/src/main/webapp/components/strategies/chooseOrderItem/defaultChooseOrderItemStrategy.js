//# sourceURL=pos.defaultChooseOrderItemStrategy.js
define([
    'angular'
], function (angular) {
    return {
        chooseOrderItem : function(injector, elementData){
            injector.get("pos.chooseOrderItemService").chooseOrderItem();
        },
        getViewData : function(injector, elementData){
            return {};
        }
    };
});