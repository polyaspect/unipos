//# sourceURL=pos.designer.designerSettings.gridDirectiveSettings.reportPreviewGridSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.gridDirectiveSettings.reportPreviewGridSettings"
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope', 'uuid2',
        function (initOptions, screenService, styleService, rootScope, uuid2) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/gridDirectiveSettings/gridTypes/reportPreviewGrid/reportPreviewGridSettings.html',
                link: function (scope, el, attrs) {
                    scope.roles= [{key: "dailySettlement", text: "Tagesabschluss"}];

                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {};

                    //wenn, dann über elementType:
                    //scope.parentCss = styleService.getGlobalCssData().jkGrid.data.reportPreviewGrid.data || {};

                    if (scope.area && scope.area.data) {
                        scope.model.gridId = scope.area.data.id;
                    }
                    scope.save = function () {
                        var elementType = "reportPreviewGrid";
                        var config = scope.area;

                        config.render = true;
                        config.data = {};

                        //LEGACY:
                        config.data.id = scope.model.gridId;
                        // --> Auf andere Elements darf nicht mehr referenziert werden, id wird automatisch generiert

                        config.data.service = {};
                        // --> alle subitems werden ersetzt

                        config.data.service.name = "pos.reportPreviewGridService";
                        // --> wird ersetzt durch ViewController, der Service-Name wird per Konvention gebaut

                        config.data.service.columnDefs = "getColumnDefs";
                        // --> wird standardmaesig zu getColumnDefs

                        config.data.service.data = "getData";
                        // --> wird zu getViewData

                        config.data.service.eventName = "pos.reportPreviewGridService-" + scope.gridId;
                        // --> element update event wird automatisch zu "updateDataSource:" + uuid

                        //NEW:
                        config.data.legacy = false;
                        config.data.uuid = scope.area.uuid;
                        config.data.elementType = elementType;
                        config.data.roles = Enumerable.From(scope.roles).Where(function (x) {
                            return x.checked;
                        }).Select(function (x) {
                            return {
                                key: x.key
                            };
                        }).ToArray();

                        scope.$broadcast("saveStyle-" + scope.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            }
        }];
});