//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.loginButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.loginButtonSettings'
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope',
        function (initOptions, screenService, styleService, rootScope) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/loginButton/loginButtonSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {};
                    scope.parentCss = styleService.getGlobalCssData().button.data.loginFunction.data || {};
                    if (scope.area && scope.area.data) {
                        scope.model.buttonText = scope.area.data.name;
                    }
                    scope.save = function () {
                        var loginButtonConfig = scope.area;
                        loginButtonConfig.data = {};
                        loginButtonConfig.data.name = scope.model.buttonText;
                        loginButtonConfig.data.serviceName = "pos.loginService";
                        loginButtonConfig.data.serviceAction = "doAction";
                        loginButtonConfig.data.serviceData = {};
                        loginButtonConfig.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            }
        }];
});