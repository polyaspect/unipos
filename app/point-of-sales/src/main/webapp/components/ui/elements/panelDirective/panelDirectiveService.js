//# sourceURL=pos.ui.elements.panel.panelDirectiveService.js
define([
    'angular'
], function (angular) {
    return ['pos.dataService', '$injector', function (dataService, injector) {
        this.serviceName = "panelDirectiveService";
        var self = this;
        dataService.setByServiceKey(self.serviceName, {});

        this.setDirectiveData = function (directiveId, data) {
            dataService.setByDirectiveKey(self.serviceName, directiveId, data);
        };

        this.getDirectiveData = function (directiveId) {
            return dataService.getByDirectiveKey(self.serviceName, directiveId);
        };

        this.getData = function (cell) {
            var service = injector.get(cell.service.name);
            if (service != undefined) {
                return service[cell.service.data](cell);
            }
        };
        this.getDataSettings = function (cell) {
            var service = injector.get(cell.service.name);
            if (service != undefined) {
                if (service[cell.service.data + "Settings"] != undefined) {
                    return service[cell.service.data + "Settings"](cell);
                }
            }
        };
        this.getPanelOptions = function (directiveId) {
            return {
                scrollButtons: {
                    prevOrNextButtonRoot: directiveId,
                    nextButton: ".scroll-button.next",
                    prevButton: ".scroll-button.prev",
                    slickPrevButton: "#slick-buttons .slick-prev",
                    slickNextButton: "#slick-buttons .slick-next",
                    prev: {},
                    next: {}
                },
                slider: {
                    appendArrows: directiveId + " #slick-buttons",
                }
            };
        };
    }];
});