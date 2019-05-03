//# sourceURL=pos.defaultOpenOrderStrategy.js
define([
    'angular'
], function (angular) {
    return {
        openOrder : function(injector, elementData, tagKey, tagValue){
            injector.get("pos.openOrderService").openOrder(elementData, tagKey, tagValue);
        }
    };
});