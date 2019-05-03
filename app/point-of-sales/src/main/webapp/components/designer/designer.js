//# sourceURL=pos.designer.designer.js
define([
        'angular',
        'pos.sweetAlert',
        "css!pos.designer.designer",
        "css!pos.fontAwesome",
        'json!components/designer/designerSettings/textDirectiveSettings/modes.json'
    ], function (angular, swal) {
        return ['$scope', '$rootScope', '$routeParams', '$compile', 'pos.designer.screenService', 'pos.designer.screenCollectionService', 'pos.designer.screenCollectionApiService', 'pos.initOptions',
            'pos.designer.designerSettings.designerSettingsService', 'pos.designer.styleService', '$location', 'uuid2', 'pos.dataService', 'pos.productService',
            '$timeout', 'pos.discountService', 'pos.printerService', 'pos.abstractStrategyFactory', 'pos.categoryService', '$uibModal',
            function (scope, rootScope, routeParams, compile, screenService, screenCollectionService, screenCollectionApiService, initOptions, designerSettingsService, styleService, location, uuid, dataService, productService,
                      timeout, discountService, printerService, abstractStrategyFactory, categoryService, uibModal) {
                rootScope.showCard = false;
                scope.selected = {
                    area: undefined
                };
                designerSettingsService.setDataByDirectiveAndKey(designerSettingsService.textController, "modes", require("json!components/designer/designerSettings/textDirectiveSettings/modes.json"));
                abstractStrategyFactory.changeState(abstractStrategyFactory.states.doNothing);
                productService.setAllProducts();
                categoryService.setAllCategories();
                scope.screenOptions = {
                    portrait: [
                        {value: 'S'},
                        {value: 'M'},
                        {value: 'L'}
                    ],
                    landscape: [
                        {value: 'S'},
                        {value: 'M'},
                        {value: 'L'},
                        {value: 'XL'}
                    ]
                };
                scope.screenSettings = {
                    css: {},
                    sizeOptions: {
                        showTicksValues: true,
                        stepsArray: [
                            {value: 'S'},
                            {value: 'M'},
                            {value: 'L'},
                            {value: 'XL'}
                        ],
                        onChange: function (id) {
                            scope.changeSize();
                        },
                    }
                };

                if (routeParams.collectionName) {
                    screenCollectionService.current = screenCollectionApiService.findByName(routeParams.collectionName);
                    scope.screenCollection = screenCollectionService.current;
                    scope.currentScreen = undefined;
                } else {
                    scope.screenCollection = screenCollectionService.new(undefined);
                    scope.currentScreen = undefined;
                }
                scope.$on('$destroy', function () {
                    rootScopeEvents.globalCssUpdate();
                    rootScopeEvents.screenSetUpdate();
                    rootScopeEvents.screenUpdate();
                    rootScopeEvents.buttonUpdate();
                });
                var rootScopeEvents = {};

                rootScopeEvents.globalCssUpdate = rootScope.$on(styleService.serviceName + "-" + styleService.keys.globalCssKey + "-updated", function () {
                    var parentCss = styleService.getGlobalCssData();
                    var screenSetCss = styleService.getScreenSetCssData();
                    if (!screenSetCss) {
                        screenSetCss = {};
                    }
                    angular.forEach(parentCss, function (directive, directiveName) {
                        angular.forEach(directive.data, function (functionData, functionName) {
                            angular.forEach(functionData.data, function (cssItem, cssItemName) {
                                if (!screenSetCss) {
                                    screenSetCss = {};
                                }
                                if (!screenSetCss[directiveName]) {
                                    screenSetCss[directiveName] = {};
                                    screenSetCss[directiveName].data = {};
                                }
                                if (!screenSetCss[directiveName].data[functionName]) {
                                    screenSetCss[directiveName].data[functionName] = {};
                                    screenSetCss[directiveName].data[functionName].data = {};
                                }
                                if (!screenSetCss[directiveName].data[functionName].data[cssItemName]) {
                                    screenSetCss[directiveName].data[functionName].data[cssItemName] = {};
                                    screenSetCss[directiveName].data[functionName].data[cssItemName].data = {};
                                    screenSetCss[directiveName].data[functionName].data[cssItemName].data.custom = {};
                                    screenSetCss[directiveName].data[functionName].data[cssItemName].data.parent = {};
                                    screenSetCss[directiveName].data[functionName].data[cssItemName].data.customChecked = false;
                                }
                                screenSetCss[directiveName].data[functionName].data[cssItemName].data.parent = cssItem.data.customChecked ? cssItem.data.custom : cssItem.data.parent;

                            });
                        });
                    });
                    styleService.setScreenSetCssData(screenSetCss);
                });
                rootScopeEvents.screenSetUpdate = rootScope.$on(styleService.serviceName + "-" + styleService.keys.screenSetCssKey + "-updated", function () {
                    var parentCss = styleService.getScreenSetCssData();

                    angular.forEach(screenService.getScreenSetDeviceData(screenService.currentScreenSet.screenSetName, screenService.currentScreenSet.device), function (item, index) {
                        var screenCss = styleService.getScreenCssData(index);
                        var screenLabelCss = styleService.getDirectiveData(styleService.keys.screenLabelKey);
                        if (!screenCss) {
                            screenCss = {};
                        }
                        angular.forEach(parentCss, function (directive, directiveName) {
                            angular.forEach(directive.data, function (functionData, functionName) {
                                angular.forEach(functionData.data, function (cssItem, cssItemName) {
                                    if (screenLabelCss[directiveName] && screenLabelCss[directiveName].data && screenLabelCss[directiveName].data[functionName] && screenLabelCss[directiveName].data[functionName].data && screenLabelCss[directiveName].data[functionName].data[cssItemName]) {
                                        if (!screenCss[directiveName]) {
                                            screenCss[directiveName] = {};
                                        }
                                        if (!screenCss[directiveName].data) {
                                            screenCss[directiveName].data = {};
                                        }
                                        if (!screenCss[directiveName].data[functionName]) {
                                            screenCss[directiveName].data[functionName] = {};
                                        }
                                        if (!screenCss[directiveName].data[functionName].data) {
                                            screenCss[directiveName].data[functionName].data = {};
                                        }
                                        if (!screenCss[directiveName].data[functionName].data[cssItemName]) {
                                            screenCss[directiveName].data[functionName].data[cssItemName] = {};
                                            screenCss[directiveName].data[functionName].data[cssItemName].data = {};
                                            screenCss[directiveName].data[functionName].data[cssItemName].custom = {};
                                            screenCss[directiveName].data[functionName].data[cssItemName].customChecked = {};
                                        }
                                        screenCss[directiveName].data[functionName].data[cssItemName].data.parent = cssItem.data.customChecked ? cssItem.data.custom : cssItem.data.parent;
                                    }
                                });
                            });
                        });
                        styleService.setScreenCssData(screenCss, index);
                    });
                    styleService.updatePanels();
                    styleService.updateTexts();
                    styleService.updateGrids();
                    styleService.updateButtons();
                });

                rootScopeEvents.screenUpdate = rootScope.$on(styleService.serviceName + "-" + styleService.keys.screenCssKey + "-updated", function (event, screenName) {
                    var screenCss = styleService.getScreenCssData(screenName);
                    if (screenCss) {
                        scope.css.screen = screenCss["screen"].data["general"].data["screen"].data.customChecked ? screenCss["screen"].data["general"].data["screen"].data.custom : screenCss["screen"].data["general"].data["screen"].data.parent;
                    }
                });
                rootScopeEvents.buttonUpdate = rootScope.$on(styleService.serviceName + "-" + styleService.keys.buttonCssKey + "-updated", function () {
                    styleService.updatePanels();
                    styleService.updateTexts();
                    styleService.updateGrids();
                    styleService.updateButtons();
                });

                scope.showNewElementsDialog = function (ev) {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/designer/designerOptions/newElements/newElements.html',
                        controller: 'pos.designer.designerOptions.newElements',
                        size: 'lg',
                        appendTo: angular.element("body"),
                        resolve: {}
                    });
                    modalInstance.result.then(function (result) {
                        scope.currentScreen.data.push(result);
                    });
                };
                scope.showOpenCollectionDialog = function (ev) {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/designer/designerOptions/openCollection/openCollection.html',
                        controller: 'pos.designer.designerOptions.openCollection',
                        size: 'lg',
                        appendTo: angular.element("body"),
                        resolve: {}
                    });
                };
                scope.addScreen = function () {
                    swal({
                            title: "Namen eingeben!",
                            text: "Geben Sie einen Namen für die neue Oberfläche ein!",
                            type: "input",
                            showCancelButton: true,
                            closeOnConfirm: false,
                            animation: "slide-from-top",
                            inputPlaceholder: "Name"
                        },
                        function (inputValue) {
                            if (inputValue === false) return false;

                            if (inputValue === "") {
                                swal.showInputError("Der Name darf nicht leer sein!");
                                return false
                            }
                            let screen = screenService.new(inputValue);
                            scope.selectScreen(screen);
                            swal("Nice!", "Der screen " + inputValue + " wurde erstellt", "success");
                        });
                };
                scope.selectScreen = function (screen) {
                    scope.currentScreen = screen;
                    scope.screenSettings.css = designerSettingsService.getDimension(scope.currentScreen.settings.orientation == "PORTRAIT", scope.currentScreen.settings.size, angular.element(".designer-live-view-parent").width(), angular.element(".designer-live-view-parent").height());
                    scope.selected.area = undefined;
                    if (scope.currentScreen.settings.orientation == "PORTRAIT") {
                        scope.screenSettings.sizeOptions.stepsArray = scope.screenOptions.portrait;
                    } else {
                        scope.screenSettings.sizeOptions.stepsArray = scope.screenOptions.landscape;
                    }
                    rootScope.$emit(screenService.serviceName + "-area-selected");
                };
                scope.changeOrientation = function () {
                    let width = angular.element(".designer-live-view-parent").width();
                    let height = angular.element(".designer-live-view-parent").height();
                    if (scope.currentScreen.settings.orientation != "PORTRAIT") {
                        scope.screenSettings.sizeOptions.stepsArray = scope.screenOptions.portrait;
                    } else {
                        scope.screenSettings.sizeOptions.stepsArray = scope.screenOptions.landscape;
                    }
                    scope.screenSettings.css = designerSettingsService.getDimension(scope.currentScreen.settings.orientation != "PORTRAIT", scope.currentScreen.settings.size, width, height);

                    timeout(function () {
                        angular.forEach(scope.currentScreen.data, function (item, index) {
                            rootScope.$emit(screenService.serviceName + "-" + item.uuid);
                        });
                    }, 10);
                };

                scope.changeSize = function () {
                    let width = angular.element(".designer-live-view-parent").width();
                    let height = angular.element(".designer-live-view-parent").height();
                    scope.screenSettings.css = designerSettingsService.getDimension(scope.currentScreen.settings.orientation == "PORTRAIT", scope.currentScreen.settings.size, width, height);

                    timeout(function () {
                        angular.forEach(scope.currentScreen.data, function (item, index) {
                            rootScope.$emit(screenService.serviceName + "-" + item.uuid);
                        });
                    }, 10);
                };

                scope.saveScreenCollection = function () {
                    if (routeParams.collectionName) {
                        screenCollectionApiService.save(screenCollectionService.current).$promise.then(function () {
                            if (screenCollectionService.current.standart) {
                                screenCollectionApiService.setDefault(screenCollectionService.current.name);
                            }
                        });
                    } else {
                        swal({
                                title: "Namen eingaben",
                                text: "Bitte den Namen der Sammlung eingaben!",
                                type: "input",
                                showCancelButton: true,
                                closeOnConfirm: false,
                                animation: "slide-from-top",
                                inputPlaceholder: "Name"
                            },
                            function (inputValue) {
                                if (inputValue === false) return false;

                                if (inputValue === "") {
                                    swal.showInputError("Der Name darf nicht leer sein!");
                                    return false
                                }
                                screenCollectionService.current.name = inputValue;
                                screenCollectionApiService.save(screenCollectionService.current).$promise.then(function () {
                                    if (screenCollectionService.current.standart) {
                                        screenCollectionApiService.setDefault(screenCollectionService.current.name).then(function () {
                                            location.path("/designer/" + screenCollectionService.current.name);
                                        });
                                    } else {
                                        location.path("/designer/" + screenCollectionService.current.name);
                                    }
                                });
                                swal("Oberfläche erstellt", "Die Oberfläche " + inputValue + " wurde erstellt", "success");
                            });
                    }
                };
                scope.showSettings = function (design) {
                    uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/designer/designerSettings/' + toCamelCase(scope.selected.area.settingsName) + "Settings/" + toCamelCase(scope.selected.area.settingsName) + "Settings.html",
                        controller: "pos.designer.designerSettings." + toCamelCase(scope.selected.area.settingsName) + "Settings",
                        size: 'lg',
                        appendTo: angular.element("body"),
                        resolve: {area: scope.selected.area, options: {showDesignFirst: design}},
                        windowClass: 'grid-directive-settings-modal'
                    });
                };
                scope.deleteArea = function () {
                    screenService.deleteByUuid(scope.selected.area.uuid);
                };

                scope.showCollectionSettings = function (event) {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/designer/designerOptions/collectionSettings/collectionSettings.html',
                        controller: "pos.designer.designerOptions.collectionSettings",
                        size: 'lg',
                        appendTo: angular.element("body"),
                        resolve: {
                            modal: {
                                stores: screenCollectionService.current.settings.stores,
                                default: screenCollectionService.current.standart
                            }
                        },
                    });
                    modalInstance.result.then(function (selectedItem) {
                        screenCollectionService.current.standart = selectedItem.default;
                        screenCollectionService.current.settings.stores = selectedItem.stores;
                    });
                };
                scope.showScreenSettings = function (event) {
                    var modalInstance = uibModal.open({
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: initOptions.baseDir + 'components/designer/designerOptions/screenSettings/screenSettings.html',
                        controller: "pos.designer.designerOptions.screenSettings",
                        size: 'lg',
                        appendTo: angular.element("body"),
                        resolve: {
                            modal: {
                                screen: scope.currentScreen
                            }
                        },
                    });
                    modalInstance.result.then(function (selectedItem) {
                        scope.currentScreen.settings.function = selectedItem.value;
                    });
                };
                scope.publish = function () {
                    screenCollectionApiService.publish(screenCollectionService.current.name);
                };
            }
        ];
        function toCamelCase(s) {
            return s.replace(/[-_]([a-z])/g, function (g) {
                return g[1].toUpperCase();
            })
        }
    }
);