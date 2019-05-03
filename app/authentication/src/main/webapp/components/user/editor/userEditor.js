//# sourceURL=userEditorController.js
define([
        'angular',
        'auth.linq',
        'css!auth.userEditorController'
    ], function (angular, Enumerable) {
        return ["$scope", 'userService', 'companyService', 'rightService', 'mitarbeiterPinService', 'usernamePasswordService', 'initOptions', '$uibModal', '$uibModalInstance', 'model', 'create',
            function (scope, userService, companyService, rightService, MitarbeiterPinService, UsernamePasswordService, initOptions, uibModal, uibModalInstance, model, create) {
                scope.model = model;
                scope.initOptions = initOptions;
                scope.create = create;
                scope.companies = companyService.findAll();
                rightService.getRightsPerPermission().$promise.then(function (partitions) {
                    scope.partitions = partitions;
                    //Select Pre Selected items
                    rightService.getRightsOfUser(scope.model.userId).$promise.then(function (preSelectedGuids) {
                        Enumerable.from(scope.partitions).forEach(function (partition) {
                            Enumerable.from(partition.types).forEach(function (type) {
                                if (preSelectedGuids.indexOf(type.guid) > -1) {
                                    type.checked = true;
                                    scope.onRightSelected(partition, type);
                                }
                            });

                        });
                    });
                });


                scope.model.mitarbeiterIdPin = MitarbeiterPinService.findByUserGuid(scope.model.guid);
                scope.model.usernamePassword = UsernamePasswordService.findByUserGuid(scope.model.guid);

                scope.onModuleSelected = function (module, event) {
                    Enumerable.from(module.types).forEach(function (x) {
                        x.checked = !module.checked;
                    });
                    event.stopPropagation();
                    event.preventDefault();
                };
                scope.onRightSelected = function (module, right) {
                    module.checked = Enumerable.from(module.types).any(function (x) {
                        return (x.guid == right.guid) ? !x.checked : x.checked;
                    });
                };

                scope.save = function () {
                    var result = [];

                    angular.forEach(scope.partitions, function (partition) {
                        angular.forEach(partition.types, function (type) {
                            if (type.checked) {
                                result.push(type.guid);
                            }
                        });
                    });
                    scope.model.rights = result;

                    uibModalInstance.close(true);
                };
                scope.delete = function () {
                    if (userService.checkIfResourceInstance(scope.model)) {
                        var promise = userService.deleteByGuid(scope.model.guid);
                        promise.$promise.then(function () {
                            uibModalInstance.close(false);
                            swal({
                                title: "Gelöscht",
                                text: "Der Benutzer wurde erfolgreich gelöscht!",
                                type: "success",
                                timer: 2000,
                                showConfirmaButton: false
                            });
                        });
                    }
                };
            }
        ]
            ;
    }
);
