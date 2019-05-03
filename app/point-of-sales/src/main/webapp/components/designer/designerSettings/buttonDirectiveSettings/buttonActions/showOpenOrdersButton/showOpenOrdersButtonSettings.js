//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.showOpenOrdersButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.showOpenOrdersButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', function (rootScope, initOptions, screenService, styleService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/showOpenOrdersButton/showOpenOrdersButtonSettings.html',
            link: function (scope, el, attrs) {
                if (scope.modelOptions.showDesignFirst) {
                    scope.activeTab = 1;
                }
                scope.model = {};
                scope.parentCss = styleService.getGlobalCssData().button.data.showOpenOrdersFunction.data || {};
                scope.tagKeys = [{
                    "name" : "Tisch"
                }];
                var areaConfig = scope.area;
                if (areaConfig && areaConfig.data) {
                    scope.model.buttonText = areaConfig.data.name;
                }
                scope.save = function () {
                    var buttonConfig = scope.area;
                    buttonConfig.data = {};
                    buttonConfig.data.name = scope.model.buttonText;
                    buttonConfig.data.serviceName = "pos.showOpenOrdersService";
                    buttonConfig.data.serviceAction = "doAction";
                    buttonConfig.data.serviceData = {
                        tagKey: scope.model.tagKey,
                        tagValue : scope.model.tagValue
                    };
                    buttonConfig.render = true;

                    scope.$broadcast("saveStyle-" + scope.area.uuid);
                    rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                    scope.hide();
                }
            }
        }
    }
    ];
});