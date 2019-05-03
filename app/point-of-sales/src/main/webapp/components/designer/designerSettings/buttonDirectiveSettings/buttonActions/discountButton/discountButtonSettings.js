//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.discountButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.discountButtonSettings'
], function (angular) {
    return ['pos.initOptions', 'pos.productService', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope', 'pos.discountService',
        function (initOptions, productService, screenService, styleService, rootScope, discountService) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/discountButton/discountButtonSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {
                        orderOrProduct: false,
                        percentOrFixed: false,
                        fixedValue: false,
                        discountValue: 0,
                        discount: undefined,
                        orderDirective: undefined,
                    };
                    scope.discounts = angular.copy(discountService.getDiscounts());
                    var discountButtonConfig = scope.area;
                    if (discountButtonConfig && discountButtonConfig.data) {
                        scope.model.name = discountButtonConfig.data.name;
                        scope.model.orderOrProduct = discountButtonConfig.data.serviceData.discountType == "ORDER" ? "true" : "false";
                        scope.model.percentOrFixed = discountButtonConfig.data.serviceData.discountValueType == "PERCENT" ? "true" : "false";
                        scope.model.discountValue = discountButtonConfig.data.serviceData.discountValue;
                        scope.model.fixedValue = discountButtonConfig.data.serviceData.discountIsFixed;
                        var discount = Enumerable.From(scope.discounts).FirstOrDefault(undefined, function (x) {
                            return x.guid == discountButtonConfig.data.serviceData.discountId;
                        });
                        if (discount) {
                            discount.checked = true
                        }
                    }
                    scope.parentCss = styleService.getGlobalCssData().button.data.discountFunction.data || {};
                    scope.orderDirectiveIds = screenService.getAreaByDirAndService("pos.ui.elements.grid.grid-directive", "pos.orderDirectiveService");

                    scope.save = function () {
                        var discountButtonConfig = scope.area;
                        discountButtonConfig.data = {};
                        discountButtonConfig.data.name = scope.model.name;
                        discountButtonConfig.data.serviceName = "pos.discountService";
                        discountButtonConfig.data.serviceAction = "doAction";

                        discountButtonConfig.data.serviceData = {
                            "discountType": scope.model.orderOrProduct == "true" ? "ORDER" : "PRODUCT",
                            "discountValueType": scope.model.percentOrFixed == "true" ? "PERCENT" : "VALUE",
                            "discountValue": scope.model.discountValue,
                            "discountIsFixed": scope.model.fixedValue,
                            "orderDirectiveId": scope.model.orderDirective.data.id,
                            "discountId": scope.model.discount.guid
                        };
                        discountButtonConfig.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            };
        }
    ];
});