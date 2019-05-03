//# sourceURL=pos.designer.designerOptions.collectionSettings.js
define([
    'angular',
    'css!pos.designer.designerOptions.collectionSettings'
], function (angular) {
    return ['$scope', '$uibModalInstance', '$location', 'pos.designer.screenCollectionApiService', 'modal',
        function (scope, uibModalInstance, location, screenCollectionApiService, model) {
            scope.companies = [];
            scope.options = {
                default: model.default
            };
            screenCollectionApiService.getCompanies().$promise.then(function (companies) {
                scope.companies = companies;
                Enumerable.From(scope.companies).ForEach(function (company) {
                    Enumerable.From(company.stores).ForEach(function (store) {
                        if (model.stores.indexOf(store.guid) > -1) {
                            store.checked = true;
                            scope.onStoreSelected(company, store);
                        }
                    });
                });
            });
            scope.onCompanySelected = function (company, event) {
                Enumerable.From(company.stores).ForEach(function (x) {
                    x.checked = !company.checked;
                });
                event.stopPropagation();
                event.preventDefault();
            };
            scope.onStoreSelected = function (company, store) {
                company.checked = Enumerable.From(company.stores).Any(function (x) {
                    return (x.guid == store.guid) ? !x.checked : x.checked;
                });
            };
            scope.cancel = function () {
                uibModalInstance.dismiss();
            };
            scope.save = function () {
                var result = [];

                angular.forEach(scope.companies, function (company) {
                    angular.forEach(company.stores, function (store) {
                        if (store.checked) {
                            result.push(store.guid);
                        }
                    });
                });
                scope.options.stores = result;
                uibModalInstance.close(scope.options);
            };
        }];
});