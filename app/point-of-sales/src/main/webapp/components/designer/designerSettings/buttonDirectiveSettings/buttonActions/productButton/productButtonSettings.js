//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.productButtonSettings.js
define([
        'angular',
        'css!pos.designer.designerSettings.buttonDirectiveSettings.productButtonSettings'
    ], function (angular) {
        return ['pos.initOptions', 'pos.productService', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope',
            function (initOptions, productService, screenService, styleService, rootScope) {
                return {
                    restrict: 'E',
                    replace: true,
                    scope: false,
                    templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/productButton/productButtonSettings.html',
                    link: function (scope, el, attrs) {
                        if (scope.modelOptions.showDesignFirst) {
                            scope.activeTab = 1;
                        }
                        scope.model = {};
                        scope.items = [];
                        scope.parentCss = styleService.getGlobalCssData().button.data.productFunction.data || {};
                        angular.forEach(angular.copy(productService.getAllProducts()), function (value, index) {
                            value.css = {};
                            value.css.button = {};
                            value.css.text = {};
                            const productButtonConfig = scope.area;
                            value.checked = !!(productButtonConfig.data && productButtonConfig.data.serviceData && value.id == productButtonConfig.data.serviceData.id);
                            scope.items.push(value);
                        });
                        if (scope.area && scope.area.data) {
                            scope.model.product = Enumerable.From(scope.items).FirstOrDefault(undefined, function (x) {
                                return x.id == scope.area.data.serviceData.id;
                            });
                        }
                        scope.save = function () {
                            let productButtonConfig = scope.area;
                            productButtonConfig.data = {};
                            productButtonConfig.data.name = scope.model.product.name;
                            productButtonConfig.data.serviceName = "pos.productService";
                            productButtonConfig.data.serviceAction = "doAction";
                            productButtonConfig.data.serviceData = {
                                "id": scope.model.product.id,
                                "categoryId": scope.model.product.category.id
                            };
                            productButtonConfig.render = true;

                            scope.$broadcast("saveStyle-" + scope.area.uuid);
                            rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                            scope.hide();
                        }
                    }
                }
            }
        ]
            ;
    }
)
;