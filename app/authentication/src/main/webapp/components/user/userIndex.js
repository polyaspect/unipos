//# sourceURL=userIndexController.js
define([
        'angular',
        'auth.linq',
        'auth.sweetAlert',
        'css!auth.sweetAlertStyle',
        'css!auth.userIndexController'
    ], function (angular, Enumerable) {
        return ["$rootScope", "$scope", "$q", '$uibModal', 'initOptions', "uiGridConstants", "$resource", "userService", "companyService",
            "roleService", "rightService", "mitarbeiterPinService", "usernamePasswordService",
            function (rootScope, $scope, q, uibModal, initOptions, uiGridConstants, $resource, userService, companyService,
                      roleService, rightService, mitarbeiterIdPinService, usernamePasswordService) {
                var $this = this;
                $scope.buttonDisabled = true;
                $scope.model = {};

                //Update the UI Grid Datasource by invoking the Promise;
                $this.updateUiGridDataFromDatabase = function () {
                    q.all([userService.findAll().$promise, companyService.findAll().$promise]).then(function (result) {
                        $scope.gridOptions.data = result[0];
                        Enumerable.from(result[0]).forEach(function (user) {
                            var company = Enumerable.from(result[1]).firstOrDefault(function (company) {
                                return company.guid == user.companyGuid;
                            });
                            if (company) {
                                user.companyName = company.name;
                            }
                        });
                    });
                };

                $this.updateUiGridDataFromDatabase();

                $this.checkIfOnlyOneObjectIsPresent = function (objectType) {
                    var data = $scope.gridOptions.data;
                    var count = 0;
                    angular.forEach(data, function (element) {
                        if (!userService.checkIfResourceInstance(element) && element.creationType == objectType) {
                            count++;
                        }
                    });
                    return count;
                };

                $this.getClassName = function (row) {
                    var className = "";
                    if (!userService.checkIfResourceInstance(row.entity)) {
                        className = "blue";
                    }
                    return className;
                };

                $this.makeEditorVisible = function (create) {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/user/editor/userEditor.html',
                        controller: 'userEditorController',
                        size: 'lg',
                        resolve: {
                            model: $scope.model,
                            create: create
                        }
                    });
                    modalInstance.result.then(function (createOrUpdate) {
                        if (createOrUpdate) {
                            $scope.saveRow($scope.model)
                        } else {
                            $this.updateUiGridDataFromDatabase();
                        }
                    });
                };

                $this.addPlaceHolderToThetable = function () {
                    $scope.gridOptions.data.push({
                        "name": "",
                        "surname": "",
                        "enabled": true,
                        "rights": [],
                        "usernamePassword": {},
                        "mitarbeiterIdPin": {},
                        "creationType": "create"
                    });

                };
                $this.removeUiGridSelectionColor = function () {
                    $.each($(".ui-grid-cell-focus"), function (index, data) {
                        angular.element(data).removeClass("ui-grid-cell-focus")
                    });
                };
                $scope.showCreateDialog = function (resetCreateProductForm) {
                    if (resetCreateProductForm)
                        $scope.resetForm();
                    if ($this.checkIfOnlyOneObjectIsPresent("create") == 0) {
                        $this.addPlaceHolderToThetable();
                    }
                    $this.makeEditorVisible(true);
                    $scope.gridApi.selection.selectRow($scope.gridOptions.data[$scope.gridOptions.data.length - 1]);
                };

                $scope.openSelectedDialog = function () {
                    if ($scope.model.creationType && $scope.model.creationType == "create") {
                        $this.makeEditorVisible(true);
                    } else {
                        $this.makeEditorVisible(false);
                    }
                };
                //Delete the current selected Item
                $scope.resetForm = function () {
                    $scope.gridApi.selection.clearSelectedRows();
                    $scope.model = {};
                    $this.removeUiGridSelectionColor();
                    $scope.buttonDisabled = true;
                };

                //UI Grid Options and Settings...
                $scope.gridOptions = {
                    enableFiltering: true,
                    enableSorting: true,
                    enableRowSelection: true,
                    enableRowHeaderSelection: false,
                    multiSelect: false,
                    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
                    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
                    enableSelectAll: false,
                    columnDefs: [
                        {
                            name: 'Vorname',
                            field: 'name',
                            enableCellEdit: true,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Nachname',
                            field: 'surname',
                            enableCellEdit: true,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Unternehmen',
                            field: 'companyName',
                            enableCellEdit: true,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        }
                    ],
                    data: []
                };

                $scope.gridOptions.onRegisterApi = function (gridApi) {
                    //set gridApi on scope
                    $scope.gridApi = gridApi;
                    gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
                    gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                        if (row.isSelected) {
                            $scope.model = row.entity;
                            $scope.buttonDisabled = false; //This attribute controls, if the user is able to click the Buttons on the ProductIndexView
                        } else {
                            $scope.resetForm();
                            $scope.buttonDisabled = true;
                        }
                    });
                    gridApi.cellNav.on.navigate($scope, function (newRowCol, oldRowCol) {
                        var rowEntity = newRowCol.row.entity;
                        $scope.gridApi.selection.selectRow(rowEntity);
                    });
                };

                $this.saveMitarbeiteridPin = function (user, responseFromSave) {
                    if (user.mitarbeiterIdPin == undefined) {
                        return;
                    }
                    if (!user.mitarbeiterIdPin.pin || user.mitarbeiterIdPin.pin == "") {
                        user.mitarbeiterIdPin.pin = "-1";
                    }
                    if (user.mitarbeiterIdPin.user == undefined) {
                        user.mitarbeiterIdPin.user = responseFromSave;
                    }
                    var promise = mitarbeiterIdPinService.save(user.mitarbeiterIdPin);
                    promise.$promise.then(function () {
                    }, function (error) {
                        /*swal({
                            "title": "Fehler beim Speichern der Mitarbeiter-Pin Informationen",
                            "text": "Hat ein anderer Mitarbeiter bereits die von Ihnen eingegebene Mitarbeiter-Nr?",
                            "type": "error"
                        });*/
                    });
                };

                $this.saveUsernamePassword = function (user, responseFromSave) {
                    if (user.usernamePassword == undefined) {
                        return;
                    }
                    if (!user.usernamePassword.password || user.usernamePassword.password == "") {
                        user.usernamePassword.password = "";
                    }
                    if (user.usernamePassword.user == undefined) {
                        user.usernamePassword.user = responseFromSave;
                    }
                    usernamePasswordService.save(user.usernamePassword)
                };

                $scope.saveUser = function (rowEntity, responseFromSave) {
                    if (rowEntity.rights && rowEntity.rights.length > 0) {
                        rightService.assignRightsToUser(rowEntity.userId, rowEntity.rights).$promise.then(function () {
                            swal({
                                "title": "Erfolgreich",
                                "text": "Die Berechtigungen wurden erfolgreich geändert",
                                "type": "success",
                                "timer": 2500
                            })
                        }, function () {
                            swal({
                                "title": "Nicht erfolgreich",
                                "text": "Die Berechtigungen konnten nicht erfolgreich bearbeitet werden. Verfügen Sie über ausreichende Berechtigungen?",
                                "type": "error"
                            })
                        });
                    }
                    $this.saveMitarbeiteridPin(rowEntity, responseFromSave);
                    $this.saveUsernamePassword(rowEntity, responseFromSave);
                    $this.updateUiGridDataFromDatabase();
                    $scope.resetForm();
                };

                $scope.saveRow = function (rowEntity) {
                    var promise;
                    var userCopy = angular.copy(rowEntity);
                    if (userService.checkIfResourceInstance(rowEntity)) {
                        promise = userService.update(rowEntity);
                        promise.then(function (user) {
                            $scope.saveUser(userCopy, user);
                        });
                        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise);
                    } else {
                        promise = userService.save(rowEntity);
                        promise.$promise.then(function (user) {
                            $scope.saveUser(userCopy, user);
                        });
                    }
                };
            }];
    }
);