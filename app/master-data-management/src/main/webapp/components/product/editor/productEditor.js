//# sourceURL=productEditorController.js
define([
    'angular',
    'data.linq',
    'css!data.productEditorController'
], function (angular, Enumerable) {
    return ["$scope", 'categoryService', 'companyService', 'productService', 'initOptions', '$uibModal', '$uibModalInstance', 'model', 'defaultProduct', 'create',
        function (scope, categoryService, companyService, productService, initOptions, uibModal, uibModalInstance, model, defaultProduct, create) {
            scope.model = model;
            scope.defaultProduct = angular.copy(defaultProduct);
            scope.initOptions = initOptions;
            scope.productStores = model.stores;
            scope.create = create;

            categoryService.findAll().$promise.then(function (data) {
                scope.categories = data;
            });
            companyService.findAll().$promise.then(function (data) {
                scope.companies = Enumerable.from(data).where(function (x) {
                    return x.stores && x.stores.length > 0
                }).toArray();
                Enumerable.from(scope.companies).forEach(function (x) {
                    Enumerable.from(x.stores).forEach(function (y) {
                        Enumerable.from(scope.model.stores).forEach(function (z) {
                            if (y.guid == z) {
                                scope.onStoreSelected(x, y);
                                y.checked = true;
                            }
                        });
                    });
                });
            });

            scope.onCompanySelected = function (company, event) {
                Enumerable.from(company.stores).forEach(function (x) {
                    x.checked = !company.checked;
                });
                event.stopPropagation();
                event.preventDefault();
            };
            scope.onStoreSelected = function (company, store) {
                company.checked = Enumerable.from(company.stores).any(function (x) {
                    return (x.id == store.id) ? !x.checked : x.checked;
                });
            };
            scope.showCreateCategoryDialog = function () {
                var modalInstance = uibModal.open({
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    templateUrl: 'createCategoryDialog.tpl.html',
                    controller: DialogController,
                    size: 'md',
                    resolve: {}
                });
                modalInstance.result.then(function (answer) {
                    categoryService.findAll().$promise.then(function (data) {
                        scope.categories = data;
                    });
                    if (answer != null) {
                        scope.model.category = answer;
                    }
                });
            };
            scope.saveProduct = function () {
                var stores = [];
                Enumerable.from(scope.companies).forEach(function (company) {
                    Enumerable.from(company.stores).forEach(function (store) {
                        if (store.checked) {
                            stores.push(store.guid);
                        }
                    });
                });
                scope.model.stores = stores;
                uibModalInstance.close(true);
            };
            scope.deleteProduct = function () {
                if (productService.checkIfResourceInstance(scope.model)) {
                    var promise = productService.deleteByProductNumber(scope.model.productIdentifier);
                    promise.$promise.then(function () {
                        uibModalInstance.close(false);
                        swal({
                            title: "Gelöscht",
                            text: "Der Artikel wurde erfolgreich gelöscht! Um die Änderungen zu aktivieren, drücken Sie bitte auf 'Freigeben'.",
                            type: "success",
                            timer: 5000,
                            showConfirmaButton: false
                        });
                    });
                }
            };
            function DialogController(scope, uibModalInstance, taxRateService, categoryService) {
                scope.category = {};
                var taxRates = taxRateService.findAll();
                taxRates.$promise.then(function (data) {
                    scope.taxRates = data;
                });

                scope.save = function () {
                    var promise = categoryService.save(scope.category);
                    promise.$promise.then(function (data) {
                        uibModalInstance.close(data);
                    });
                };
            }

            DialogController.$inject = ['$scope', '$uibModalInstance', 'taxRateService', 'categoryService'];
        }];
});
