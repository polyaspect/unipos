//# sourceURL=pos.design.globalSettings.js
define([
    'angular',
    "css!pos.design.globalSettings"
], function (angular) {
    return ['$scope', '$mdDialog', 'pos.design.styleService',
        function (scope, mdDialog, styleService) {
            scope.directives = angular.copy(styleService.getDirectiveData(styleService.keys.globalLabelKey));
            scope.functions = undefined;
            scope.themeName = styleService.getThemeName();
            var customCss = angular.copy(styleService.getGlobalCssData());
            scope.themes = [];
            styleService.getAllThemes().then(function (response) {
                scope.themes = Enumerable.From(response.data).Select(function (x) {
                    return x.name;
                }).ToArray();
            });
            angular.forEach(scope.directives, function (directive, directiveName) {
                angular.forEach(directive.data, function (functionData, functionName) {
                    angular.forEach(functionData.data, function (cssItem, cssItemName) {
                        if (!customCss) {
                            customCss = {};
                        }
                        if (!customCss[directiveName]) {
                            customCss[directiveName] = {};
                            customCss[directiveName].data = {};
                        }
                        if (!customCss[directiveName].data[functionName]) {
                            customCss[directiveName].data[functionName] = {};
                            customCss[directiveName].data[functionName].data = {};
                        }
                        if (!customCss[directiveName].data[functionName].data[cssItemName]) {
                            customCss[directiveName].data[functionName].data[cssItemName] = {};
                            customCss[directiveName].data[functionName].data[cssItemName].data = {};
                        }
                        cssItem.data.checked = customCss[directiveName].data[functionName].data[cssItemName].data.customChecked || false;
                        cssItem.data.parent = customCss[directiveName].data[functionName].data[cssItemName].data.parent || {};
                        cssItem.data.custom = customCss[directiveName].data[functionName].data[cssItemName].data.custom || {};
                        cssItem.data.customText = Enumerable.From(cssItem.data.custom).Select(function (x) {
                            return x.Key + ":" + x.Value + "\n";
                        }).ToString();
                        cssItem.data.parentText = Enumerable.From(cssItem.data.parent).Select(function (x) {
                            return x.Key + ":" + x.Value + "\n";
                        }).ToString();
                    });
                });
            });
            scope.showFunctions = function (directive) {
                scope.functions = directive.data;
            };
            scope.selectTheme = function (theme) {
                styleService.getTheme(theme).then(function (response) {
                    var styleData = response.data.data;
                    angular.forEach(scope.css, function (item, index) {
                        item.parent = styleData[index];
                        item.parentText = Enumerable.From(item.parent).Select(function (x) {
                            return x.Key + ":" + x.Value + "\n";
                        }).ToString();
                    });
                })
            };
            scope.saveAsTheme = function () {
                if (scope.themeName && scope.themeName != "") {
                    var result = {};
                    angular.forEach(scope.css, function (item, index) {
                        result[index] = {};
                        if (item.checked) {
                            angular.forEach(item.customText.split("\n"), function (value, i) {
                                if (value.trim() != "") {
                                    result[index][value.split(":")[0]] = value.split(":")[1].replace(';', '');
                                }
                            });
                        } else {
                            result[index] = item.parent;
                        }
                    });
                    styleService.saveThemeData({
                        name: scope.themeName,
                        data: result
                    });
                    styleService.setThemeName(scope.themeName);
                    angular.forEach(scope.css, function (item, index) {
                        item.custom = {};
                        if (item.checked) {
                            item.parent = {};
                            angular.forEach(item.customText.split("\n"), function (value, i) {
                                if (value.trim() != "") {
                                    item.parent[value.split(":")[0]] = value.split(":")[1].replace(';', '');
                                }
                            });
                            item.parentText = item.customText;
                            item.customText = "";
                            item.checked = false;
                        } else {
                            item.parent = item.parent;
                        }
                    });
                }
            };
            scope.hide = function () {
                mdDialog.hide();
            };
            scope.save = function () {
                angular.forEach(scope.directives, function (directive, directiveName) {
                    angular.forEach(directive.data, function (functionData, functionName) {
                        angular.forEach(functionData.data, function (cssItemData, cssItemName) {
                            cssItemData.data.customChecked = cssItemData.data.checked;
                            cssItemData.data.custom = {};
                            angular.forEach(cssItemData.data.customText.split("\n"), function (value, i) {
                                if (value.trim() != "") {
                                    cssItemData.data.custom[value.split(":")[0]] = value.split(":")[1].replace(';', '');
                                }
                            });
                            delete cssItemData.data.checked;
                            delete cssItemData.data.parentText;
                            delete cssItemData.data.customText;
                        });
                    });
                });
                styleService.setGlobalCssData(scope.directives);
                mdDialog.hide();
            };
        }];
});