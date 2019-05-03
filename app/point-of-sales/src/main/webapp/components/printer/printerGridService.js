//# sourceURL=pos.printerGridService.js
define([
    'angular'
], function (angular) {
    return ['pos.dataService', 'pos.printerService', function (dataService, printerService) {
        this.serviceName = "pos.printerGridService";
        var self = this;
        dataService.setByServiceKey(self.serviceName, {});

        this.setDirectiveData = function (directiveId, data) {
            dataService.setByDirectiveKey(self.serviceName, directiveId, data);
        };
        this.getDirectiveData = function (directiveId) {
            return dataService.getByDirectiveKey(self.serviceName, directiveId);
        };
        this.getColumnDefs = function () {
            return [{
                name: 'Name',
                field: 'name',
                width: "60%"
            },{
                name: 'Typ',
                field: 'typeName',
                width: "40%"
            }];
        };
        this.getData = function (areaData) {
            var gridData = printerService.getPrinters();
            var displayData = [];
            var index = 1;
            angular.forEach(gridData, function (item, index) {
                if (!item.css) {
                    item.css = {};
                }
                item.css.evenRows = areaData.data && areaData.data.css && areaData.data.css.evenRows;
                item.css.oddRows = areaData.data && areaData.data.css && areaData.data.css.oddRows;
                item.css.selectedRow = areaData.data && areaData.data.css && areaData.data.css.selectedRow;
                item.css.cell = areaData.data && areaData.data.css && areaData.data.css.cell;
                item.index = index;
                index++;
                displayData.push({
                    "name": item.name,
                    "typeName": item.typeName,
                    "value": item
                });
            });
            return displayData;
        };
    }];
});