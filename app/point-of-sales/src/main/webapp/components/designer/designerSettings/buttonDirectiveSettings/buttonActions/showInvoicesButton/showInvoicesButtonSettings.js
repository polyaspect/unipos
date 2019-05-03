//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.showInvoicesButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.showInvoicesButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', function (rootScope, initOptions, screenService, styleService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/showInvoicesButton/showInvoicesButtonSettings.html',
            link: function (scope, el, attrs) {
                if (scope.modelOptions.showDesignFirst) {
                    scope.activeTab = 1;
                }
                scope.model = {};
                scope.parentCss = styleService.getGlobalCssData().button.data.showInvoicesFunction.data || {};
                scope.screens = screenService.getScreens();
                scope.grids = [];
                if (scope.area && scope.area.data) {
                    scope.model.screen = Enumerable.From(scope.screens).FirstOrDefault(undefined, function (x) {
                        return x.name == scope.area.data.serviceData.screenName;
                    });
                    scope.selectScreen();
                    scope.model.directive = Enumerable.From(scope.grids).FirstOrDefault(undefined, function (x) {
                        return x.data.id == scope.area.data.serviceData.showInvoicesGrid;
                    });
                }
                scope.selectScreen = function () {
                    scope.grids = Enumerable.From(scope.model.screen.data).Where(function (x) {
                        return x.name == "pos.ui.elements.grid.grid-directive" && x.data && x.data.service && x.data.service.name == "pos.showInvoicesGridService";
                    }).ToArray();
                };
                scope.save = function () {
                    var showInvoicesButtonConfig = scope.area;
                    showInvoicesButtonConfig.data = {};
                    showInvoicesButtonConfig.data.name = scope.model.buttonText;
                    showInvoicesButtonConfig.data.serviceName = "pos.showInvoicesService";
                    showInvoicesButtonConfig.data.serviceAction = "doAction";
                    showInvoicesButtonConfig.data.serviceData = {
                        screenName: scope.model.screen.name,
                        showInvoicesGrid: scope.model.directive.data.id
                    };
                    showInvoicesButtonConfig.render = true;

                    scope.$broadcast("saveStyle-" + scope.area.uuid);
                    rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                    scope.hide();
                };
            }
        }
    }
    ];
});