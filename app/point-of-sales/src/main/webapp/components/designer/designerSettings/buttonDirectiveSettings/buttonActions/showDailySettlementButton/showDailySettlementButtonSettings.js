//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.showDailySettlementButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.showDailySettlementButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', function (rootScope, initOptions, screenService, styleService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/showDailySettlementButton/showDailySettlementButtonSettings.html',
            link: function (scope, el, attrs) {
                if (scope.modelOptions.showDesignFirst) {
                    scope.activeTab = 1;
                }
                scope.model = {};
                scope.screens = screenService.getScreens();
                scope.grids = [];
                scope.parentCss = styleService.getGlobalCssData().button.data.showDailySettlementFunction.data || {};
                scope.selectScreen = function () {
                    scope.grids = Enumerable.From(scope.model.screen.data).Where(function (x) {
                        return x.name == "pos.ui.elements.grid.grid-directive" && x.data && x.data.service && x.data.service.name == "pos.reportPreviewGridService";
                    }).ToArray();
                };
                if (scope.area && scope.area.data) {
                    scope.model.screen = Enumerable.From(scope.screens).FirstOrDefault(undefined, function (x) {
                        return x.name == scope.area.data.serviceData.screenName;
                    });
                    scope.selectScreen();
                    scope.model.reportDirective = Enumerable.From(scope.grids).FirstOrDefault(undefined, function (x) {
                        return x.data.id == scope.area.data.serviceData.reportPreviewDirective;
                    });
                }

                scope.save = function () {
                    var elementType = "showDailySettlementButton";
                    var config = scope.area;

                    config.render = true;
                    config.data = {};
                    config.data.name = scope.model.buttonText;

                    //LEGACY:
                    config.data.serviceName = "pos.reportService";
                    config.data.serviceAction = "fillDailySettlementData";
                    config.data.serviceData = {
                        screenName: scope.model.screen.name,
                        reportPreviewDirective: scope.model.reportDirective.data.id,
                        "monthly": scope.model.monthly
                    };

                    //NEW
                    config.data.legacy = false;
                    config.data.uuid = scope.area.uuid;
                    config.data.elementType = elementType;
                    config.data.metaData = {
                        screenName: scope.model.screen.name,
                        "monthly": scope.model.monthly
                    };

                    scope.$broadcast("saveStyle-" + scope.area.uuid);
                    rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                    scope.hide();
                };
            }
        }
    }
    ];
});