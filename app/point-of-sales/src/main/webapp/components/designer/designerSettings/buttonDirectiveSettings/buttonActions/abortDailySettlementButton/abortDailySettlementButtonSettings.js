//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.abortDailySettlementButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.abortDailySettlementButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService',
        function (rootScope, initOptions, screenService, styleService) {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/abortDailySettlementButton/abortDailySettlementButtonSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {
                        buttonText: "",
                        selectedScreen: {}
                    };
                    scope.parentCss = styleService.getGlobalCssData().button.data.abortDailySettlementFunction.data || {};
                    scope.screens = screenService.getScreens();
                    if (scope.area && scope.area.data) {
                        scope.model.buttonText = scope.area.data.name;
                        scope.model.selectedScreen = Enumerable.From(scope.screens).FirstOrDefault(undefined, function (x) {
                            return x.name == scope.area.data.serviceData.screenName;
                        });
                    }
                    scope.save = function () {
                        var areaConfig = scope.area;
                        areaConfig.data = {};
                        areaConfig.data.name = scope.model.buttonText;
                        areaConfig.data.serviceName = "pos.reportService";
                        areaConfig.data.serviceAction = "abortDailySettlement";
                        areaConfig.data.serviceData = {
                            "screenName": scope.model.selectedScreen.name,
                            "monthly": scope.model.monthly
                        };
                        areaConfig.render = true;

                        //NEW
                        var elementType = "abortDailySettlementButton";
                        areaConfig.data.legacy = false;
                        areaConfig.data.uuid = scope.area.uuid;
                        areaConfig.data.elementType = elementType;
                        areaConfig.data.metaData = {
                            screenName: scope.model.selectedScreen.name,
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