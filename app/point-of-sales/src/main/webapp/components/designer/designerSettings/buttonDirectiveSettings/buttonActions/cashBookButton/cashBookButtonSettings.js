//# sourceURL=pos.designer.designerSettings.buttonDirectiveSettings.cashBookButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.cashBookButtonSettings'
], function (angular) {
    return ['pos.initOptions', 'pos.productService', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope', 'pos.discountService',
        function (initOptions, productService, screenService, styleService, rootScope, discountService) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/cashBookButton/cashBookButtonSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {
                        buttonText: "",
                        options: {
                            "in": true,
                            "private": true,
                            steps: []
                        }
                    };
                    scope.parentCss = styleService.getGlobalCssData().button.data.cashBookFunction.data || {};
                    scope.screens = screenService.getScreens();
                    scope.allSteps = [{
                        name: "Betrag eingeben", state: "CASHBOOK_AMOUNT_INPUT"
                    }, {
                        name: "Grund eingeben", state: "CASHBOOK_DESCRIPTION_INPUT"
                    }, {
                        name: "Abschluss", state: "OPENED_ORDER"
                    }];
                    if (scope.area && scope.area.data) {
                        scope.model.buttonText = scope.area.data.name;
                        scope.model.options = angular.copy(scope.area.data.serviceData.options);
                        //scope.model.options.in = scope.area.data.serviceData.options.in ? "true" : "false";
                        //scope.model.options.private = scope.area.data.serviceData.options.private ? "true" : "false";
                    }
                    scope.deleteStep = function (index) {
                        scope.model.options.steps.splice(index, 1);
                    };
                    scope.addStep = function (step) {
                        var any = Enumerable.From(scope.model.options.steps).Any(function (x) {
                            return x.name == step.name;
                        });
                        if (!any) {
                            scope.model.options.steps.push(angular.copy(step));
                        }
                    };
                    scope.save = function () {
                        var areaConfig = scope.area;
                        areaConfig.data = {};
                        areaConfig.data.name = scope.model.buttonText;
                        areaConfig.data.serviceName = "pos.cashbookEntryService";
                        areaConfig.data.serviceAction = "addCashBookEntry";
                        //scope.model.options.in = scope.model.options.in == "true" ? true : false;
                        //scope.model.options.private = scope.model.options.private == "true" ? true : false;
                        areaConfig.data.serviceData = {
                            options: scope.model.options
                        };
                        areaConfig.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            };
        }
    ];
});