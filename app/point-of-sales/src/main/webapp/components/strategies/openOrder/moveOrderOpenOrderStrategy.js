//# sourceURL=pos.moveOrderOpenOrderStrategy.js
define([
    'angular'
], function (angular) {
    return {
        openOrder : function(injector, elementData, tagKey, tagValue){
            injector.get("pos.moveOrderService").openOrder(elementData, tagKey, tagValue);
        }
    };
});