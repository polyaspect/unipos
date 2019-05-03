//# sourceURL=pos.design.designerLiveItemDirective.js
define([
    'angular',
    'css!pos.designer.designerLiveviewDirective',
    "pos.design.jqueryUi"
], function (angular) {
    return ['$rootScope', 'pos.designer.screenService', 'pos.initOptions', '$timeout', '$compile', '$routeParams', '$uibModal',
        function (rootScope, screenService, initOptions, timeout, compile, routeParams, uibModal) {
            return {
                restrict: 'E',
                templateUrl: initOptions.baseDir + 'components/designer/designerLiveview/designerLiveviewDirective.html',
                replace: true,
                scope: {
                    area: '=',
                    liveviewSize: '=',
                    selected: '='
                },
                link: function (scope, el, attrs) {
                    scope.css = {
                        width: scope.liveviewSize.width.replace('px', '') / 5 + 'px',
                        height: scope.liveviewSize.height.replace('px', '') / 10 + 'px'
                    };
                    scope.$on("$destroy", function () {
                        renderEvent();
                        selectEvent();
                    });
                    var renderEvent = rootScope.$on(screenService.serviceName + "-" + scope.area.uuid, function () {
                        if (scope.area.render) {
                            el.draggable({
                                grid: [scope.liveviewSize.width.replace('px', '') / 100, scope.liveviewSize.height.replace('px', '') / 100],
                                containment: ".designer-live-view",
                                stop: function (event, ui) {
                                    scope.area.posX = Math.round((ui.position.left / scope.liveviewSize.width.replace('px', '')) * 100);
                                    scope.area.posY = Math.round((ui.position.top / scope.liveviewSize.height.replace('px', '')) * 100);
                                    rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);
                                }
                            }).resizable({
                                handles: 'ne, se, sw, nw',
                                grid: [scope.liveviewSize.width.replace('px', '') / 100, scope.liveviewSize.height.replace('px', '') / 100],
                                containment: ".designer-live-view",
                                stop: function (event, ui) {
                                    scope.area.width = Math.round((ui.size.width / scope.liveviewSize.width.replace('px', '')) * 100);
                                    scope.area.height = Math.round((ui.size.height / scope.liveviewSize.height.replace('px', '')) * 100);
                                    scope.area.posX = Math.round((ui.position.left / scope.liveviewSize.width.replace('px', '')) * 100);
                                    scope.area.posY = Math.round((ui.position.top / scope.liveviewSize.height.replace('px', '')) * 100);
                                    rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);
                                }
                            });

                            scope.css.left = (scope.area.posX) * (scope.liveviewSize.width.replace('px', '') / 100) + "px";
                            scope.css.top = (scope.area.posY) * (scope.liveviewSize.height.replace('px', '') / 100) + "px";
                            scope.css.width = (scope.area.width) * (scope.liveviewSize.width.replace('px', '') / 100) + "px";
                            scope.css.height = (scope.area.height) * (scope.liveviewSize.height.replace('px', '') / 100) + "px";
                            el.css("background-color", "transparent");
                            var newDirective = compile("<" + scope.area.name + " area='area'></" + scope.area.name + ">")(scope);
                            el.find("> .content").html(newDirective);
                        }
                    });
                    var selectEvent = rootScope.$on(screenService.serviceName+"-area-selected", function () {
                        if (scope.area == scope.selected.area) {
                            scope.css.border = '2px dotted black';
                        }else{
                            scope.css.border = '1px dotted black';
                        }
                        scope.css.left = (scope.area.posX) * (scope.liveviewSize.width.replace('px', '') / 100) + "px";
                        scope.css.top = (scope.area.posY) * (scope.liveviewSize.height.replace('px', '') / 100) + "px";
                        scope.css.width = (scope.area.width) * (scope.liveviewSize.width.replace('px', '') / 100) + "px";
                        scope.css.height = (scope.area.height) * (scope.liveviewSize.height.replace('px', '') / 100) + "px";
                    });
                    scope.edit = function (ev) {
                        uibModal.open({
                            ariaLabelledBy: 'modal-title',
                            ariaDescribedBy: 'modal-body',
                            templateUrl: initOptions.baseDir + 'components/designer/designerSettings/' + attrs.$normalize(scope.area.settingsName) + "Settings/" + attrs.$normalize(scope.area.settingsName) + "Settings.html",
                            controller: "pos.designer.designerSettings." + attrs.$normalize(scope.area.settingsName) + "Settings",
                            size: 'lg',
                            appendTo: angular.element("body"),
                            resolve: {area: scope.area},
                            windowClass: 'grid-directive-settings-modal'
                        });
                    };
                    el.draggable({
                        grid: [scope.liveviewSize.width.replace('px', '') / 100, scope.liveviewSize.height.replace('px', '') / 100],
                        containment: ".designer-live-view",
                        stop: function (event, ui) {
                            scope.area.posX = Math.round((ui.position.left / scope.liveviewSize.width.replace('px', '')) * 100);
                            scope.area.posY = Math.round((ui.position.top / scope.liveviewSize.height.replace('px', '')) * 100);
                            rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);
                        }
                    }).resizable({
                        handles: 'ne, se, sw, nw',
                        grid: [scope.liveviewSize.width.replace('px', '') / 100, scope.liveviewSize.height.replace('px', '') / 100],
                        containment: ".designer-live-view",
                        stop: function (event, ui) {
                            scope.area.width = Math.round((ui.size.width / scope.liveviewSize.width.replace('px', '')) * 100);
                            scope.area.height = Math.round((ui.size.height / scope.liveviewSize.height.replace('px', '')) * 100);
                            scope.area.posX = Math.round((ui.position.left / scope.liveviewSize.width.replace('px', '')) * 100);
                            scope.area.posY = Math.round((ui.position.top / scope.liveviewSize.height.replace('px', '')) * 100);
                            rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);
                        }
                    });
                    scope.elementClick = function () {
                        scope.selected.area = scope.area;
                        rootScope.$emit(screenService.serviceName+"-area-selected");
                    };
                    if (scope.area.posX == undefined || scope.area.posY == undefined || scope.area.width == undefined || scope.area.height == undefined) {
                        scope.area.posX = 0;
                        scope.area.posY = 0;
                        scope.area.width = 12;
                        scope.area.height = 10;
                    }
                    scope.css.left = (scope.area.posX) * (scope.liveviewSize.width.replace('px', '') / 100) + "px";
                    scope.css.top = (scope.area.posY) * (scope.liveviewSize.height.replace('px', '') / 100) + "px";
                    scope.css.width = (scope.area.width) * (scope.liveviewSize.width.replace('px', '') / 100) + "px";
                    scope.css.height = (scope.area.height) * (scope.liveviewSize.height.replace('px', '') / 100) + "px";

                    rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);
                }
            };
        }];
});