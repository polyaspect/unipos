//# sourceURL=pos.ui.dataSourceService.js
define([
    'angular'
], function (angular) {
    return ["$rootScope", "$injector", function (rootScope, $injector) {
        var dataSources = [];
        var self = this;

        this.getByElementType = function (uiElementType) {
            return dataSources[uiElementType];
        };

        this.getByElementTypeAndRole = function(uiElementType, uiElementRole){
            return dataSources[uiElementType + "-"  + uiElementRole];
        };

        this.setByElementType = function (uiElementType, dataSource) {
            dataSources[uiElementType] = dataSource;
        };

        this.setByElementTypeAndRole = function (uiElementType, uiElementRole, dataSource) {
            dataSources[uiElementType + "-" + uiElementRole] = dataSource;
            rootScope.$emit("updateDataSource:" + uiElementType, uiElementRole);
        };

        this.getViewData = function(metaData, elementCategory, elementType, role){
            var viewController = $injector.get("pos.ui.elements." + elementCategory + "." + elementType + "ViewController");
            if(role != undefined){
                return viewController.getViewData(self.getByElementTypeAndRole(elementType, role), metaData);
            }else{
                return viewController.getViewData(self.getByElementType(elementType), metaData);
            }
        };

    }];
});