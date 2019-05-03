//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.subTotalButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.subTotalButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', function (rootScope, initOptions, screenService, styleService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/subTotalButton/subTotalButtonSettings.html',
            link: function (scope, el, attrs) {
                if (scope.modelOptions.showDesignFirst) {
                    scope.activeTab = 1;
                }
                scope.model = {};
                scope.parentCss = styleService.getGlobalCssData().button.data.subTotalFunction.data || {};
                scope.screens = screenService.getScreens();
                if (scope.area && scope.area.data) {
                    scope.model.buttonText = scope.area.data.name;
                    scope.model.screen = Enumerable.From(scope.screens).FirstOrDefault(undefined, function (x) {
                        return x.name == scope.area.data.serviceData.screenName;
                    });
                }
                scope.save = function () {
                    let subTotalButtonConfig = scope.area;
                    subTotalButtonConfig.data = {};
                    subTotalButtonConfig.data.name = scope.model.buttonText;
                    subTotalButtonConfig.data.serviceName = "pos.subTotalService";
                    subTotalButtonConfig.data.serviceAction = "doAction";
                    subTotalButtonConfig.data.serviceData = {
                        screenName: scope.model.screen.name
                    };
                    subTotalButtonConfig.render = true;

                    scope.$broadcast("saveStyle-" + scope.area.uuid);
                    rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                    scope.hide();
                };
            }
        }
    }
    ];
});