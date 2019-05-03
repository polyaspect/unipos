//# sourceURL=pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.panelDirectiveSettings.productPanelSettings"
], function (angular) {
    return ['$timeout', 'pos.initOptions', '$compile', 'pos.designer.screenService', 'pos.designer.styleService',
        function (timeout, initOptions, compile, screenService, styleService) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/panelDirectiveSettings/panelTypes/product/productPanelSettings.html',
                link: function (scope, el, attrs) {
                    scope.childOptions = {
                        showTypes: true
                    };
                    scope.parentCss = styleService.getGlobalCssData().panel.data.product.data || {};
                    scope.typeSelected = false;
                    scope.types = [{
                        name: "Produkte",
                        directive: "pos.designer.designer-settings.panel-directive-settings.product-panel-settings.type.products"
                    }, {
                        name: "Produkte einer Warengruppe",
                        directive: "pos.designer.designer-settings.panel-directive-settings.product-panel-settings.type.products-Single-category"
                    }, {
                        name: "Produkte anhand Warengruppen",
                        directive: "pos.designer.designer-Settings.panel-Directive-Settings.product-Panel-Settings.type.products-per-Category"
                    }];
                    scope.selectType = function (type) {
                        var newDirective = compile("<" + type.directive + "></" + type.directive + ">")(scope);
                        angular.element(".product-panel-settings .directive-tabs").html(newDirective);
                        scope.childOptions.showTypes = false;
                    };
                }
            };
        }
    ];
});