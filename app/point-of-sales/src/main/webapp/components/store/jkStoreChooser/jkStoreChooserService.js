//# sourceURL=pos.jkStoreChooserService.js
define([
    'angular'
], function (angular) {
    return ['pos.dataService', '$http', function (dataService, http) {
        this.serviceName = "pos.jkStoreChooserService";
        this.storeKey = "stores";
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
                name: 'Number',
                field: 'storeId',
                width: "20%"
            }, {
                name: 'Name',
                field: 'name',
                width: "80%"
            }];
        };
        this.getStores = function (areaData) {
            var gridData = self.getDirectiveData(self.storeKey);
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
                    "storeId": item.storeId,
                    "name": item.name,
                    "value": item
                });
            });
            return displayData;
        };
        this.getAllStores = function () {
            http.get("/data/stores/findByUser").then(function (response) {
                self.setDirectiveData(self.storeKey, response.data);
            });
        };
    }];
});