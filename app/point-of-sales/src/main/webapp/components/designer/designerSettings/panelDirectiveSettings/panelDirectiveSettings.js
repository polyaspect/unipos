//# sourceURL=pos.design.panelDirectiveSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.panelDirectiveSettings"
], function (angular) {
    return ['$scope', '$uibModalInstance', 'pos.categoryService', 'pos.designer.screenService', '$compile', '$timeout', 'area', 'options',
        function (scope, uibModalInstance, categoryService, screenService, $compile, timeout, area, options) {
            scope.modelOptions = options;
            scope.area = area;
            scope.options = {
                showTypes: true
            };
            scope.types = [{
                text: "Produkte",
                name: "pos.productPanelService",
                settingsDirective: 'pos.designer.designer-settings.panel-directive-settings.product-panel-settings'
            }, {
                text: "Numpad",
                name: "pos.numpadPanelService",
                action: "getNumpadData",
                settingsDirective: 'pos.designer.designer-settings.panel-directive-settings.numpad-panel-settings'
            }, {
                text: "Tastatur",
                name: "pos.numpadPanelService",
                action: "getKeyboardData",
                settingsDirective: 'pos.designer.designer-settings.panel-directive-settings.keyboard-panel-settings'
            }, {
                text: "Category",
                name: "pos.categoryPanelService",
                settingsDirective: 'pos.designer.designer-settings.panel-directive-settings.category-panel-settings'
            }];

            scope.selectType = function (item) {
                scope.options.showTypes = false;
                let tabs = $compile('<' + item.settingsDirective + "></" + item.settingsDirective + '>')(scope);
                angular.element(".panel-directive-settings .directive-tabs").append(tabs);
            };
            if (scope.area && scope.area.data && scope.area.data.service) {
                var action = Enumerable.From(scope.types).FirstOrDefault(undefined, function (x) {
                    return x.name == scope.area.data.service.name && (!x.action || (x.action && x.action == scope.area.data.service.data));
                });

                timeout(function () {
                    scope.selectType(action);
                }, 10);
            }
            scope.hide = function () {
                uibModalInstance.close();
            };
        }];
});