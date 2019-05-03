//# sourceURL=pos.designer.screenService.js
define([
    'angular'
], function (angular) {
    return ['pos.designer.screenCollectionService', '$rootScope', 'uuid2', '$http', '$injector', '$routeParams',
        function (screenCollectionService, rootScope, uuid2, http, injector, routeParams) {
            this.serviceName = "design.screenService";
            this.new = function (screenname) {
                var screen = {
                    name: screenname,
                    settings: {
                        function: "HOME",
                        orientation: "LANDSCAPE",
                        size: "M"
                    },
                    data: []
                };
                screenCollectionService.current.screens.push(screen);
                return screen;
            };
            this.panelIdExists = function (panelId, exceptThisId) {
                var erg = false;
                angular.forEach(self.getCatalogData(self.currentScreenSet.catalogName), function (design, designName) {
                    if (designName != "designerCurrentDirectiveEdit" && design.data[self.currentScreenSet.device] && design.data[self.currentScreenSet.device].data) {
                        angular.forEach(design.data[self.currentScreenSet.device].data, function (screen, screenName) {
                            if (!erg) {
                                erg = Enumerable.From(screen.data).Any(function (x) {
                                    return x.Value.data.id == panelId && x.Value.data.id != exceptThisId;
                                })
                            }
                        });
                    }
                });
                return erg;
            };
            this.getScreens = function () {
                return screenCollectionService.current.screens;
            };
            this.getAreaByDirAndService = function (dirName, serviceName) {
                let result = [];
                Enumerable.From(screenCollectionService.current.screens).ForEach(function (x) {
                    Array.prototype.push.apply(result, Enumerable.From(x.data).Where(function (y) {
                        return y.name == dirName && y.data && y.data.service && y.data.service.name == serviceName;
                    }).ToArray());
                });
                return result;
            };
            this.getDataByUuid = function (uuid) {

            };
            this.deleteByUuid = function (uuid) {
                for (let i = 0; i < screenCollectionService.current.screens.length; i++) {
                    for (let j = 0; j < screenCollectionService.current.screens[i].data.length; j++) {
                        if (screenCollectionService.current.screens[i].data[j].uuid == uuid) {
                            screenCollectionService.current.screens[i].data.splice(j, 1);
                            return;
                        }
                    }
                }
            };
        }];
});