//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.adminMenuButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.adminMenuButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', function (rootScope, initOptions, screenService, styleService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/adminMenuButton/adminMenuButtonSettings.html',
            link: function (scope, el, attrs) {
                if (scope.modelOptions.showDesignFirst) {
                    scope.activeTab = 1;
                }
                scope.model = {};
                if (scope.area && scope.area.data) {
                    scope.model.buttonText = scope.area.data.name;
                }
                scope.parentCss = styleService.getGlobalCssData().button.data.adminMenuFunction.data || {};
                scope.save = function () {
                    var adminMenuButtonConfig = scope.area;
                    adminMenuButtonConfig.data = {};
                    adminMenuButtonConfig.data.name = scope.model.buttonText;
                    adminMenuButtonConfig.data.serviceName = "pos.adminMenuService";
                    adminMenuButtonConfig.data.serviceAction = "doAction";
                    adminMenuButtonConfig.render = true;

                    scope.$broadcast("saveStyle-" + scope.area.uuid);
                    rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                    scope.hide();
                }
            }
        }
    }
    ];
});