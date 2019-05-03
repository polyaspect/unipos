//# sourceURL=pos.functionsService.js
define([
    'angular'
], function (angular) {
    return ['$injector', 'pos.directiveIdsConst', 'pos.dataService', function (injector, directiveIdsConst, dataService) {
        this.serviceName = "functionsService";
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