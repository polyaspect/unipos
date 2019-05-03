//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.openOrderButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.openOrderButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService',  'pos.designer.styleService', function (rootScope, initOptions, screenService, styleService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/openOrderButton/openOrderButtonSettings.html',
            link: function (scope, el, attrs) {
                if (scope.modelOptions.showDesignFirst) {
                    scope.activeTab = 1;
                }
                scope.model = {};
                scope.parentCss = styleService.getGlobalCssData().button.data.openOrderFunction.data || {};
                scope.screens = screenService.getScreens();
                scope.tagKeys = [{
                    "name" : ""
                },{
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
                    buttonConfig.data.serviceName = "pos.openOrderButtonService";
                    buttonConfig.data.serviceAction = "doAction";
                    buttonConfig.data.serviceData = {
                        tagKey: scope.model.tagKey,
                        tagValue : scope.model.tagValue,
                        screenName: scope.model.screen.name
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