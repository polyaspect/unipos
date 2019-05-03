//# sourceURL=pos.ui.elements.grid.gridDirectiveService.js
define([
    'angular'
], function (angular) {
    return ['pos.dataService', 'pos.ui.dataSourceService', '$injector', function (dataService, dataSourceService, injector) {
        this.serviceName = "gridDirectiveService";
        var self = this;
        dataService.setByServiceKey(self.serviceName, {});

        this.setDirectiveData = function (directiveId, data) {
            dataService.setByDirectiveKey(self.serviceName, directiveId, data);
        };

        this.getDirectiveData = function (directiveId) {
            return dataService.getByDirectiveKey(self.serviceName, directiveId);
        };

        this.getData = function (areaData) {
            if(areaData.data.legacy != undefined && areaData.data.legacy == false){
                var viewController = injector.get("pos.ui.elements.grid." + areaData.data.elementType + "ViewController");
                return {
                    enableFullRowSelection: true,
                    enableRowHeaderSelection: false,
                    multiSelect: false,
                    columnDefs: viewController.getColumnDefs()
                };
            }

            var service = injector.get(areaData.data.service.name);
            return {
                enableFullRowSelection: true,
                enableRowHeaderSelection: false,
                multiSelect: false,
                columnDefs: service[areaData.data.service.columnDefs](),
                data: service[areaData.data.service.data](areaData)
            };
        };

        this.cellClick = function (elementData, row, col) {
            var service = injector.get(elementData.data.service.name);
            if(service.click != undefined){
                service.click(elementData, row, col);
            }
        };
    }];
});