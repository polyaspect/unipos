//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.paymentButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.paymentButtonSettings'
], function (angular) {
    return ['pos.initOptions', 'pos.paymentService', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope',
        function (initOptions, paymentService, screenService, styleService, rootScope) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/paymentButton/paymentButtonSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {};
                    paymentService.getPaymentMethods().then(function (result) {
                        scope.items = result;
                        if (scope.area && scope.area.data) {
                            scope.model.paymentMethod = Enumerable.From(scope.items).FirstOrDefault(undefined, function (x) {
                                return x.guid == scope.area.data.serviceData.paymentId;
                            });
                        }
                    });
                    scope.parentCss = styleService.getGlobalCssData().button.data.paymentFunction.data || {};
                    if (scope.area && scope.area.data) {
                        scope.model.paymentMethod = Enumerable.From(scope.items).FirstOrDefault(undefined, function (x) {
                            return x.guid == scope.area.data.serviceData.paymentId;
                        });
                    }
                    scope.save = function () {
                        var paymentButtonConfig = scope.area;
                        paymentButtonConfig.data = {};
                        paymentButtonConfig.data.name = scope.model.paymentMethod.name;
                        paymentButtonConfig.data.serviceName = "pos.paymentService";
                        paymentButtonConfig.data.serviceAction = "addPaymentOrderItem";
                        paymentButtonConfig.data.serviceData = {
                            "paymentId": scope.model.paymentMethod.guid
                        };
                        paymentButtonConfig.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            }
        }];
});