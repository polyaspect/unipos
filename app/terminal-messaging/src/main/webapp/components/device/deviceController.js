//# sourceURL=deviceController.js
define([
        'angular',
        'socket.angularResource',
        'socket.sweetAlert',
        'css!socket.sweetAlertStyle',
        'css!socket.deviceStyle'
    ], function (angular) {
        return ["$scope", "$rootScope", "uiGridConstants", "$resource", "$uibModal", "initOptions", "deviceService", "printerService", 'socket.storeService',
            function ($scope, rootScope, uiGridConstants, $resource, uibModal, initOptions, deviceService, printerService, storeService) {
                var $this = this;
                //Initialise the default Objects
                $scope.buttonDisabled = true;
                $scope.model = {};

                //Update the UI Grid Datasource by invoking the Promise;
                $this.updateUiGridDataFromDatabase = function () {
                    $scope.gridOptions.data = deviceService.findAll();
                };

                $this.checkIfOnlyOneObjectIsPresent = function (objectType) {
                    var data = $scope.gridOptions.data;
                    var count = 0;
                    angular.forEach(data, function (element) {
                        if (!deviceService.checkIfResourceInstance(element) && element.creationType == objectType) {
                            count++;
                        }
                    });
                    return count;
                };

                $this.getClassName = function (row) {
                    var className = "";
                    if (!deviceService.checkIfResourceInstance(row.entity)) {
                        className = "blue";
                    }
                    return className;
                };

                $this.makeEditorVisible = function (create) {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/device/editor/deviceEditor.html',
                        controller: 'DeviceEditorController',
                        size: 'md',
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
                        "deviceName": "",
                        "ipAdress": "",
                        "authToken": "",
                        "currentWorkingUser": "",
                        "creationType": "create"
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
                $scope.openSelectedEditor = function () {
                    if ($scope.model.creationType && $scope.model.creationType == "create") {
                        $this.makeEditorVisible(true);
                    } else {
                        $this.makeEditorVisible(false);
                    }
                };

                $scope.setDevice = function(){
                    if($scope.model.deviceId == null){
                        alert("Hier muss ein SWAL hinkommen");
                        return;
                    }
                    deviceService.setDevice($scope.model.deviceId);
                };

                //Delete the current selected Item
                $scope.resetForm = function () {
                    $scope.gridApi.selection.clearSelectedRows();
                    $scope.model = {};
                    $scope.buttonDisabled = true;
                    $this.removeUiGridSelectionColor();
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
                            field: 'deviceName',
                            width: 150,
                            enableCellEdit: true,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'ip-Adresse',
                            field: 'ipAdress',
                            width: 200,
                            enableCellEdit: false,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Registriert am',
                            field: 'creationDate',
                            enableCellEdit: false,
                            cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity[col.field] | date: \'dd.MM.yy HH:mm\'}}</div>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Letzes Signal',
                            field: 'lastKeepAlive',
                            enableCellEdit: false,
                            cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity[col.field] | date: \'dd.MM.yy HH:mm\'}}</div>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Neuladen notwendig',
                            field: 'reloadRequired',
                            enableCellEdit: true,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Neuladen wenn möglich',
                            field: 'reloadWhenPossible',
                            enableCellEdit: true,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Neuladen erzwingen',
                            field: 'forceReload',
                            enableCellEdit: true,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Letzes Neuladen',
                            field: 'lastReload',
                            enableCellEdit: false,
                            cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity[col.field] | date: \'dd.MM.yy HH:mm\'}}</div>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        }
                    ],
                    data: deviceService.findAll()
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
                    if (deviceService.checkIfResourceInstance(rowEntity)) {
                        promise = deviceService.update(rowEntity);
                        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise);
                    } else {
                        var printer = $scope.model.selectedPrinter;
                        promise = storeService.addStoreAndPrinterToDevice($scope.model.deviceName, $scope.model.deviceId, $scope.model.selectedStore, $scope.model.selectedPrinter);
                        promise.success(function () {
                            $this.updateUiGridDataFromDatabase();
                        })
                    }
                };
            }];
    }
);