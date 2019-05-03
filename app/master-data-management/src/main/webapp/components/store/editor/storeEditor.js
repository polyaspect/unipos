//# sourceURL=storeEditorController.js
define([
    'angular',
    'css!data.storeEditorController'
], function (angular) {
    return ["$scope", 'storeService', 'initOptions', '$uibModal', '$http', 'companyService', '$uibModalInstance', 'model', 'create',
        function (scope, storeService, initOptions, uibModal, http, companyService, uibModalInstance, model, create) {
            var self = this;
            scope.model = model;
            scope.initOptions = initOptions;
            scope.create = create;
            scope.companies = companyService.findAll();
            http.get("components/store/countries.json").then(function (response) {
                scope.countriesOftheWorld = response.data
            });

            scope.save = function () {
                uibModalInstance.close(true);
            };
            scope.delete = function () {
                if (storeService.checkIfResourceInstance(scope.store)) {
                    var promise = storeService.deleteByGuid(scope.store.guid);
                    promise.$promise.then(function () {
                        uibModalInstance.close(false);
                        swal({
                            title: "Gelöscht",
                            text: "Die Filiale wurde erfolgreich gelöscht!",
                            type: "success",
                            timer: 2000,
                            showConfirmaButton: false
                        });
                    });
                }
            };
        }];
});
