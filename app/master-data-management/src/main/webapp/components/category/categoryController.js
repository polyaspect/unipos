//# sourceURL=categoryController.js
define([
    'angular',
    'data.angularResource',
    'data.sweetAlert',
    'css!data.sweetAlertStyle',
    'css!data.categoryStyle'
], function (angular) {
    return ["$scope", "$rootScope", "uiGridConstants", "$uibModal", "$resource", "categoryService", "taxRateService", "initOptions",
        function ($scope, rootScope, uiGridConstants, uibModal, $resource, categoryService, taxRateService, initOptions) {
            var $this = this;
            //Initialise the default Objects
            $scope.buttonDisabled = true;
            $scope.model = {};

            //Update the UI Grid Datasource by invoking the Promise;
            $this.updateUiGridDataFromDatabase = function () {
                var promise = categoryService.findAll();
                promise.$promise.then(function (data) {
                    $scope.gridOptions.data = data;
                });
            };

            $this.checkIfOnlyOneObjectIsPresent = function (objectType) {
                var data = $scope.gridOptions.data;
                var count = 0;
                angular.forEach(data, function (element) {
                    if (!categoryService.checkIfResourceInstance(element) && element.creationType == objectType) {
                        count++;
                    }
                });
                return count;
            };


            $this.getClassName = function (row) {
                var className = "";
                if (!categoryService.checkIfResourceInstance(row.entity)) {
                    className = "blue";
                }
                return className;
            };

            $this.makeCategoryEditorVisible = function (create) {
                var modalInstance = uibModal.open({
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    templateUrl: initOptions.baseDir + 'components/category/editor/categoryEditor.html',
                    controller: 'CategoryEditorController',
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
                    "taxRated.id": undefined,
                    "creationType": "create"
                });

            };

            $scope.showCreateCategoryDialog = function (resetCreateProductForm) {
                if (resetCreateProductForm) {
                    $scope.resetForm();
                }
                if ($this.checkIfOnlyOneObjectIsPresent("create") == 0) {
                    $this.addPlaceHolderToThetable();
                }
                $this.makeCategoryEditorVisible(true);
                $scope.gridApi.selection.selectRow($scope.gridOptions.data[$scope.gridOptions.data.length - 1]);
            };
            $this.removeUiGridSelectionColor = function () {
                $.each($(".ui-grid-cell-focus"), function (index, data) {
                    angular.element(data).removeClass("ui-grid-cell-focus")
                });
            };
            $scope.openSelectedCategory = function () {
                if ($scope.model.creationType && $scope.model.creationType == "create") {
                    $this.makeCategoryEditorVisible(true);
                } else {
                    $this.makeCategoryEditorVisible(false);
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
                        name: 'Bezeichnung',
                        field: 'name',
                        enableCellEdit: !window.isMobile(),
                        cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                            return 'first-cells ' + $this.getClassName(row);
                        }
                    },
                    {
                        name: 'Steuerklasse-Bezeichnung',
                        field: 'taxRate.name',
                        enableCellEdit: false,
                        cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                            return 'first-cells ' + $this.getClassName(row);
                        }
                    },
                    {
                        name: "Steuerklasse-Prozent",
                        field: 'taxRate.percentage',
                        enableCellEdit: false,
                        cellTemplate: '<div class="ui-grid-cell-contents">{{COL_FIELD + " %" }}</div>',
                        cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                            return 'first-cells ' + $this.getClassName(row);
                        }
                    }
                ],
                data: categoryService.findAll()
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
                if (categoryService.checkIfResourceInstance(rowEntity)) {
                    promise = categoryService.update(rowEntity);
                    $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise);
                } else {
                    promise = categoryService.save(rowEntity);
                    promise.$promise.then(function () {
                        $this.updateUiGridDataFromDatabase();
                    })
                }
                swal({
                    title: "Gespeichert",
                    text: "Die Warengruppe wurde erfolgreich gespeichert!",
                    type: "success",
                    timer: 2000,
                    showConfirmaButton: false
                });
            };
        }];
});