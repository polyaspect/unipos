//# sourceURL=pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.products.js
define([
    'angular',
    "css!pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.type.products"
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope', 'pos.productService',
        function (initOptions, screenService, styleService, rootScope, productService) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/panelDirectiveSettings/panelTypes/product/types/products/productTypeProductPanelSettings.html',
                link: function (scope, el, attrs) {
                    scope.model = {};
                    var productPanel = scope.area;
                    scope.items = angular.copy(productService.getAllProducts());
                    if (productPanel && productPanel.data && productPanel.data.id) {
                        scope.model.id = productPanel.data.id;
                        scope.model.rows = productPanel.data.rows;
                        scope.model.columns = productPanel.data.columns;
                        scope.model.allProducts = productPanel.data.service.data == "getAllProducts";
                        if (!scope.items) {
                            scope.items = [];
                        }
                        scope.model.products = [];
                        Enumerable.From(productPanel.data.service.productsIds).ForEach(function (x) {
                            scope.model.products.push(Enumerable.From(scope.items).FirstOrDefault(undefined, function (y) {
                               return x == y.guid;
                            }));
                        });
                    }


                    scope.selectSourceItem = function (item) {
                        if (!scope.model.allProducts) {
                            item.checked = !item.checked;
                        }
                    };
                    scope.selectAllProducts = function () {
                        angular.forEach(scope.items, function (value, index) {
                            value.checked = false;
                        });
                        scope.model.allProducts = !scope.model.allProducts;
                    };
                    scope.save = function () {
                        var productPanel = scope.area;
                        if (!productPanel.data) {
                            productPanel.data = {};
                        }
                        productPanel.data.id = scope.model.id;
                        productPanel.data.rows = scope.model.rows;
                        productPanel.data.columns = scope.model.columns;
                        productPanel.data.service = {};
                        productPanel.data.service.name = "pos.productPanelService";
                        productPanel.data.service.data = scope.model.allProducts ? "getAllProducts" : "getProducts";
                        productPanel.data.service.productsIds = Enumerable.From(scope.model.products).Select(function (x) {
                            return x.guid;
                        }).ToArray();
                        productPanel.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    };
                }
            }
        }];
});