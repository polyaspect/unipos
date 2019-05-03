//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.switchScreenButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.switchScreenButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', function (rootScope, initOptions, screenService, styleService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/switchScreenButton/switchScreenButtonSettings.html',
            link: function (scope, el, attrs) {
                if (scope.modelOptions.showDesignFirst) {
                    scope.activeTab = 1;
                }
                scope.model = {};
                scope.parentCss = styleService.getGlobalCssData().button.data.switchScreenFunction.data || {};
                scope.screens = screenService.getScreens();
                var areaConfig = scope.area;
                if (areaConfig && areaConfig.data) {
                    scope.model.buttonText = areaConfig.data.name;
                    scope.model.screen = Enumerable.From(scope.screens).FirstOrDefault(undefined, function (x) {
                        return x.name == areaConfig.data.serviceData.screenName;
                    });
                }
                scope.save = function () {
                    var switchScreenButtonConfig = scope.area;
                    switchScreenButtonConfig.data = {};
                    switchScreenButtonConfig.data.name = scope.model.buttonText;
                    switchScreenButtonConfig.data.serviceName = "pos.switchScreenService";
                    switchScreenButtonConfig.data.serviceAction = "doAction";
                    switchScreenButtonConfig.data.serviceData = {
                        screenName: scope.model.screen.name
                    };
                    switchScreenButtonConfig.render = true;

                    scope.$broadcast("saveStyle-" + scope.area.uuid);
                    rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                    scope.hide();
                }
            }
        }
    }
    ];
});