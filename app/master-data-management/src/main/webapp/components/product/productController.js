//# sourceURL=productController.js
define([
        'angular',
        'data.linq',
        'data.angularResource',
        'data.sweetAlert',
        'css!data.sweetAlertStyle',
        'css!data.productStyle',
        'css!data.jkLayoutCss'
    ], function (angular, Enumerable) {
        return ["$scope", "$rootScope", "uiGridConstants", "$resource", "productService", "categoryService", "taxRateService", "storeService", "companyService", "$rootScope", '$q', '$uibModal', 'initOptions',
            function ($scope, rootScope, uiGridConstants, $resource, productService, categoryService, taxRateService, storeService, companyService, $rootScope, $q, uibModal, initOptions) {
                var $this = this;
                $this.storeGuids = null;
                //Setup a default Product Object
                $this.defaultProduct = {};
                //Initialise the default Objects
                $scope.buttonDisabled = true;
                $scope.model = {};
                $scope.model.category = {};
                $scope.selectedCompany = 'all';

                companyService.findAll().$promise.then(function (data) {
                    $scope.companies = Enumerable.from(data).where(function (x) {
                        return x.stores && x.stores.length > 0
                    }).toArray();
                    $scope.companies.unshift({guid: 'all', name: "Alle"});
                    $scope.selectedCompany = 'all';
                });

                storeService.findAllStoreGuids().$promise.then(function (data) {
                    $this.storeGuids = data;
                });

                $scope.showCreateProductDialog = function (resetCreateProductForm) {
                    if (resetCreateProductForm) {
                        $scope.resetForm();
                    }
                    if ($this.checkIfOnlyOneObjectIsPresent("create") == 0) {
                        $this.addPlaceHolderToThetable();
                    } else {
                        $scope.gridOptions.data.pop();
                        $this.addPlaceHolderToThetable();
                    }
                    $this.makeProductEditorVisible(true);
                    $scope.gridApi.selection.selectRow($scope.gridOptions.data[$scope.gridOptions.data.length - 1]);
                };

                $this.makeProductEditorVisible = function (create) {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/product/editor/productEditor.html',
                        controller: 'ProductEditorController',
                        size: 'lg',
                        resolve: {
                            model: $scope.model,
                            defaultProduct: $this.defaultProduct,
                            create: create
                        }
                    });
                    modalInstance.result.then(function (createOrUpdate) {
                        if (createOrUpdate) {
                            $scope.saveRow($scope.model)
                        } else {
                            $this.updateUiGridDataFromDatabase();
                        }
                        //$scope.resetForm();
                    });
                    modalInstance.closed.then(function () {
                        if ($this.checkIfOnlyOneObjectIsPresent("create") == 1) {
                            $scope.gridOptions.data.pop();
                        }
                    })
                };

                //Find out the next highest ProductNumber with the Help of the API
                $this.setNextProductNumber = function () {
                    var nextProductNumber = productService.getNextValidProductNumber();
                    nextProductNumber.$promise.then(function (data) {
                        $this.defaultProduct.number = data.product.number + 1;
                    });
                    var maxSortOrder = productService.getmaxSortOrder();
                    maxSortOrder.then(function (response) {
                        $scope.model.sortOrder = response.data + 1;
                        $this.defaultProduct.minSortOrder = response.data + 1;
                    });
                };
                //Update the UI Grid Datasource by invoking the Promise;
                $this.updateUiGridDataFromDatabase = function () {
                    if ($scope.selectedCompany === 'all') {
                        productService.findAll().$promise.then(function (data) {
                            $this.setNextProductNumber();
                            $scope.gridOptions.data = Enumerable.from(data).orderBy(function (x) {
                                return x.sortOrder;
                            }).toArray();
                            storeService.findAllStoreGuids().$promise.then(function (data) {
                                $this.storeGuids = data;
                            });
                        });
                    } else {
                        productService.findbyCompanyGuid($scope.selectedCompany).$promise.then(function (data) {
                            $this.setNextProductNumber();
                            $scope.gridOptions.data = Enumerable.from(data).orderBy(function (x) {
                                return x.sortOrder;
                            }).toArray();
                            storeService.findStoreGuidsByCompanyGuid($scope.selectedCompany).$promise.then(function (data) {
                                $this.storeGuids = data;
                            });
                        });
                    }
                };
                $this.updateUiGridDataFromDatabase();
                $this.checkIfOnlyOneObjectIsPresent = function (objectType) {
                    var data = $scope.gridOptions.data;
                    var count = 0;
                    angular.forEach(data, function (element) {
                        if (!productService.checkIfResourceInstance(element) && element.creationType == objectType) {
                            count++;
                        }
                    });
                    return count;
                };

                $this.getClassName = function (row) {
                    var className = "";
                    if (!productService.checkIfResourceInstance(row.entity)) {
                        className = "blue";
                    }
                    return className;
                };


                $this.removeUiGridSelectionColor = function () {
                    $.each($(".ui-grid-cell-focus"), function (index, data) {
                        angular.element(data).removeClass("ui-grid-cell-focus")
                    });
                };

                $scope.openSelectedArticle = function () {
                    if ($scope.model.creationType && $scope.model.creationType == "create") {
                        $this.makeProductEditorVisible(true);
                    } else {
                        $this.makeProductEditorVisible(false);
                    }
                };

                $this.addPlaceHolderToThetable = function () {
                    $scope.gridOptions.data.push({
                        "number": "",
                        "name": "",
                        "price": "",
                        "customPriceInputAllowed": false,
                        "stores": $this.storeGuids,
                        "category.name": "",
                        "creationType": "create"
                    });
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
                            var promise = productService.publishChanges();
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
                            text: "Dadurch werden alle unveröffentlichten Änderungen verworfen!",
                            type: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#388E3C",
                            confirmButtonText: "Änderungen verwerfen",
                            cancelButtonText: "Abbrechen",
                            closeOnConfirm: false,
                            closeOnCancel: false
                        }, function (isConfirm) {
                            if (isConfirm) {
                                var promise = productService.resetChanges();
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
                                    text: "Ihre vorgenommen Änderungen wurden NICHT verworfen",
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
                    $this.setNextProductNumber();
                    $scope.buttonDisabled = true;
                };

                //UI Grid Options and Settings...
                $scope.gridOptions = {
                    rowTemplate: '<div grid="grid" class="ui-grid-draggable-row" draggable="true"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader, \'custom\': true }" ui-grid-cell></div></div>',
                    enableFiltering: true,
                    enableSorting: true,
                    enableRowSelection: true,
                    enableRowHeaderSelection: false,
                    multiSelect: false,
                    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
                    enableVerticalScrollbar: uiGridConstants.scrollbars.WHEN_NEEDED,
                    enableSelectAll: false,
                    columnDefs: [
                        {
                            name: 'Nummer', field: 'number', enableCellEdit: true,
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
                            name: 'Preis',
                            field: 'price',
                            cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity[col.field] == undefined ? "---Manuelle Eingabe---" : (row.entity[col.field]| currency:\'EUR: \')}}</div>',
                            enableCellEdit: !window.isMobile(),
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Warengruppe',
                            field: 'category.name',
                            enableCellEdit: false,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Reihenfolge',
                            field: 'sortOrder',
                            cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity[col.field] > 0? row.entity[col.field]:""}}</div>',
                            enableCellEdit: true,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            },
                            sortingAlgorithm: function (a, b) {
                                if (a == b) return 0;
                                if (a == "") return 1;
                                if (b == "") return -1;
                                if (a < b) return -1;
                                if (a > b) return 1;
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
                    if (productService.checkIfResourceInstance(rowEntity)) {
                        rowEntity.price = parseFloat(rowEntity.price);
                        var promise = productService.update(rowEntity);
                        //$scope.gridApi.rowEdit.setSavePromise(rowEntity, promise);
                        promise.then(function () {
                            $this.updateUiGridDataFromDatabase();
                        });
                    }
                    else {
                        var promise = productService.save(rowEntity);
                        promise.$promise.then(function () {
                            $this.updateUiGridDataFromDatabase();
                        })
                    }
                    swal({
                        title: "Gespeichert",
                        text: "Der Artikel wurde erfolgreich gespeichert! Um die Änderungen zu aktivieren, drücken Sie bitte auf 'Freigeben'.",
                        type: "success",
                        timer: 5000,
                        showConfirmaButton: false
                    });
                };
                $scope.selectionChanged = function () {
                    $this.updateUiGridDataFromDatabase();
                    $scope.resetForm();
                    $scope.buttonDisabled = true;
                };
            }
        ]
            ;
    }
)
;