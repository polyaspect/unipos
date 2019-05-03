//# sourceURL=companyEditorController.js
define([
    'angular',
    'css!data.companyEditorController'
], function () {
    return ["$scope", 'companyService', 'initOptions', '$uibModal', '$uibModalInstance', 'model', 'create',
        function (scope, companyService, initOptions, uibModal, uibModalInstance, model, create) {
            scope.model = model;
            scope.initOptions = initOptions;
            scope.create = create;

            scope.save = function () {
                uibModalInstance.close(true);
            };
            scope.delete = function () {
                if (companyService.checkIfResourceInstance(scope.model)) {
                    var promise = companyService.deleteByGuid(scope.model.guid);
                    promise.$promise.then(function () {
                        uibModalInstance.close(false);
                        swal({
                            title: "Gelöscht",
                            text: "Das Unternehmen wurde erfolgreich gelöscht!",
                            type: "success",
                            timer: 2000,
                            showConfirmaButton: false
                        });
                    },  function(error) {
                        uibModalInstance.close(false);
                        swal({
                            title: "Nicht berechtigt",
                            text: "Ihnen fehlt zumindest eine der folgenden Berechtigungen um diese Aktion auszuführen: '" + error.data + "'",
                            type: "error",
                            showConfirmaButton: true
                        });
                    });
                }
            };
        }];
});
