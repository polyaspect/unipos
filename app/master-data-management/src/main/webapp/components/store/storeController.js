//# sourceURL=storeController.js
define([
        'angular',
        'data.linq',
        'data.angularResource',
        'css!data.storeStyle'
    ], function (angular, Enumerable) {
        return ["$scope", "$rootScope", "uiGridConstants", "$resource", "$uibModal", "$q", "initOptions", "storeService", "companyService",
            function ($scope, rootScope, uiGridConstants, $resource, uibModal, q, initOptions, storeService, companyService) {
                var $this = this;
                //Initialise the default Objects
                $scope.buttonDisabled = true;
                $scope.model = {};
                $scope.model.address = {};

                //Update the UI Grid Datasource by invoking the Promise;
                $this.updateUiGridDataFromDatabase = function () {
                    q.all([companyService.findAll().$promise, storeService.findAll().$promise]).then(function (result) {
                        Enumerable.from(result[1]).forEach(function (store) {
                            var company = Enumerable.from(result[0]).firstOrDefault(function (company) {
                                return store.companyGuid == company.guid;
                            }, undefined);
                            if(company != undefined){
                                store.companyName = company.name;
                            }
                            var date = new Date(store.closeHour);
                            date.setHours(date.getHours() - 1);
                            store.closeHour = date;
                        });
                        $scope.gridOptions.data = result[1];
                    });
                };
                $this.updateUiGridDataFromDatabase();
                $this.checkIfOnlyOneObjectIsPresent = function (objectType) {
                    var data = $scope.gridOptions.data;
                    var count = 0;
                    angular.forEach(data, function (element) {
                        if (!storeService.checkIfResourceInstance(element) && element.creationType == objectType) {
                            count++;
                        }
                    });
                    return count;
                };

                $this.getClassName = function (row) {
                    var className = "";
                    if (!storeService.checkIfResourceInstance(row.entity)) {
                        className = "blue";
                    }
                    return className;
                };

                $this.makeStoreEditorVisible = function (create) {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/store/editor/storeEditor.html',
                        controller: 'StoreEditorController',
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
                        $scope.resetForm();
                    });
                };

                $this.addPlaceHolderToThetable = function () {
                    $scope.gridOptions.data.push({
                        "name": "",
                        "address": {
                            "street": "",
                            "postCode": 1130,
                            "city": "Wien",
                            "country": "Österreich"
                        },
                        "fax": "",
                        "email": "",
                        "phone": "",
                        "creationType": "create"
                    });
                };

                $scope.openSelectedStore = function () {
                    if ($scope.model.creationType && $scope.model.creationType == "create") {
                        $this.makeStoreEditorVisible(true);
                    } else {
                        $this.makeStoreEditorVisible(false);
                    }
                };

                $scope.showCreateStoreDialog = function (resetCreateProductForm) {
                    if (resetCreateProductForm)
                        $scope.resetForm();
                    if ($this.checkIfOnlyOneObjectIsPresent("create") == 0) {
                        $this.addPlaceHolderToThetable();
                    }
                    $this.makeStoreEditorVisible(true);
                    $scope.gridApi.selection.selectRow($scope.gridOptions.data[$scope.gridOptions.data.length - 1]);
                };

                //Delete the current selected Item
                $scope.resetForm = function () {
                    $scope.gridApi.selection.clearSelectedRows();
                    $scope.model = {};
                    $scope.model.address = {};
                    $this.removeUiGridSelectionColor();
                    $scope.buttonDisabled = true;
                };

                $this.removeUiGridSelectionColor = function () {
                    $.each($(".ui-grid-cell-focus"), function (index, data) {
                        angular.element(data).removeClass("ui-grid-cell-focus")
                    });
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
                            name: 'Bezeichnung',
                            field: 'name',
                            enableCellEdit: !window.isMobile(),
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Adresse',
                            field: 'address.street',
                            enableCellEdit: !window.isMobile(),
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Postleitzahl',
                            field: 'address.postCode',
                            enableCellEdit: !window.isMobile(),
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Stadt',
                            field: 'address.city',
                            enableCellEdit: !window.isMobile(),
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Staat',
                            field: 'address.country',
                            enableCellEdit: false,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Unternehmen',
                            field: 'companyName',
                            enableCellEdit: false,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: "BS.",
                            field: 'closeHour',
                            enableCellEdit: false,
                            cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity.closeHour | date:\'HH:mm\'}}</div>',
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

                $scope.saveRow = function (rowEntity) {
                    let promise;
                    if (storeService.checkIfResourceInstance(rowEntity)) {
                        promise = storeService.update(rowEntity);
                        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise);
                    } else {
                        promise = storeService.save(rowEntity);
                        promise.$promise.then(function () {
                            $this.updateUiGridDataFromDatabase();
                        })
                    }
                    swal({
                        title: "Gespeichert",
                        text: "Die Filiale wurde erfolgreich gespeichert!",
                        type: "success",
                        timer: 2000,
                        showConfirmaButton: false
                    });
                };
            }];
    }
);