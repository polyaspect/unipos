//# sourceURL=pos.design.styleService.js
define([
    'angular'
], function (angular) {
    return ['pos.dataService', '$rootScope', 'pos.designer.screenService', '$http', function (dataService, rootScope, screenService, http) {
        this.serviceName = "design.styleService";
        var self = this;
        this.keys = {
            themeCssKey: "themeCss",
            globalCssKey: "globalCss",
            screenSetCssKey: "screenSetCss",
            screenCssKey: "screenCss",
            buttonCssKey: "buttonCssKey",
            functionCssKey: "functionCssKey",
            globalLabelKey: "globalLabelKey",
            screenSetLabelKey: "screenSetLabelKey",
            screenLabelKey: "screenLabelKey",
            buttonLabelKey: "buttonLabelKey"
        };
        dataService.setByServiceKey(self.serviceName, {});
        this.setDirectiveData = function (directive, data) {
            dataService.setByDirectiveKey(self.serviceName, directive, data);
        };

        self.data = {
            screen: {
                displayText: "Screen Style",
                data: {
                    general: {
                        displayText: "General",
                        data: {
                            screen: {
                                displayText: "Screen",
                                data: {}
                            }
                        }
                    }
                }
            },
            button: {
                displayText: "Button Style",
                data: {
                    general: {
                        displayText: "General",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    showDailySettlementFunction: {
                        displayText: "Show Daily Settlement Service Style",
                        serviceName: "pos.reportService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    closeDailySettlementFunction: {
                        displayText: "Close Daily Settlement Service Style",
                        serviceName: "pos.reportService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    abortDailySettlementFunction: {
                        displayText: "Close Daily Settlement Service Style",
                        serviceName: "pos.reportService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    productFunction: {
                        displayText: "Product Service Style",
                        serviceName: "pos.productService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    reversalFunction: {
                        displayText: "Storno Service Style",
                        serviceName: "pos.reversalService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    switchScreenFunction: {
                        displayText: "Screen Wechsel Style",
                        serviceName: "pos.switchScreenService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    discountFunction: {
                        displayText: "Discount Style",
                        serviceName: "pos.discountService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    paymentFunction: {
                        displayText: "Payment Service Style",
                        serviceName: "pos.paymentService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    loginFunction: {
                        displayText: "Login Service Style",
                        serviceName: "pos.loginService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    logoutFunction: {
                        displayText: "Logout Service Style",
                        serviceName: "pos.logoutService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    subTotalFunction: {
                        displayText: "Zwischensumme Service Style",
                        serviceName: "pos.subTotalService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    addNewDeviceFunction: {
                        displayText: "Gerät hinzufügen Style",
                        serviceName: "pos.addNewDeviceService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    showInvoicesFunction: {
                        displayText: "Rechnungen anzeigen",
                        serviceName: "pos.showInvoicesService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    adminMenuFunction: {
                        displayText: "Admin-Menü anzeigen",
                        serviceName: "pos.adminMenuService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    addPrinterToDeviceFunction: {
                        displayText: "Drucker zum Gerät hinzufügen",
                        serviceName: "pos.printerService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    enterFunction: {
                        displayText: "Enter Button",
                        serviceName: "pos.enterService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    cashBookFunction: {
                        displayText: "CashBook Button",
                        serviceName: "pos.cashbookEntryService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    showOpenOrdersFunction: {
                        displayText: "Offene Bestellungen anzeigen",
                        serviceName: "pos.showOpenOrdersService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    openOrderFunction: {
                        displayText: "Bestellungen öffnen",
                        serviceName: "pos.openOrderService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    splitOrderFunction: {
                        displayText: "Rechnung splitten",
                        serviceName: "pos.splitOrderService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    moveOrderFunction: {
                        displayText: "Rechnung verschieben",
                        serviceName: "pos.moveOrderService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    reassignOrderFunction: {
                        displayText: "Rechnung neu zuordnen",
                        serviceName: "pos.reassignOrderService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    splitPaymentFunction: {
                        displayText: "Zahlung splitten",
                        serviceName: "pos.splitPaymentService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    revertInvoiceFunction: {
                        displayText: "Rechnung stornieren",
                        serviceName: "pos.revertInvoiceButtonService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    },
                    reprintInvoiceFunction: {
                        displayText: "Rechnung nachdrucken",
                        serviceName: "pos.reprintInvoiceButtonService",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            },
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    }
                }
            },
            text: {
                displayText: "Text Style",
                data: {
                    general: {
                        displayText: "General",
                        data: {
                            text: {
                                displayText: "Text",
                                data: {}
                            }
                        }
                    }
                }
            },
            jkGrid: {
                displayText: "Grid Style",
                data: {
                    general: {
                        displayText: "General",
                        data: {
                            gridCss: {
                                displayText: "GridCss",
                                data: {}
                            },
                            evenRows: {
                                displayText: "Gerade Rows",
                                data: {}
                            },
                            oddRows: {
                                displayText: "Ungerade Rows",
                                data: {}
                            },
                            cell: {
                                displayText: "Zellen",
                                data: {}
                            },
                            selectedRow: {
                                displayText: "Ausgewählte Row",
                                data: {}
                            }
                        }
                    },
                    orderDirective: {
                        displayText: "order",
                        serviceName: "pos.orderDirectiveService",
                        data: {
                            gridCss: {
                                displayText: "GridCss",
                                data: {}
                            },
                            evenRows: {
                                displayText: "Gerade Rows",
                                data: {}
                            },
                            oddRows: {
                                displayText: "Ungerade Rows",
                                data: {}
                            },
                            cell: {
                                displayText: "Zellen",
                                data: {}
                            },
                            selectedRow: {
                                displayText: "Ausgewählte Row",
                                data: {}
                            }
                        }
                    },
                    storeChooser: {
                        displayText: "stores",
                        serviceName: "pos.jkStoreChooserService",
                        data: {
                            gridCss: {
                                displayText: "GridCss",
                                data: {}
                            },
                            evenRows: {
                                displayText: "Gerade Rows",
                                data: {}
                            },
                            oddRows: {
                                displayText: "Ungerade Rows",
                                data: {}
                            },
                            cell: {
                                displayText: "Zellen",
                                data: {}
                            },
                            selectedRow: {
                                displayText: "Ausgewählte Row",
                                data: {}
                            }
                        }
                    },
                    showInvoicesGrid: {
                        displayText: "Rechnungsliste",
                        serviceName: "pos.showInvoicesGridService",
                        data: {
                            gridCss: {
                                displayText: "GridCss",
                                data: {}
                            },
                            evenRows: {
                                displayText: "Gerade Rows",
                                data: {}
                            },
                            oddRows: {
                                displayText: "Ungerade Rows",
                                data: {}
                            },
                            cell: {
                                displayText: "Zellen",
                                data: {}
                            },
                            selectedRow: {
                                displayText: "Ausgewählte Row",
                                data: {}
                            }
                        }
                    },
                    reportPreviewGrid: {
                        displayText: "Reports anzeigen",
                        serviceName: "pos.reportPreviewGridService",
                        data: {
                            gridCss: {
                                displayText: "GridCss",
                                data: {}
                            },
                            evenRows: {
                                displayText: "Gerade Rows",
                                data: {}
                            },
                            oddRows: {
                                displayText: "Ungerade Rows",
                                data: {}
                            },
                            cell: {
                                displayText: "Zellen",
                                data: {}
                            },
                            selectedRow: {
                                displayText: "Ausgewählte Row",
                                data: {}
                            }
                        }
                    },
                    printerGrid: {
                        displayText: "Drucker anzeigen",
                        serviceName: "pos.printerGridService",
                        data: {
                            gridCss: {
                                displayText: "GridCss",
                                data: {}
                            },
                            evenRows: {
                                displayText: "Gerade Rows",
                                data: {}
                            },
                            oddRows: {
                                displayText: "Ungerade Rows",
                                data: {}
                            },
                            cell: {
                                displayText: "Zellen",
                                data: {}
                            },
                            selectedRow: {
                                displayText: "Ausgewählte Row",
                                data: {}
                            }
                        }
                    },
                    cashBookEntryGrid: {
                        displayText: "Cash Book Einträge",
                        serviceName: "pos.cashbookEntryGridService",
                        data: {
                            gridCss: {
                                displayText: "GridCss",
                                data: {}
                            },
                            evenRows: {
                                displayText: "IN",
                                data: {}
                            },
                            oddRows: {
                                displayText: "OUT",
                                data: {}
                            },
                            cell: {
                                displayText: "Zellen",
                                data: {}
                            },
                            selectedRow: {
                                displayText: "Ausgewählte Row",
                                data: {}
                            }
                        }
                    },
                    chooseOrderItemGrid: {
                        displayText: "Rechnungspositionen auswählen",
                        serviceName: "pos.cashbookEntryGridService",
                        data: {
                            gridCss: {
                                displayText: "Tabelle",
                                data: {}
                            },
                            evenRows: {
                                displayText: "Gerade Zeilen",
                                data: {}
                            },
                            oddRows: {
                                displayText: "Ungerade Zeilen",
                                data: {}
                            },
                            cell: {
                                displayText: "Zellen",
                                data: {}
                            },
                            selectedRow: {
                                displayText: "Ausgewählte Zeile",
                                data: {}
                            }
                        }
                    },
                    chooseOrderGrid: {
                        displayText: "Bestellungen auswählen",
                        serviceName: "pos.chooseOrderGridService",
                        data: {
                            gridCss: {
                                displayText: "Tabelle",
                                data: {}
                            },
                            evenRows: {
                                displayText: "Gerade Zeilen",
                                data: {}
                            },
                            oddRows: {
                                displayText: "Ungerade Zeilen",
                                data: {}
                            },
                            cell: {
                                displayText: "Zellen",
                                data: {}
                            },
                            selectedRow: {
                                displayText: "Ausgewählte Zeile",
                                data: {}
                            }
                        }
                    },
                    usersGrid: {
                        displayText: "Benutzer auswählen",
                        serviceName: "pos.usersGridService",
                        data: {
                            gridCss: {
                                displayText: "Tabelle",
                                data: {}
                            },
                            evenRows: {
                                displayText: "Gerade Zeilen",
                                data: {}
                            },
                            oddRows: {
                                displayText: "Ungerade Zeilen",
                                data: {}
                            },
                            cell: {
                                displayText: "Zellen",
                                data: {}
                            },
                            selectedRow: {
                                displayText: "Ausgewählte Zeile",
                                data: {}
                            }
                        }
                    }
                }
            },
            panel: {
                displayText: "Panel Style",
                data: {
                    general: {
                        displayText: "General",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            }
                        }
                    },
                    product: {
                        displayText: "Product",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            }
                        }
                    },
                    numpad: {
                        displayText: "Numpad",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            }
                        }
                    },
                    keyboard: {
                        displayText: "Keyboard",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            }
                        }
                    },
                    category: {
                        displayText: "Warengrupppe",
                        data: {
                            button: {
                                displayText: "Button",
                                data: {}
                            }
                        }
                    }
                }
            }
        };

        self.setDirectiveData(self.keys.globalLabelKey, angular.copy(self.data));
        self.setDirectiveData(self.keys.globalCssKey, angular.copy(self.data));

        self.buttonLabelKey = {
            productFunction: {
                displayText: "Product Service Style",
                serviceName: "pos.productService",
                data: {}
            },
            reversalFunction: {
                displayText: "Storno Service Style",
                serviceName: "pos.reversalService",
                data: {}
            },
            switchScreenFunction: {
                displayText: "Screen Wechsel Style",
                serviceName: "pos.switchScreenService",
                data: {}
            },
            paymentFunction: {
                displayText: "Payment Service Style",
                serviceName: "pos.paymentService",
                data: {}
            },
            loginFunction: {
                displayText: "Login Service Style",
                serviceName: "pos.loginService",
                data: {}
            },
            logoutFunction: {
                displayText: "Logout Service Style",
                serviceName: "pos.logoutService",
                data: {}
            },
            subTotalFunction: {
                displayText: "Zwischensumme Service Style",
                serviceName: "pos.subTotalService",
                data: {}
            },
            addNewDeviceFunction: {
                displayText: "Gerät hinzufügen Style",
                serviceName: "pos.addNewDeviceService",
                data: {}
            },
            showInvoicesFunction: {
                displayText: "Rechnungen anzeigen",
                serviceName: "pos.showInvoicesService",
                data: {}
            }
        };

        self.setDirectiveData(self.keys.buttonLabelKey, self.buttonLabelKey);
        self.setDirectiveData(self.keys.buttonCssKey, self.buttonCssKey);

        this.setServiceData = function (data) {
            dataService.setByServiceKey(self.serviceName, data);

            // Wurden neue Directives hinzugefügt, werden die Settings um leere Settings erweitert:

            var globalLabelKey = self.getDirectiveData(self.keys.globalLabelKey);
            var globalCssKey = self.getDirectiveData(self.keys.globalCssKey);
            var screenSetLabelKey = self.getDirectiveData(self.keys.screenSetLabelKey);
            var screenSetCssKey = self.getDirectiveData(self.keys.screenSetCssKey);
            var buttonLabelKey = self.getDirectiveData(self.keys.buttonLabelKey);

            $.extend(true, globalLabelKey, self.data);
            $.extend(true, globalCssKey, self.data);
            $.extend(true, screenSetLabelKey, self.data);
            $.extend(true, screenSetCssKey, self.data);
            $.extend(true, buttonLabelKey, self.buttonLabelKey);

            self.setDirectiveData(self.keys.globalLabelKey, globalLabelKey);
            self.setDirectiveData(self.keys.globalCssKey, globalCssKey);
            self.setDirectiveData(self.keys.screenSetLabelKey, screenSetLabelKey);
            self.setDirectiveData(self.keys.screenSetCssKey, screenSetCssKey);
            self.setDirectiveData(self.keys.buttonLabelKey, buttonLabelKey);
        };
        this.getDirectiveData = function (directive) {
            return dataService.getByDirectiveKey(self.serviceName, directive);
        };
        this.getAllThemes = function () {
            return http.get("/design/theme/list");
        };

        this.getTheme = function (themeName) {
            return http.get("/design/theme/findByName/" + themeName);
        };
        this.saveThemeData = function (data) {
            http.post("/design/theme/save", data);
        };
        this.setThemeName = function (themeName) {
            self.setDirectiveData(self.keys.themeCssKey, themeName);
        };
        this.getThemeName = function () {
            return self.getDirectiveData(self.keys.themeCssKey);
        };
        this.setGlobalCssData = function (data) {
            self.setDirectiveData(self.keys.globalCssKey, data);
            rootScope.$emit(self.serviceName + "-" + self.keys.globalCssKey + "-updated");
        };
        this.getGlobalCssData = function () {
            return self.getDirectiveData(self.keys.globalCssKey);
        };
        this.updateTexts = function () {
            angular.forEach(screenService.getScreens(), function (screen, index) {
                angular.forEach(screen.data, function (area, index2) {
                    if (area.name == "pos.ui.elements.text.text-directive") {
                        var textGlobalCss = self.getGlobalCssData().text.data.general.data;
                        if(!area.css){
                            area.css = {};
                        }
                        var customCss = area.css;
                        if (!area.data.css) {
                            area.data.css = {};
                        }
                        angular.forEach(textGlobalCss, function (item, index) {
                            if (item.data && item.data.customChecked) {
                                area.data.css[index] = item.data.custom;
                            }
                        });
                        angular.forEach(customCss, function (item, index) {
                            if (item.data && item.data.customChecked) {
                                area.data.css[index] = item.data.custom;
                            }
                        });
                    }
                });
            });
        };
        this.updatePanels = function () {
            angular.forEach(screenService.getScreens(), function (screen, index) {
                angular.forEach(screen.data, function (area, index2) {
                    if (area.name == "pos.ui.elements.panel.panel-directive") {
                        var panelGlobalCss = self.getGlobalCssData().panel.data.general.data;
                        var panelTypeGlobalCss = {};
                        if(!area.css){
                            area.css = {};
                        }
                        var customCss = area.css;

                        if (area.data && area.data.service.name == "pos.productPanelService") {
                            panelTypeGlobalCss = self.getGlobalCssData().panel.data.product.data;
                        }
                        if (area.data && area.data.service.name == "pos.numpadPanelService" && area.data.service.data == "getNumpadData") {
                            panelTypeGlobalCss = self.getGlobalCssData().panel.data.numpad.data;
                        }
                        if (area.data && area.data.service.name == "pos.numpadPanelService" && area.data.service.data == "getKeyboardData") {
                            panelTypeGlobalCss = self.getGlobalCssData().panel.data.keyboard.data;
                        }
                        if (area.data && area.data.service.name == "pos.categoryPanelService") {
                            panelTypeGlobalCss = self.getGlobalCssData().panel.data.category.data;
                        }
                        if (!area.data.css) {
                            area.data.css = {};
                        }
                        angular.forEach(panelGlobalCss, function (item, index) {
                            if (item.data && item.data.customChecked) {
                                area.data.css[index] = item.data.custom;
                            }
                        });
                        angular.forEach(panelTypeGlobalCss, function (item, index) {
                            if (item.data && item.data.customChecked) {
                                area.data.css[index] = item.data.custom;
                            }
                        });
                        angular.forEach(customCss, function (item, index) {
                            if (item.data && item.data.customChecked) {
                                area.data.css[index] = item.data.custom;
                            }
                        });
                    }
                });
            });
        };
        this.updateGrids = function () {
            angular.forEach(screenService.getScreens(), function (screen, index) {
                angular.forEach(screen.data, function (area, index2) {
                    if (area.name == "pos.ui.elements.grid.grid-directive") {
                        if (area.data) {
                            if (!area.data.css) {
                                area.data.css = {};
                            }
                            if(!area.css){
                                area.css = {};
                            }
                            var customCss = area.css;
                            var jkGridGlobalCss = self.getGlobalCssData().jkGrid.data.general.data;
                            var globalLabels = angular.copy(self.getDirectiveData(self.keys.globalLabelKey)).jkGrid.data;
                            var functionGlobalCss = Enumerable.From(self.getGlobalCssData().jkGrid.data).FirstOrDefault(undefined, function (x) {
                                var label = Enumerable.From(globalLabels).FirstOrDefault(undefined, function (y) {
                                    return y.Value.serviceName == area.data.service.name;
                                });
                                return x.Key == label.Key;
                            }).Value.data;

                            if (!area.data.css) {
                                area.data.css = {};
                            }
                            angular.forEach(jkGridGlobalCss, function (item, index) {
                                if (item.data && item.data.customChecked) {
                                    area.data.css[index] = item.data.custom;
                                }
                                if (item.data && !item.data.customChecked && !angular.equals({}, item.data.parent)) {
                                    area.data.css[index] = item.data.parent;
                                }
                            });
                            angular.forEach(functionGlobalCss, function (item, index) {
                                if (item.data && item.data.customChecked) {
                                    area.data.css[index] = item.data.custom;
                                }
                                if (item.data && !item.data.customChecked && !angular.equals({}, item.data.parent)) {
                                    area.data.css[index] = item.data.parent;
                                }
                            });
                            angular.forEach(customCss, function (item, index) {
                                if (item.data && item.data.customChecked) {
                                    area.data.css[index] = item.data.custom;
                                }
                            });
                        }
                    }
                });
            });
        };
        this.updateButtons = function () {
            var globalLabels = angular.copy(self.getDirectiveData(self.keys.globalLabelKey)).button.data;
            angular.forEach(screenService.getScreens(), function (screen, index) {
                angular.forEach(screen.data, function (area, index2) {
                    if (area.name == "pos.ui.elements.button.button-directive") {
                        if (area.data) {
                            if (!area.data.css) {
                                area.data.css = {};
                            }
                            var functionGlobalCss = Enumerable.From(self.getGlobalCssData().button.data).FirstOrDefault(undefined, function (x) {
                                var label = Enumerable.From(globalLabels).FirstOrDefault(undefined, function (y) {
                                    return y.Value.serviceName == area.data.serviceName;
                                });

                                return x.Key == label.Key;
                            }).Value.data;
                            if(!area.css){
                                area.css = {};
                            }

                            var buttonCss = area.css;
                            var buttonGlobalCss = self.getGlobalCssData().button.data.general.data;
                            angular.forEach(buttonGlobalCss, function (item, index) {
                                if (item.data && item.data.customChecked) {
                                    area.data.css[index] = item.data.custom;
                                }
                                if (item.data && !item.data.customChecked && !angular.equals({}, item.data.parent)) {
                                    area.data.css[index] = item.data.parent;
                                }
                            });
                            angular.forEach(functionGlobalCss, function (item, index) {
                                if (item.data && item.data.customChecked) {
                                    area.data.css[index] = item.data.custom;
                                }
                                if (item.data && !item.data.customChecked && !angular.equals({}, item.data.parent)) {
                                    area.data.css[index] = item.data.parent;
                                }
                            });
                            angular.forEach(buttonCss, function (item, index) {
                                if (item.data && item.data.customChecked) {
                                    area.data.css[index] = item.data.custom;
                                }
                            });
                        }
                    }
                });
            });
        };
    }];
});