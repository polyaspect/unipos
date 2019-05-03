//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.reversalButtonSettings.js
define([
    'angular',
    'pos.linq',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.reprintInvoiceButtonSettings'
], function (angular) {
    return ['$rootScope', 'pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', function (rootScope, initOptions, screenService, styleService) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/reprintInvoiceButton/reprintInvoiceButtonSettings.html',
            link: function (scope, el, attrs) {
                if (scope.modelOptions.showDesignFirst) {
                    scope.activeTab = 1;
                }
                scope.model = {};
                scope.parentCss = styleService.getGlobalCssData().button.data.reprintInvoiceFunction.data || {};
                scope.invoiceGrids = screenService.getAreaByDirAndService("pos.ui.elements.grid.grid-directive", "pos.showInvoicesGridService");

                if (scope.area && scope.area.data) {
                    scope.model.buttonText = scope.area.data.name;
                }
                scope.save = function () {
                    var reversalButtonConfig = scope.area;
                    reversalButtonConfig.data = {};
                    reversalButtonConfig.data.name = scope.model.buttonText;
                    reversalButtonConfig.data.serviceName = "pos.reprintInvoiceButtonService";
                    reversalButtonConfig.data.serviceAction = "doAction";
                    reversalButtonConfig.data.serviceData = {
                        "invoiceGridId": scope.model.invoiceGrid.data.id
                    };
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