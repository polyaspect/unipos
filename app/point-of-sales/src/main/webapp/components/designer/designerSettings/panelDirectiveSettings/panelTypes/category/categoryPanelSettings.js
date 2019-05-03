//# sourceURL=pos.designer.designerSettings.panelDirectiveSettings.categoryPanelSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.panelDirectiveSettings.categoryPanelSettings"
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope', 'pos.categoryService',
        function (initOptions, screenService, styleService, rootScope, categoryService) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/panelDirectiveSettings/panelTypes/category/categoryPanelSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {};
                    scope.parentCss = styleService.getGlobalCssData().panel.data.category.data || {};
                    scope.items = angular.copy(categoryService.getCategories());
                    var categoryPanel = scope.area;
                    if (categoryPanel && categoryPanel.data && categoryPanel.data.id) {
                        scope.model.id = categoryPanel.data.id;
                        scope.model.rows = categoryPanel.data.rows;
                        scope.model.columns = categoryPanel.data.columns;
                        scope.model.allCategories = categoryPanel.data.service.data == "getAllCategories";
                        if (!scope.items) {
                            scope.items = [];
                        }
                        Enumerable.From(categoryPanel.data.service.categoryIds).ForEach(function (x) {
                           Enumerable.From(scope.items).FirstOrDefault(undefined, function (y) {
                               return y.guid == x;
                           }).checked = true;
                        });
                    }

                    scope.selectSourceItem = function (item) {4
                        if(!scope.model.allCategories){
                            item.checked = !item.checked;
                        }
                    };
                    scope.selectAll = function () {
                        scope.model.allCategories = !scope.model.allCategories;
                        angular.forEach(scope.items, function (value, index) {
                            value.checked = false;
                        });
                    };
                    scope.save = function () {
                        var categoryPanel = scope.area;
                        if (!categoryPanel.data) {
                            categoryPanel.data = {};
                        }
                        categoryPanel.data.id = scope.model.id;
                        categoryPanel.data.rows = scope.model.rows;
                        categoryPanel.data.columns = scope.model.columns;
                        categoryPanel.data.service = {};
                        categoryPanel.data.service.name = "pos.categoryPanelService";
                        categoryPanel.data.service.data = scope.model.allCategories ? "getAllCategories" : "getCategories";
                        categoryPanel.data.service.categoryIds = Enumerable.From(scope.items).Where(function (x) {
                            return x.checked;
                        }).Select(function (x) {
                            return x.guid;
                        }).ToArray();
                        categoryPanel.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    };
                }
            }
        }];
});