//# sourceURL=pos.subTotalService.js
define([
    'angular'
], function (angular) {
    return ['pos.orderService', 'pos.ui.elements.text.textDirectiveService', 'pos.areaService', 'pos.abstractStrategyFactory', function (orderService, textService, areaService, abstractStrategyFactory) {
        var self = this;
        this.doAction = function (cell) {
            abstractStrategyFactory.changeState("SUBTOTAL_CALCULATED");
            textService.setText(textService.textModes.price, 0);
            textService.setText(textService.textModes.info, orderService.getOpenAmountOfOrder(orderService.getCurrentOrder()));
            areaService.toScreen(cell.serviceData.screenName);
        };
    }];
});