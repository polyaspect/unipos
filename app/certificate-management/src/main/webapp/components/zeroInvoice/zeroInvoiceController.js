//# sourceURL=zeroInvoiceController.js
define([
    'signature.sweetAlert',
    'angular',
    'signature.angularResource',
    'signature.zeroInvoiceService',
    'css!signature.sweetAlertStyle',
    'css!signature.materialTable',
    'css!signature.reviewStyle'
], function (swal) {
    return ["$scope", "$rootScope", "uiGridConstants", "$resource", "$uibModal", "initOptions", "zeroInvoiceService",
        function ($scope, rootScope, uiGridConstants, $resource, uibModal, initOptions, zeroInvoiceService) {
            var $this = this;

            zeroInvoiceService.getCompanyGuidByAuthToken().then(function (data) {
                if (data.data != null && data.data != '') {
                    $scope.selectedCompany = data.data;
                } else {
                    $scope.selectedCompany = 'all';
                }
                $this.updateUiGridDataFromDatabase();
            });

            $this.updateUiGridDataFromDatabase = function () {
                $scope.gridOptions.data = zeroInvoiceService.findZeroInvoicesByCompanyGuid($scope.selectedCompany);
            };

            $scope.makeEditorVisible = function () {
                var modalInstance = uibModal.open({
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    templateUrl: initOptions.baseDir + 'components/zeroInvoice/editor/zeroInvoiceEditor.html',
                    controller: 'ZeroInvoiceEditorController',
                    size: 'md',
                    resolve: {
                        model: $scope.model
                    }
                });
                modalInstance.result.then(function () {
                    $this.updateUiGridDataFromDatabase();
                });
            };

            $scope.printStartInvoice = function () {
                zeroInvoiceService.createStartInvoice().$promise.then(function () {
                    swal({
                        type: 'success',
                        title: 'Erfolgreich',
                        text: "Der Startbeleg wurde erfolgreich erstellt."
                    });
                    $this.updateUiGridDataFromDatabase();
                }, function () {
                    swal({
                        type: 'error',
                        title: 'Fehlgeschlagen',
                        text: "Beim Erstellen des Startbelegs ist ein Fehler aufgetreten. Bitte überprüfen Sie die Logs."
                    });
                })
            };

            $scope.printZeroInvoice = function () {
                swal({
                    title: 'Nullbeleg auswählen',
                    input: 'select',
                    inputOptions: {
                        'NULL': "Allgemeiner Nullbeleg",
                        'SAMMEL': "Sammelbeleg",
                        'SCHLUSS': "Schlussbeleg",
                        'MONATS': "Monatsbeleg",
                        'JAHRES': "Jahresbeleg"
                    },
                    inputPlaceholder: 'Nullbeleg auwählen...',
                    showCancelButton: true
                }).then(function (result) {
                    zeroInvoiceService.createZeroInvoice(result).success(function () {
                        swal({
                            type: 'success',
                            title: 'Erfolgreich',
                            text: "Der Nullbeleg wurde erfolgreich erstellt"
                        });
                        $this.updateUiGridDataFromDatabase();
                    }).error(function () {
                        swal({
                            type: 'error',
                            title: 'Fehlgeschlagen',
                            text: "Beim Erstellen des Nullbeleges ist ein Fehler aufgetreten. Bitte überprüfen Sie die Logs."
                        });
                    });
                })
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
                        cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                            return 'first-cells ' + $this.getClassName(row);
                        },
                        sort: {
                            direction: uiGridConstants.DESC,
                            priority: 2
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
                        name: 'Datum', field: 'creationDate', enableCellEdit: false,
                        cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity[col.field]| date : "dd.MM.yyyy"}}</div>',
                        cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                            return 'first-cells ' + $this.getClassName(row);
                        },
                        sort: {
                            direction: uiGridConstants.DESC,
                            priority: 1
                        }
                    },
                    {
                        name: 'Uhrzeit', field: 'creationDate', enableCellEdit: false,
                        cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity[col.field]| date : "H:mm"}}</div>',
                        cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                            return 'first-cells ' + $this.getClassName(row);
                        }

                    },
                    {
                        name: 'Typ', field: 'signatureInvoiceType', enableCellEdit: false,
                        cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity[col.field]}}</div>',
                        cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                            return 'first-cells ' + $this.getClassName(row);
                        }
                    }
                ]
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
            }
        }
    ]
})
;