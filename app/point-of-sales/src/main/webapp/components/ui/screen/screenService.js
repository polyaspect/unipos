//# sourceURL=pos.ui.screenService.js
define([
    'angular'
], function (angular) {
    return ["pos.areaService", "pos.dataService", 'pos.directiveIdsConst', 'pos.deviceService', '$http', 'pos.ui.elements.text.textDirectiveService', '$cookies',
        function (areaService, dataService, directiveIdsConst, deviceService, http, textService, cookies) {
            this.toScreen = function (screenName) {
                areaService.toScreen(screenName);
            };

            this.updateScreenCollection = function (data, scope) {
                var screenCollection = data.payload;

                screenCollection = JSON.parse(screenCollection);
                if(!angular.isObject(screenCollection)){
                    screenCollection = JSON.parse(screenCollection);
                }

                http.get("/data/stores/getByUserAndDevice").then(function (response) {

                    if (screenCollection.settings.stores.indexOf(response.data.guid) < 0) {
                        noScreensFound();
                        return;
                    }

                    let approxSizes = deviceService.getApproxSize();
                    let screens;
                    for (size of approxSizes.data) {
                        screens = Enumerable.From(screenCollection.screens).Where(function (x) {
                            return x.settings.orientation == approxSizes.orientation && x.settings.size == size.size && (x.settings.function == "HOME" || x.settings.function == "OTHER");
                        }).ToArray();
                        if (screens.length > 0) {
                            break;
                        }
                    }
                    if (screens.length <= 0) {
                        noScreensFound();
                        return;
                    }
                    scope.areaConfigCollection = screens;

                    areaService.data = scope.areaConfigCollection;
                    areaService.areaNames = Enumerable.From(scope.areaConfigCollection).Select(function (x) {
                        return x.name;
                    }).ToArray();
                    dataService.setByDirectiveKey("areaService", directiveIdsConst.areaConfigAll, scope.areaConfigCollection);

                    http.get("/auth/auth/getUserByAuthToken/" + cookies.get("AuthToken")).then(function (response) {
                        textService.setText("loggedInUser", "Angemeldeter Benutzer: " + response.data.name + " " + response.data.surname);
                    });

                });
            }
        }]
});