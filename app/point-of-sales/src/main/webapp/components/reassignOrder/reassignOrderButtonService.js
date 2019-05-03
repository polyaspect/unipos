//# sourceURL=pos.splitOrderService.js
define([
    'angular'
], function (angular) {
    return ['pos.ui.elements.grid.gridDirectiveService', 'pos.areaService', 'pos.directiveIdsConst','$rootScope', 'pos.moveOrderService', 'pos.abstractStrategyFactory',
        function (gridDirectiveService, screenService, directiveIdsConst, http, rootScope, moveOrderService, abstractStrategyFactory) {

            this.doAction = function(element){
                var elementData = element.serviceData;

                screenService.toScreen(elementData.screenName);
                abstractStrategyFactory.changeState("REASSIGN_ORDER");
            }

        }];
});