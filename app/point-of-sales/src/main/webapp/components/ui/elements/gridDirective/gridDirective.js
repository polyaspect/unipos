//# sourceURL=pos.gridDirective.js
define([
    'angular',
    'css!pos.ui.elements.grid.gridDirective'
], function (angular) {
    return ['pos.ui.elements.grid.gridDirectiveService', '$rootScope', '$timeout', 'pos.initOptions', 'pos.ui.dataSourceService',
        function (gridDirectiveService, rootScope, timeout, initOptions, dataSourceService) {
        return {
            restrict: 'E',
            templateUrl: initOptions.baseDir + 'components/ui/elements/gridDirective/gridDirective.html',
            replace: true,
            scope: {
                "area": "="
            },
            link: function (scope, el, attrs) {
                // LEGACY:
                rootScope.$on(scope.area.data.service.eventName || (scope.area.data.service.name + "-" + scope.area.data.id), function () {
                    scope.gridOptions.data = gridDirectiveService.getData(scope.area).data;
                    if(scope.gridOptions.data.length > 0){
                        scope.gridApi.grid.modifyRows(scope.gridOptions.data);
                        scope.gridApi.selection.selectRow(scope.gridOptions.data[scope.gridOptions.data.length - 1]);
                    }
                    timeout(function () {
                        angular.element(el).find(".ui-grid-viewport").scrollTop(angular.element(el).find(".ui-grid-canvas").height());
                    }, 1);
                });
                el.addClass(scope.area.data.service.name.replace(".", ""));
                el.addClass(scope.area.data.id);
                el.addClass(scope.area.data.uuid);

                // NEW:
                rootScope.$on("updateDataSource:" + scope.area.data.elementType, function (event, eventRole) {
                    scope.gridOptions.data = gridDirectiveService.getData(scope.area).data;
                    var data = scope.area.data;
                    angular.forEach(data.roles, function (role, index) {
                        if (role.key == eventRole) {
                            scope.gridOptions.data = dataSourceService.getViewData(scope.area.data, "grid", data.elementType, eventRole);
                            timeout(function () {
                                angular.element(el).find(".ui-grid-viewport").scrollTop(angular.element(el).find(".ui-grid-canvas").height());
                            }, 1);
                        }
                    });
                });

                var gridOptions = gridDirectiveService.getData(scope.area);

                var availableWidth = angular.element(el).width() - 73;
                angular.forEach(gridOptions.columnDefs, function(columnDefs){
                    var percent = Number(columnDefs.width.replace("%", ""));
                    columnDefs.width = availableWidth * (percent / 100);
                });

                scope.gridOptions = gridOptions;

                scope.gridOptions.rowTemplate = '<div grid="grid" style="background-color:black"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" ' +
                    'class="ui-grid-cell" style="display:block" ui-grid-cell></div></div>';
                scope.gridOptions.rowTemplate = angular.element(scope.gridOptions.rowTemplate).find("div.ui-grid-cell").css((scope.area.data && scope.area.data.css && scope.area.data.css.cell) || {});
                angular.forEach(scope.gridOptions.columnDefs, function (item, index) {
                    if (!item.cellTemplate) {
                        item.cellTemplate = '<div class="ui-grid-row-text" ng-style="row.grid.selection.lastSelectedRow.entity.value.index == row.entity.value.index ? ' +
                            'row.entity.value.css.selectedRow:row.entity.value.css.rows" ng-click="grid.appScope.cellClicked(row,col)">{{COL_FIELD CUSTOM_FILTERS}}</div>';
                    }

                });
                scope.cellClicked = function (row, col){
                    gridDirectiveService.cellClick(scope.area, row, col);
                };
                scope.gridOptions.onRegisterApi = function (gridApi) {
                    scope.gridApi = gridApi;
                    if (rootScope.jkGridApi == undefined) {
                        rootScope.jkGridApi = {};
                    }
                    if (rootScope.jkGridApi[scope.area.data.id] == undefined) {
                        rootScope.jkGridApi[scope.area.data.id] = {};
                    }
                    rootScope.jkGridApi[scope.area.data.id] = gridApi;
                };
            }
        };
    }];
});