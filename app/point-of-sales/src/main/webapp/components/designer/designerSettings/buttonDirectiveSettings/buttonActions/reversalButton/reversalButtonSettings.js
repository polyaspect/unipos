//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.reversalButtonSettings.js
define([
    'angular',
    'pos.linq',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.reversalButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', function (rootScope, initOptions, screenService, styleService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/reversalButton/reversalButtonSettings.html',
            link: function (scope, el, attrs) {
                if (scope.modelOptions.showDesignFirst) {
                    scope.activeTab = 1;
                }
                scope.model = {};
                scope.parentCss = styleService.getGlobalCssData().button.data.reversalFunction.data || {};
                scope.orderDirectiveIds = screenService.getAreaByDirAndService("pos.ui.elements.grid.grid-directive", "pos.orderDirectiveService");

                const areaConfig = scope.area;
                if (areaConfig && areaConfig.data) {
                    scope.model.orderDirective = Enumerable.From(scope.orderDirectiveIds).FirstOrDefault(undefined, function (x) {
                        return x.data.id == areaConfig.data.serviceData.orderDirectiveId
                    });
                }
                scope.save = function () {
                    var reversalButtonConfig = scope.area;
                    reversalButtonConfig.data = {};
                    reversalButtonConfig.data.name = "Storno";
                    reversalButtonConfig.data.serviceName = "pos.reversalService";
                    reversalButtonConfig.data.serviceAction = "doAction";
                    reversalButtonConfig.data.serviceData = {
                        "orderDirectiveId": scope.model.orderDirective.data.id
                    };
                    reversalButtonConfig.render = true;

                    scope.$broadcast("saveStyle-" + scope.area.uuid);
                    rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                    scope.hide();
                };
                if (scope.orderDirectiveIds.length == 1) {
                    scope.model.orderDirective = scope.orderDirectiveIds[0];
                }
            }
        }
    }
    ];
});