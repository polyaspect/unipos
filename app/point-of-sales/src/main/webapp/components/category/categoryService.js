//# sourceURL=pos.categoryService.js
define([
    'angular'
], function (angular) {
    return ['pos.directiveIdsConst', 'pos.urlSettings', '$http', 'pos.loadingService', '$rootScope',
        function (directiveIdsConst, urlSettings, http, loadingService, rootScope) {
            this.serviceName = "pos.categoryService";
            var self = this;
            this.currentCategory = {};
            this.categories = [];
            this.doAction = function (cell) {
                if (!self.currentCategory[cell.directiveId]) {
                    self.currentCategory[cell.directiveId] = {};
                }
                self.currentCategory[cell.directiveId].category = cell.serviceData.category;
                rootScope.$emit(self.serviceName + "-currentCategory");
            };
            this.getCategories = function () {
                return self.categories;
            };
            this.setAllCategories = function (loading) {
                if (!loading) {
                    loadingService.startLoading("categories", "Warengruppen laden...");
                }
                http.get(urlSettings.baseUrl + urlSettings.getAllCategories).then(function (response) {
                    self.categories = response.data;
                    if (!loading) {
                        setTimeout(function () {
                            loadingService.finishLoading("categories")
                        }, 100);
                    }
                }, function (response) {
                    if (!loading) {
                        setTimeout(function () {
                            loadingService.errorLoading("categories", "Fehler beim Laden der Warengruppen");
                        }, 100);
                    }
                });
            };
        }];
});