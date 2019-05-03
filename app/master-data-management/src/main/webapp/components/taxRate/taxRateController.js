//# sourceURL=taxRateController.js
define([
        'angular',
        'data.angularResource',
        'css!data.taxRateStyle'
    ], function (angular) {
        return ["$scope", "$rootScope", "uiGridConstants", "$resource", "$uibModal", "initOptions", "taxRateService",
            function ($scope, rootScope, uiGridConstants, $resource, uibModal, initOptions, taxRateService) {
                var $this = this;
                //Initialise the default Objects
                $scope.buttonDisabled = true;
                $scope.model = {};


                //Update the UI Grid Datasource by invoking the Promise;
                $this.updateUiGridDataFromDatabase = function () {
                    $scope.gridOptions.data = taxRateService.findAll();
                };

                $this.checkIfOnlyOneObjectIsPresent = function (objectType) {
                    var data = $scope.gridOptions.data;
                    var count = 0;
                    angular.forEach(data, function (element) {
                        if (!taxRateService.checkIfResourceInstance(element) && element.creationType == objectType) {
                            count++;
                        }
                    });
                    return count;
                };

                $this.getClassName = function (row) {
                    var className = "";
                    if (!taxRateService.checkIfResourceInstance(row.entity)) {
                        className = "blue";
                    }
                    return className;
                };

                $this.makeTaxRateEditorVisible = function (create) {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/taxRate/editor/taxRateEditor.html',
                        controller: 'TaxRateEditorController',
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
                        "name": "",
                        "description": "",
                        "percentage": 20,
                        "taxRateCategory": "NORMAL",
                        "creationType": "create"
                    });
                };
                $scope.openSelectedTaxRate = function () {
                    if ($scope.model.creationType && $scope.model.creationType == "create") {
                        $this.makeTaxRateEditorVisible(true);
                    } else {
                        $this.makeTaxRateEditorVisible(false);
                    }
                };

                $scope.duplicateSelectedRow = function () {
                    if ($this.checkIfOnlyOneObjectIsPresent("copy") < 1) {
                        var rows = $scope.gridApi.selection.getSelectedRows();
                        var row = rows.length > 0 ? rows[0] : null;
                        if (row != null) {
                            $scope.gridOptions.data.push({
                                "name": row.name,
                                "description": row.description,
                                "percentage": row.percentage,
                                "taxRateCategory": row.taxRateCategory,
                                "creationType": "copy"
                            });
                            $scope.model = $scope.gridOptions.data[$scope.gridOptions.data.length - 1];
                            $scope.gridApi.selection.selectRow($scope.gridOptions.data[$scope.gridOptions.data.length - 1]);
                            $this.makeTaxRateEditorVisible(true);
                        }
                    }
                };

                $scope.showCreateTaxRateDialog = function (resetCreateProductForm) {
                    if (resetCreateProductForm)
                        $scope.resetForm();
                    if ($this.checkIfOnlyOneObjectIsPresent("create") == 0) {
                        $this.addPlaceHolderToThetable();
                    }
                    $this.makeTaxRateEditorVisible(true);
                    $scope.gridApi.selection.selectRow($scope.gridOptions.data[$scope.gridOptions.data.length - 1]);
                };
                $this.removeUiGridSelectionColor = function () {
                    $.each($(".ui-grid-cell-focus"), function (index, data) {
                        angular.element(data).removeClass("ui-grid-cell-focus")
                    });
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
                            name: 'Bezeichnung',
                            field: 'name',
                            enableCellEdit: !window.isMobile(),
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Prozent',
                            field: 'percentage',
                            enableCellEdit: !window.isMobile(),
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Typ',
                            field: 'taxRateCategory',
                            enableCellEdit: !window.isMobile(),
                            cellTemplate: '<div class="ui-grid-cell-contents">{{COL_FIELD == "NORMAL" ? "Normal" : COL_FIELD == "DISCOUNT" ? "Rabatt" : ""}}</div>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        }
                    ],
                    data: taxRateService.findAll()
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
                    if (taxRateService.checkIfResourceInstance(rowEntity)) {
                        promise = taxRateService.update(rowEntity);
                        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise);
                    } else {
                        promise = taxRateService.save(rowEntity);
                        promise.$promise.then(function () {
                            $this.updateUiGridDataFromDatabase();
                        })
                    }
                    swal({
                        title: "Gespeichert",
                        text: "Die Steuerklasse wurde erfolgreich gespeichert!",
                        type: "success",
                        timer: 2000,
                        showConfirmaButton: false
                    });
                };
            }];
    }
);