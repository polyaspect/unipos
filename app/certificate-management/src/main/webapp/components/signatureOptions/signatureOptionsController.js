//# sourceURL=signatureOptionsController.js
define([
    'signature.sweetAlert',
        'angular',
        'signature.angularResource',
        'css!signature.signatureStyle',
        'css!signature.sweetAlertStyle'
    ], function (swal, angular) {
        return ["$scope", "$rootScope", "uiGridConstants", "$resource", "$uibModal", "initOptions", "signatureOptionsService",
            function ($scope, rootScope, uiGridConstants, $resource, uibModal, initOptions, signatureService) {
                var $this = this;
                //Initialise the default Objects
                $scope.buttonDisabled = true;
                $scope.model = {};

                //Update the UI Grid Datasource by invoking the Promise;
                $this.updateUiGridDataFromDatabase = function () {
                    $scope.gridOptions.data = signatureService.findAll();
                    $scope.gridApi.selection.clearSelectedRows()
                };

                $this.checkIfOnlyOneObjectIsPresent = function (objectType) {
                    var data = $scope.gridOptions.data;
                    var count = 0;
                    angular.forEach(data, function (element) {
                        if (!signatureService.checkIfResourceInstance(element) && element.creationType == objectType) {
                            count++;
                        }
                    });
                    return count;
                };

                $this.getClassName = function (row) {
                    var className = "";
                    if (!signatureService.checkIfResourceInstance(row.entity)) {
                        className = "blue";
                    }
                    return className;
                };

                $this.makeEditorVisible = function (create) {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/signatureOptions/editor/signatureOptionsEditor.html',
                        controller: 'SignatureOptionsEditorController',
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

                $this.addPlaceHolderToTheTable = function () {
                    $scope.gridOptions.data.push({
                        "secretKeyPlainText": "",
                        "rksSuite": "",
                        "rkSerialNo": "",
                        "signaturPin": "",
                        "kassaId": "",
                        "creationType": "create"
                    });
                };
                $scope.openSelectedModal = function () {
                    if ($scope.model.creationType && $scope.model.creationType == "create") {
                        $this.makeEditorVisible(true);
                    } else {
                        $this.makeEditorVisible(false);
                    }
                };

                $scope.showCreateDialog = function (resetCreateProductForm) {
                    //First check if you don't habe an already existing signatureOption
                    //ToDo: Reimplement
                    /* signatureService.isFirstSignatureOptionForStore().then(function(result) {
                        if(result.data) { //ToDo: Doesn't work at the moment. Returns false, but just irgnores it --> form is show :-( */
                            if (resetCreateProductForm)
                                $scope.resetForm();
                            if ($this.checkIfOnlyOneObjectIsPresent("create") == 0) {
                                $this.addPlaceHolderToTheTable();
                            }
                            $this.makeEditorVisible(true);
                            $scope.gridApi.selection.selectRow($scope.gridOptions.data[$scope.gridOptions.data.length - 1]);
                        /*} else {
                            swal({title: "Signatur-Einstellung bereits vorhanden!", status: "warning"});
                            return;
                        }
                    });*/
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
                            name: 'Kassa-ID',
                            field: 'kassaId',
                            enableCellEdit: false,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'RKS-Suite',
                            field: 'rksSuite',
                            enableCellEdit: false,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'Zertifikat-Seriennummer ',
                            field: 'crtSerialNo',
                            enableCellEdit: false,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        }
                    ],
                    data: signatureService.findAll()
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
                    if (signatureService.checkIfResourceInstance(rowEntity)) {
                        promise = signatureService.update(rowEntity);
                        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise);
                    } else {
                        promise = signatureService.save(rowEntity);
                        promise.$promise.then(function () {
                            $this.updateUiGridDataFromDatabase();
                        })
                    }
                    swal({
                        title: "Gespeichert",
                        text: "Die Signatur-Einstellungen wurde erfolgreich gespeichert!",
                        type: "success",
                        timer: 2000,
                        showConfirmButton: false
                    });
                };
            }];
    }
);