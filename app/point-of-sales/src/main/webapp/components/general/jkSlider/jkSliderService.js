//# sourceURL=jkSliderService.js
define([
    'angular'
], function (angular) {
    return ['pos.directiveIdsConst', 'pos.dataService', function (directiveIdsConst, dataService) {
        this.serviceName = "jkSliderService";
        var self = this;
        dataService.setByServiceKey(self.serviceName, {});

        this.setDirectiveData = function (directiveId, data) {
            dataService.setByDirectiveKey(self.serviceName, directiveId, data);
        };

        this.getDirectiveData = function (directiveId) {
            return dataService.getByDirectiveKey(self.serviceName, directiveId);
        };
    }];
});