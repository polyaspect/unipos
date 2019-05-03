//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.enterButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.enterButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', function (rootScope, initOptions, screenService, styleService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/enterButton/enterButtonSettings.html',
            link: function (scope, el, attrs) {
                if (scope.modelOptions.showDesignFirst) {
                    scope.activeTab = 1;
                }
                scope.model = {};
                scope.parentCss = styleService.getGlobalCssData().button.data.enterFunction.data || {};
                if (scope.area && scope.area.data) {
                    scope.model.buttonText = scope.area.data.name;
                }
                scope.save = function () {
                    var reversalButtonConfig = scope.area;
                    reversalButtonConfig.data = {};
                    reversalButtonConfig.data.name = scope.model.buttonText;
                    reversalButtonConfig.data.serviceName = "pos.enterService";
                    reversalButtonConfig.data.serviceAction = "doAction";
                    reversalButtonConfig.data.serviceData = {};
                    reversalButtonConfig.render = true;

                    scope.$broadcast("saveStyle-" + scope.area.uuid);
                    rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                    scope.hide();
                };
            }
        }
    }
    ];
});