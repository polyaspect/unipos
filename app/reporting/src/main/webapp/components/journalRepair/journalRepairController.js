//# sourceURL=reviewController.js
define([
        'angular',
        'report.angularResource',
        'report.reviewService',
        'report.sweetAlert',
        'css!report.sweetAlertStyle',
        'css!report.materialTable',
        'css!report.reviewStyle'
    ], function () {
        return ["$scope", "$rootScope", "uiGridConstants", "$resource", "$uibModal", "initOptions", "reviewService",
            function ($scope, rootScope, uiGridConstants, $resource, uibModal, initOptions, journalReviewService) {
                var $this = this;
                $scope.buttonDisabled = true;

                $scope.filters = ["--- KEIN FILTER ---", "StoreID ungleich CashierID"];

                $this.updateUiGridDataFromDatabase = function () {
                    if ($scope.selectedFilter == "--- KEIN FILTER ---") {
                        $scope.gridOptions.data = journalReviewService.findAll();
                    } else if ($scope.selectedFilter == "StoreID ungleich CashierID"){
                        $scope.gridOptions.data = journalReviewService.findWhereStoreIDNotEqualsCashierID()
                    }
                };

                $scope.makeEditorVisible = function () {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/review/editor/reviewEditor.html',
                        controller: 'ReviewEditorController',
                        size: 'md',
                        resolve: {
                            model: $scope.model
                        }
                    });
                    modalInstance.result.then(function () {
                        $this.updateUiGridDataFromDatabase();
                    });
                };

                $this.getClassName = function (row) {
                    var className = "";
                    if (row.entity.reverted === true) {
                        className = "reverted";
                    }
                    return className;
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
                            name: 'RNr',
                            field: 'invoiceId',
                            enableCellEdit: false,
                            width: 70,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            },
                            sort: {
                                direction: uiGridConstants.DESC,
                                priority: 1
                            },
                            sortingAlgorithm: function (a, b) {
                                if (a == b) return 0;
                                if (a == "") return 1;
                                if (b == "") return -1;
                                if (a < b) return -1;
                                if (a > b) return 1;
                            }
                        },
                        {
                            name: 'Umsatz', field: 'turnoverGross', enableCellEdit: false,
                            cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity[col.field]| currency:\'EUR: \'}}</div>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'CashierId', field: 'cashier.workerId', enableCellEdit: false,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Username', field: 'cashier.name', enableCellEdit: false,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'StoreId', field: 'store.storeId', enableCellEdit: false,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Company', field: 'company.name', enableCellEdit: false,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },,
                        {
                            name: 'DeviceId', field: 'deviceId', enableCellEdit: false,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Datum', field: 'creationDate', enableCellEdit: false,
                            cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity[col.field]| date : "dd.MM.yyyy"}}</div>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            },
                            width: 100
                        },
                        {
                            name: 'Uhrzeit', field: 'creationDate', enableCellEdit: false,
                            cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity[col.field]| date : "H:mm"}}</div>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            },
                            width: 50

                        },
                        {
                            name: 'Bemerkung', field: 'reverted', enableCellEdit: false,
                            cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity[col.field] ? "storniert" : ""}}</div>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        }
                    ],
                    data: journalReviewService.findAll()
                };

                $scope.gridOptions.onRegisterApi = function (gridApi) {
                    //set gridApi on scope
                    $scope.gridApi = gridApi;
                    gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                        if (row.isSelected) {
                            $scope.model = row.entity;
                            $scope.buttonDisabled = false; //This attribute controls, if the user is able to click the Buttons on the ProductIndexView
                        } else {
                            $scope.buttonDisabled = true;
                        }
                    });
                    gridApi.cellNav.on.navigate($scope, function (newRowCol, oldRowCol) {
                        var rowEntity = newRowCol.row.entity;
                        $scope.gridApi.selection.selectRow(rowEntity);
                    });
                };

                $scope.selectionChanged = function () {
                    $this.updateUiGridDataFromDatabase();
                };
            }]
    }
)
;