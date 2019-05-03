//# sourceURL=pos.switchScreenService.js
define([
    'angular'
], function (angular) {
    return ['pos.areaService', function (areaService) {
        var self = this;
        this.doAction = function (cell) {
            areaService.toScreen(cell.serviceData.screenName);
        };
    }];
});