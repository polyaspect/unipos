//# sourceURL=pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.productsSingleCategory.js
define([
    'angular',
    "css!pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.productsSingleCategory"
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope', 'pos.categoryService',
        function (initOptions, screenService, styleService, rootScope, categoryService) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/panelDirectiveSettings/panelTypes/product/types/productsSingleCategory/productsSingleCategoryTypeProductPanelSettings.html',
                link: function (scope, el, attrs) {
                    scope.model = {};
                    var productPanel = scope.area;
                    scope.items = angular.copy(categoryService.getCategories());

                    if (productPanel && productPanel.data && productPanel.data.id) {
                        scope.model.id = productPanel.data.id;
                        scope.model.rows = productPanel.data.rows;
                        scope.model.columns = productPanel.data.columns;
                        scope.model.categoryId = productPanel.data.categoryId;
                    }
                    scope.save = function () {
                        var productPanel = scope.area;
                        if (!productPanel.data) {
                            productPanel.data = {};
                        }
                        productPanel.data.id = scope.model.id;
                        productPanel.data.rows = scope.model.rows;
                        productPanel.data.columns = scope.model.columns;
                        productPanel.data.categoryId = scope.model.categoryId;
                        productPanel.data.service = {};
                        productPanel.data.service.name = "pos.productPanelService";
                        productPanel.data.service.data = "getProductsBySingleCategory";
                        productPanel.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    };
                }
            }
        }];
});