//# sourceURL=pos.splitOrderService.js
define([
    'angular'
], function (angular) {
    return ['$injector', 'pos.abstractStrategyFactory',
        function (injector, abstractStrategyFactory) {

            this.doAction = function (element) {
                var tagKey = element.serviceData.tagKey.name;
                var tagValue = element.serviceData.tagValue;

                abstractStrategyFactory.openOrderStrategy().openOrder(injector, element, tagKey, tagValue);
            }

        }];
});