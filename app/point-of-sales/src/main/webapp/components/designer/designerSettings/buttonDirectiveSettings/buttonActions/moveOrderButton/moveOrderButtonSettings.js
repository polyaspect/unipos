//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.moveOrderButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.moveOrderButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService',  'pos.designer.styleService', function (rootScope, initOptions, screenService, styleService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/moveOrderButton/moveOrderButtonSettings.html',
            link: function (scope, el, attrs) {
                if (scope.modelOptions.showDesignFirst) {
                    scope.activeTab = 1;
                }
                scope.model = {};
                scope.parentCss = styleService.getGlobalCssData().button.data.moveOrderFunction.data || {};
                scope.screens = screenService.getScreens();
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
                    buttonConfig.data.serviceName = "pos.moveOrderService";
                    buttonConfig.data.serviceAction = "doAction";
                    buttonConfig.data.serviceData = {
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