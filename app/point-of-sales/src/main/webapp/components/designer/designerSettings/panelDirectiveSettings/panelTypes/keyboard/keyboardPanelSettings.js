//# sourceURL=pos.designer.designerSettings.panelDirectiveSettings.keyboardPanelSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.panelDirectiveSettings.keyboardPanelSettings"
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope', 'pos.productService', 'pos.designer.designerSettings.designerSettingsService',
        function (initOptions, screenService, styleService, rootScope, productService, designerSettingsService) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/panelDirectiveSettings/panelTypes/keyboard/keyboardPanelSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {};
                    scope.modes = angular.copy(designerSettingsService.getDataByDirectiveAndKey(designerSettingsService.textController, "modes"));
                    var productPanel = scope.area;
                    if (productPanel && productPanel.data) {
                        scope.model.panelId = productPanel.data.id;
                        if (productPanel.data.service) {
                            scope.model.enterButton = productPanel.data.service.enterButton;
                        }
                        angular.forEach(scope.modes, function (item1, index) {
                            angular.forEach(productPanel.data.service.modes, function (item2, index2) {
                                if (item1.name == item2.name) {
                                    item1.checked = true;
                                }
                            });
                        });
                    }
                    scope.parentCss = styleService.getGlobalCssData().panel.data.keyboard.data || {};

                    scope.selectSourceItem = function (item) {
                        item.checked = !item.checked;
                    };
                    scope.save = function () {
                        var productPanel = scope.area;
                        if (!productPanel.data) {
                            productPanel.data = {};
                        }
                        productPanel.data.id = scope.model.panelId;
                        productPanel.data.columns = 14;
                        productPanel.data.rows = 4;
                        productPanel.data.service = {};
                        productPanel.data.service.name = "pos.numpadPanelService";
                        productPanel.data.service.data = "getKeyboardData";
                        productPanel.data.service.enterButton = scope.model.enterButton;
                        productPanel.data.service.modes = Enumerable.From(scope.modes).Where(function (x) {
                            return x.checked;
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