//# sourceURL=discountController.js
define([
        'angular',
        'data.angularResource',
        'data.sweetAlert',
        'css!data.sweetAlertStyle',
        'css!data.discountStyle',
        'css!data.jkLayoutCss'
    ], function () {
        return ["$scope", "$rootScope", "uiGridConstants", "$resource", "$uibModal", "initOptions", "discountService",
            function ($scope, rootScope, uiGridConstants, $resource, uibModal, initOptions, discountService) {
                var $this = this;
                //Initialise the default Objects
                $scope.buttonDisabled = true;
                $scope.model = {};

                //Update the UI Grid Datasource by invoking the Promise;
                $this.updateUiGridDataFromDatabase = function () {
                    $scope.gridOptions.data = discountService.findAll();
                };

                $this.checkIfOnlyOneObjectIsPresent = function (objectType) {
                    var data = $scope.gridOptions.data;
                    var count = 0;
                    angular.forEach(data, function (element) {
                        if (!discountService.checkIfResourceInstance(element) && element.creationType == objectType) {
                            count++;
                        }
                    });
                    return count;
                };

                $this.getClassName = function (row) {
                    var className = "";
                    if (!discountService.checkIfResourceInstance(row.entity)) {
                        className = "blue";
                    }
                    return className;
                };

                $this.makeDiscountEditorVisible = function (create) {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/discount/editor/discountEditor.html',
                        controller: 'DiscountEditorController',
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
                        "value": "",
                        "discountUsage": "",
                        "discountType": "",
                        "creationType": "create"
                    });
                };
                $scope.duplicateSelectedRow = function () {
                    if ($this.checkIfOnlyOneObjectIsPresent("copy") < 1) {
                        var rows = $scope.gridApi.selection.getSelectedRows();
                        var row = rows.length > 0 ? rows[0] : null;
                        if (row != null) {
                            $scope.gridOptions.data.push({
                                "name": row.name,
                                "value": row.value,
                                "discountUsage": row.discountUsage,
                                "discountType": row.discountType,
                                "creationType": "copy"
                            });
                        }
                        $scope.model = $scope.gridOptions.data[$scope.gridOptions.data.length - 1];
                        $scope.gridApi.selection.selectRow($scope.gridOptions.data[$scope.gridOptions.data.length - 1]);
                        $this.makeDiscountEditorVisible(true);
                    }
                };

                $scope.showCreateDiscountForm = function (resetDiscountForm) {
                    if (resetDiscountForm)
                        $scope.resetForm();
                    if ($this.checkIfOnlyOneObjectIsPresent("create") == 0) {
                        $this.addPlaceHolderToThetable();
                    }
                    $this.makeDiscountEditorVisible(true);
                    $scope.gridApi.selection.selectRow($scope.gridOptions.data[$scope.gridOptions.data.length - 1]);
                };
                $scope.openSelectedDiscount = function () {
                    if ($scope.model.creationType && $scope.model.creationType == "create") {
                        $this.makeDiscountEditorVisible(true);
                    } else {
                        $this.makeDiscountEditorVisible(false);
                    }
                };
                $scope.publishChanges = function () {
                    swal({
                        title: "Sind Sie sicher?",
                        text: "Dadurch werden alle unveröffentlichten Änderungen freigegeben!",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#388E3C",
                        confirmButtonText: "Änderungen freigeben",
                        cancelButtonText: "Abbrechen",
                        closeOnConfirm: false,
                        closeOnCancel: false
                    }, function (isConfirm) {
                        if (isConfirm) {
                            var promise = discountService.publishChanges();
                            promise.$promise.then(function () {
                                $this.updateUiGridDataFromDatabase();
                                $scope.resetForm();
                                swal({
                                    title: "Änderungen freigegeben",
                                    text: "Ihre vorgenommen Änderungen wurden freigegeben",
                                    type: "success",
                                    timer: 2000,
                                    showConfirmButton: true
                                });
                            })
                        } else {
                            swal({
                                title: "Freigeben abgebrochen",
                                text: "Ihre vorgenommen Änderungen wurden NICHT freigegeben",
                                type: "error"
                            })
                        }
                    });
                };

                $scope.resetChanges = function () {
                    swal({
                            title: "Sind Sie sicher?",
                            text: "Dadurch werden alle unveröffentlichten Änderungen gelöscht!",
                            type: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#388E3C",
                            confirmButtonText: "Änderungen verwerfen",
                            cancelButtonText: "Abbrechen",
                            closeOnConfirm: false,
                            closeOnCancel: false
                        }, function (isConfirm) {
                            if (isConfirm) {
                                var promise = discountService.resetChanges();
                                promise.$promise.then(function () {
                                    $this.updateUiGridDataFromDatabase();
                                    $scope.resetForm();
                                    swal({
                                        title: "Änderungen verworfen",
                                        text: "Ihre vorgenommen Änderungen wurden verworfen",
                                        type: "success",
                                        timer: 2000,
                                        showConfirmButton: true
                                    });
                                })
                            } else {
                                swal({
                                    title: "Verwerfen abgebrochen",
                                    text: "Ihre vorgenommen Änderungen wurden NICHT gelöscht",
                                    type: "error"
                                })
                            }
                        }
                    );
                };

                //Delete the current selected Item
                $scope.resetForm = function () {
                    $scope.gridApi.selection.clearSelectedRows();
                    $scope.model = {};
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
                            visible: false,
                            field: 'discountIdentifier', sort: {
                            direction: uiGridConstants.ASC,
                            priority: 1
                        },
                            sortingAlgorithm: function (a, b) {
                                if (a == b) return 0;
                                if (a == "") return 1;
                                if (b == "") return -1;
                                if (a < b) return -1;
                                if (a > b) return 1;
                            },
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Bezeichnung',
                            field: 'name',
                            enableCellEdit: !window.isMobile(),
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Verwendung',
                            field: 'discountUsage',
                            enableCellEdit: false,
                            cellTemplate: '<div class="ui-grid-cell-contents">{{COL_FIELD == "SINGLE" ? "Bonierung" : COL_FIELD == "ORDER" ? "Bestellung" : ""}}</div>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Typ',
                            field: 'discountType',
                            enableCellEdit: false,
                            cellTemplate: '<div class="ui-grid-cell-contents">{{COL_FIELD == "VALUE" ? "Wert" : COL_FIELD == "PERCENTAGE" ? "Prozent" : ""}}</div>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Wert',
                            field: 'value',
                            enableCellEdit: !window.isMobile(),
                            cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity.discountType == "VALUE" ? (row.entity[col.field]| currency:\'EUR: \') : row.entity[col.field] + "%"}}</div>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        }
                    ],
                    data: discountService.findAll()
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
                    var promise;
                    if (discountService.checkIfResourceInstance(rowEntity)) {
                        promise = discountService.update(rowEntity);
                        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise);
                    } else {
                        promise = discountService.save(rowEntity);
                        promise.$promise.then(function () {
                            $this.updateUiGridDataFromDatabase();
                        })
                    }
                    swal({
                        title: "Gespeichert",
                        text: "Der Rabatt wurde erfolgreich gespeichert! Um die Änderungen zu aktivieren, drücken Sie bitte auf 'Freigeben'.",
                        type: "success",
                        timer: 5000,
                        showConfirmaButton: false
                    });
                };
            }
        ]
            ;
    }
);