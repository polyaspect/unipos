//# sourceURL=pos.dataService.js
define([
    'angular'
], function (angular) {
    return ["$rootScope", function (rootScope) {
        var data = {};
        this.getByServiceKey = function (serviceKey) {
            return data[serviceKey];
        };
        this.setByServiceKey = function (serviceKey, serviceData) {
            data[serviceKey] = serviceData;
        };
        this.getByDirectiveKey = function (serviceKey, directiveKey) {
            return data[serviceKey][directiveKey];
        };
        this.setByDirectiveKey = function (serviceKey, directiveKey, directiveData, rootScopeEventKey, rootScopeEventData) {
            data[serviceKey][directiveKey] = directiveData;
            rootScope.$emit(rootScopeEventKey || (serviceKey + "-" + directiveKey), rootScopeEventData);
        };
        this.getAllServiceKeys = function () {
            return Object.keys(data);
        };
        this.getAllData = function () {
            return data;
        }
    }];
});