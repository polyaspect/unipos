//# sourceURL=pos.design.screenSetSettings.js
define([
    'angular',
    "css!pos.design.screenSetSettings"
], function (angular) {
    return ['$scope', '$mdDialog', 'pos.design.styleService',
        function (scope, mdDialog, styleService) {
            scope.directives = angular.copy(styleService.getDirectiveData(styleService.keys.screenSetLabelKey));
            scope.functions = undefined;
            var customCss = angular.copy(styleService.getScreenSetCssData());
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
                styleService.setScreenSetCssData(scope.directives);
                mdDialog.hide();
            };
        }];
});