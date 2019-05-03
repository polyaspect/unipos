//# sourceURL=pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.productsPerCategory.js
define([
    'angular',
    "css!pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.productsPerCategory"
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope', 'pos.productService',
        function (initOptions, screenService, styleService, rootScope, productService) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/panelDirectiveSettings/panelTypes/product/types/productsPerCategory/productsPerCategoryTypeProductPanelSettings.html',
                link: function (scope, el, attrs) {
                    var productPanel = scope.area;
                    scope.model = {};
                    scope.categoryDirectiveIds = screenService.getAreaByDirAndService("pos.ui.elements.panel.panel-directive","pos.categoryPanelService");
                    if (productPanel && productPanel.data && productPanel.data.id) {
                        scope.model.id = productPanel.data.id;
                        scope.model.rows = productPanel.data.rows;
                        scope.model.columns = productPanel.data.columns;
                        scope.model.categoryDirectiveId = productPanel.data.categoryDirectiveId;
                    }
                    scope.save = function () {
                        var productPanel = scope.area;
                        if (!productPanel.data) {
                            productPanel.data = {};
                        }
                        productPanel.data.id = scope.model.id;
                        productPanel.data.rows = scope.model.rows;
                        productPanel.data.columns = scope.model.columns;
                        productPanel.data.categoryDirectiveId = scope.model.directive.data.id;
                        productPanel.data.service = {};
                        productPanel.data.service.name = "pos.productPanelService";
                        productPanel.data.service.data = "getProductsPerCategory";
                        productPanel.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    };
                }
            }
        }];
});