//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.splitOrderButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.splitOrderButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService',  'pos.designer.styleService', function (rootScope, initOptions, screenService, styleService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/splitOrderButton/splitOrderButtonSettings.html',
            link: function (scope, el, attrs) {
                if (scope.modelOptions.showDesignFirst) {
                    scope.activeTab = 1;
                }
                scope.model = {};
                scope.parentCss = styleService.getGlobalCssData().button.data.splitOrderFunction.data || {};
                scope.screens = screenService.getScreens();
                scope.grids = [];

                scope.selectScreen = function () {
                    scope.grids = Enumerable.From(scope.model.screen.data).Where(function (x) {
                        return x.name == "pos.ui.elements.grid.grid-directive" && x.data && x.data.service && x.data.service.name == "pos.chooseOrderItemGridService";
                    }).ToArray();
                };

                var areaConfig = scope.area;
                if (areaConfig && areaConfig.data) {
                    scope.model.buttonText = areaConfig.data.name;
                }
                scope.save = function () {
                    var buttonConfig = scope.area;
                    buttonConfig.data = {};
                    buttonConfig.data.name = scope.model.buttonText;
                    buttonConfig.data.serviceName = "pos.splitOrderButtonService";
                    buttonConfig.data.serviceAction = "doAction";
                    buttonConfig.data.serviceData = {
                        screenName: scope.model.screen.name,
                        chooseOrderItemGridFrom: scope.model.chooseOrderItemGridFrom.data.id,
                        chooseOrderItemGridTo: scope.model.chooseOrderItemGridTo.data.id
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