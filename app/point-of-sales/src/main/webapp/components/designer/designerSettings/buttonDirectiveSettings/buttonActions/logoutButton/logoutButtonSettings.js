//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.logoutButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.logoutButtonSettings'
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope',
        function (initOptions, screenService, styleService, rootScope) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/logoutButton/logoutButtonSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {};
                    scope.parentCss = styleService.getGlobalCssData().button.data.logoutFunction.data || {};
                    if (scope.area && scope.area.data) {
                        scope.model.buttonText = scope.area.data.name;
                    }
                    scope.save = function () {
                        var logoutButtonConfig = scope.area;
                        logoutButtonConfig.data = {};
                        logoutButtonConfig.data.name = scope.model.buttonText;
                        logoutButtonConfig.data.serviceName = "pos.logoutService";
                        logoutButtonConfig.data.serviceAction = "doAction";
                        logoutButtonConfig.data.serviceData = {};
                        logoutButtonConfig.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            }
        }];
});