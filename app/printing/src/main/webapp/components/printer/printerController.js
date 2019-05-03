//# sourceURL=printerController.js
define([
        'angular',
        'printer.angularResource',
        'printer.flowjs',
        'printer.sweetAlert',
        'css!printer.sweetAlertStyle',
        'css!printer.materialTable',
        'css!printer.printerStyle',
        'css!printer.jkLayoutCss'
    ], function () {
        return ["$scope", "uiGridConstants", "$resource", "$rootScope", "$uibModal", "initOptions", "printerService", "storeService",
            function ($scope, uiGridConstants, $resource, rootScope, uibModal, initOptions, printerService, storeService) {
                var $this = this;
                //Initialise the default Objects
                $scope.buttonDisabled = true;
                $scope.model = {};

                //Update the UI Grid Datasource by invoking the Promise;
                $this.updateUiGridDataFromDatabase = function () {
                    $scope.gridOptions.data = printerService.findAll();
                };

                $this.checkIfOnlyOneObjectIsPresent = function (objectType) {
                    var data = $scope.gridOptions.data;
                    var count = 0;
                    angular.forEach(data, function (element) {
                        if (!printerService.checkIfResourceInstance(element) && element.creationType == objectType) {
                            count++;
                        }
                    });
                    return count;
                };

                $this.getClassName = function (row) {
                    var className = "";
                    if (!printerService.checkIfResourceInstance(row.entity)) {
                        className = "blue";
                    }
                    return className;
                };

                $this.makeEditorVisible = function (create) {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/printer/editor/printerEditor.html',
                        controller: 'PrinterEditorController',
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
                        "typeName": "",
                        "ipAddress": "",
                        "charCount": 48,
                        "stores": [],
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
                $scope.duplicateSelectedRow = function () {
                    if ($this.checkIfOnlyOneObjectIsPresent("copy") < 1) {
                        var rows = $scope.gridApi.selection.getSelectedRows();
                        var row = rows.length > 0 ? rows[0] : null;
                        if (row != null) {
                            $scope.gridOptions.data.push({
                                "name": row.name,
                                "typeName": row.typeName,
                                "ipAddress": row.ipAddress,
                                "charCount": row.charCount,
                                "stores": row.stores,
                                "creationType": "copy"
                            });
                        }
                        $scope.model = $scope.gridOptions.data[$scope.gridOptions.data.length - 1];
                        $scope.gridApi.selection.selectRow($scope.gridOptions.data[$scope.gridOptions.data.length - 1]);
                        $this.makeEditorVisible(true);
                    }
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
                $scope.savePrinter = function () {
                    var promise = printerService.save($scope.printer);
                    promise.$promise.then(function (savedPrinter) {
                        $scope.model = savedPrinter;
                        if ($scope.printerLogo != null) {
                            printerService.uploadLogo($scope.printerLogo, $scope.model).$promise.then(function () {
                                $scope.gridOptions.data = printerService.findAll();
                                $scope.resetForm();
                                $this.makePrinterEditorInvisible();
                                swal({
                                    title: "Drucker erfolgreich angelegt",
                                    text: "\"" + savedPrinter.name + "\" kann jetzt verwendet werden!",
                                    type: "success",
                                    timer: 2000,
                                    showConfirmButton: true
                                });
                            }, function (error) {
                                swal({
                                    title: "Logo nicht eingespielt",
                                    text: "Das Logo konnte nicht erfolgreich in den Drucker eingespielt werden",
                                    type: "error"
                                })
                            });
                        } else {
                            $scope.gridOptions.data = printerService.findAll();
                            $scope.resetForm();
                            $this.makePrinterEditorInvisible();
                            swal({
                                title: "Drucker erfolgreich angelegt",
                                text: "\"" + savedPrinter.name + "\" kann jetzt verwendet werden!",
                                type: "success",
                                showConfirmButton: true,
                                timer: 5000
                            });
                        }
                    }, function (error) {
                        swal({
                            title: "Drucker nicht erstellt",
                            text: "Der von ihnen erstellte Drucker konnte nicht eingespielt werden. Überprüfen Sie nochmal das Formular.",
                            type: "error"
                        })
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
                        {field: 'id', visible: false},
                        {
                            name: 'Bezeichnung',
                            field: 'name',
                            enableCellEdit: true,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: 'TypenBezeichnung',
                            field: 'typeName',
                            enableCellEdit: true,
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: "Standard",
                            width: 50,
                            field: 'defaultPrinter',
                            enableCellEdit: false,
                            cellTemplate: '<input style="text-align: center;margin: 0 auto;display: block;margin-top: 0.6rem;" type="checkbox" ng-model="row.entity.defaultPrinter" disabled>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        },
                        {
                            name: "Typ",
                            field: 'connectionType',
                            enableCellEdit: false,
                            cellTemplate: '<div class="ui-grid-cell-contents" ng-model="row.entity.connectionType" >{{COL_FIELD == "NETWORK" ? "Netzwerk-Drucker" : COL_FIELD == "USB" ? "USB-Drucker" : COL_FIELD == "SERIAL" ? "Serieller-Drucker" : "?"}}</div>',
                            cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                return 'first-cells ' + $this.getClassName(row);
                            }
                        }
                    ],
                    data: printerService.findAll()
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
                    var image = rowEntity.printerLogo;
                    if (printerService.checkIfResourceInstance(rowEntity)) {
                        promise = printerService.update(rowEntity);
                        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise);
                    } else {
                        promise = printerService.save(rowEntity).$promise;
                    }
                    promise.then(function (savedPrinter) {
                        if (!printerService.checkIfResourceInstance(rowEntity)) {
                            $this.updateUiGridDataFromDatabase();
                        }
                        if (image != null) {
                            printerService.uploadLogo(image, rowEntity).$promise.then(function () {
                                swal({
                                    title: "Drucker erfolgreich angelegt",
                                    text: "\"" + savedPrinter.name + "\" kann jetzt verwendet werden!",
                                    type: "success",
                                    timer: 2000,
                                    showConfirmButton: true
                                });
                            }, function (error) {
                                swal({
                                    title: "Logo nicht eingespielt",
                                    text: "Das Logo konnte nicht erfolgreich in den Drucker eingespielt werden",
                                    type: "error"
                                });
                            });
                        } else {
                            swal({
                                title: "Drucker erfolgreich angelegt",
                                text: "\"" + savedPrinter.name + "\" kann jetzt verwendet werden!",
                                type: "success",
                                showConfirmButton: true,
                                timer: 5000
                            });
                        }
                    });
                };
            }];

        function CompanyStoreController($scope, $mdDialog, $timeout, companyService, productStores) {

            $scope.stores = [];
            var companies = [];
            var stores = [];

            companyService.findAll().$promise.then(function (data) {
                $scope.companies = data;
                if (Object.prototype.toString.call(productStores) === '[object Array]') {
                    if (productStores.length > 0) {
                        $timeout(function () {
                            $timeout(function () {
                                stores = productStores;
                                angular.forEach($scope.companies, function (fCompany) {
                                    var storePerCompanyCount = 0;
                                    angular.forEach(fCompany.stores, function (fStore) {
                                        angular.forEach(stores, function (storeGuid) {
                                            if (storeGuid === fStore.guid) {
                                                fStore.isSelected = true;
                                                fStore.companyName = fCompany.name;
                                                storePerCompanyCount++;
                                            }
                                        });
                                    });
                                    if (storePerCompanyCount > 0) {
                                        companies.push(fCompany.guid);
                                    }
                                    if (storePerCompanyCount == fCompany.stores.length) {
                                        fCompany.isSelected = true;
                                    }
                                });
                                $scope.$apply();
                            }, 0);
                            $timeout(function () {
                                angular.forEach(angular.element("#productCompanyList md-list-item button"), function (listItem) {
                                    if (angular.element(listItem).attr("ng-click") == "toggleShowStores($event, company)") {

                                        $timeout(function () {
                                            var companyGuid = angular.element(listItem).closest("md-list-item").data("companyguid");
                                            if (companies.indexOf(companyGuid) != -1) {
                                                angular.element(listItem).triggerHandler('click');
                                            }
                                        }, 0);
                                    }
                                }, 0);
                            });
                        }, 0);
                    }
                }
            });

            $scope.hide = function () {
                $mdDialog.hide();
            };

            $scope.save = function () {
                $mdDialog.hide(stores);
            };

            $scope.selectCompany = function ($event, company) {
                var target = angular.element($event.target).closest("md-list-item");
                var companyGuid = target.data("companyguid");
                if (company.isSelected) {
                    angular.element($event.target).closest("md-list-item").data("selected", false);
                    $scope.toggleShowStores($event, company);
                    angular.forEach(company.stores, function (store, key) {
                        angular.forEach($scope.stores, function (scopeStore) {
                            if (scopeStore.guid === store.guid) {
                                scopeStore.isSelected = true;
                            }
                        });
                        if (stores.indexOf(store.guid) == -1) {
                            stores.push(store.guid);
                        }
                    })
                } else {
                    angular.forEach(company.stores, function (store) {
                        angular.forEach($scope.stores, function (scopeStore) {
                            if (scopeStore.guid === store.guid) {
                                scopeStore.isSelected = false;
                            }
                        });
                        var index = stores.indexOf(store.guid);
                        stores.splice(index, 1);
                    });
                }
                //company.isSelected = !company.isSelected;
            };

            $scope.toggleShowStores = function ($event, company) {
                if (angular.element($event.target).closest("md-list-item").data("selected") == false) {
                    angular.forEach(company.stores, function (store, key) {
                        var count = 0;
                        angular.forEach($scope.stores, function (value, key) {
                            if (value.guid === store.guid) {
                                count++;
                            }
                        });
                        if (count === 0) {
                            store.companyName = company.name;
                            $scope.stores.push(store);
                        }
                    });
                    var element = angular.element($event.target).closest("md-list-item");
                    element.css("background-color", "#2ECC71");
                    angular.element($event.target).closest("md-list-item").data("selected", true);
                } else {
                    angular.forEach(company.stores, function (store, key) {
                        angular.forEach($scope.stores, function (scopeStore, key) {
                            if (scopeStore.guid === store.guid) {
                                scopeStore.isSelected = false;
                                var index = $scope.stores.indexOf(scopeStore);
                                if (index != -1) {
                                    $scope.stores.splice(index, 1);
                                }
                                index = stores.indexOf(scopeStore.guid);
                                if (index != -1) {
                                    stores.splice(index, 1);
                                }
                            }
                        })
                    });
                    var element = angular.element($event.target).closest("md-list-item");
                    element.css("background-color", "white");
                    angular.element($event.target).closest("md-list-item").data("selected", false);
                    company.isSelected = false;
                }
            };

            $scope.selectStore = function ($event, store) {
                if (store.isSelected == false) {
                    var index = stores.indexOf(store.guid);
                    stores.splice(index, 1);
                    angular.forEach($scope.companies, function (fCompany) {
                        if (fCompany.guid === store.companyGuid) {
                            var storesPerCompany = 0;
                            angular.forEach(fCompany.stores, function (fStore) {
                                if (fStore.isSelected == true) {
                                    storesPerCompany++;
                                }
                            });
                            if (storesPerCompany < fCompany.stores.length) {
                                fCompany.isSelected = false;
                            }
                        }
                    });
                } else {
                    angular.forEach($scope.companies, function (fCompany) {
                        if (fCompany.guid === store.companyGuid) {
                            var storesPerCompany = 0;
                            angular.forEach(fCompany.stores, function (fStore) {
                                if (fStore.isSelected == true) {
                                    storesPerCompany++;
                                }
                            });
                            if (storesPerCompany == fCompany.stores.length) {
                                fCompany.isSelected = true;
                            }
                        }
                    });
                    stores.push(store.guid);
                }
            };

            $scope.saveScope = function () {
                alert(stores.length);
            };
        }
    }
)
;