//# sourceURL=pos.design.styleSettings
define([
    'angular',
    'css!pos.designer.designerOptions.styleSettings'
], function (angular) {
    return ['pos.initOptions', 'pos.designer.styleService', 'pos.designer.screenService', '$rootScope',
        function (initOptions, styleService, screenService, rootScope) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerOptions/styleSettings/styleSettings.html',
                link: function (scope, el, attrs) {
                    var customCss = scope.area.css;
                    if (!customCss || angular.equals({}, customCss)) {
                        scope.css = {};
                        angular.forEach(scope.parentCss, function (item, index) {
                            if (!scope.css[index]) {
                                scope.css[index] = {};
                            }
                            if (!scope.css[index].data) {
                                scope.css[index].data = {};
                            }
                            scope.css[index].displayText = item.displayText;
                            scope.css[index].data.customChecked = 'false';
                            scope.css[index].data.customText = "";
                            if (item.data.customChecked) {
                                scope.css[index].data.parent = item.data.custom;
                                scope.css[index].data.parentText = Enumerable.From(item.data.custom).Select(function (x) {
                                    return x.Key + ":" + x.Value + "\n";
                                }).ToString();
                            } else {
                                scope.css[index].data.parent = item.data.parent || {};
                                scope.css[index].data.parentText = Enumerable.From(item.data.parent || {}).Select(function (x) {
                                    return x.Key + ":" + x.Value + "\n";
                                }).ToString();
                            }
                        });
                    } else {
                        scope.css = angular.copy(customCss);
                        angular.forEach(scope.css, function (item, index) {
                            item.data.customText = Enumerable.From(item.data.custom).Select(function (x) {
                                return x.Key + ":" + x.Value + "\n";
                            }).ToString();
                            if (scope.parentCss[index]) {
                                item.displayText = scope.parentCss[index].displayText;
                                item.data.parentText = Enumerable.From(scope.parentCss[index].data.parent).Select(function (x) {
                                    return x.Key + ":" + x.Value + "\n";
                                }).ToString();
                            }
                        });
                    }

                    scope.$on("saveStyle-" + scope.area.uuid, function () {
                        let result = {};
                        angular.forEach(scope.css, function (item, index) {
                            result[index] = {};
                            result[index].data = {};
                            result[index].data.customChecked = true; //(item.data.customChecked === 'true');
                            result[index].data.parent = item.data.parent;
                            result[index].data.custom = {};
                            if (item.data.customText) {
                                angular.forEach(item.data.customText.split("\n"), function (value, i) {
                                    if (value.trim() != "") {
                                        result[index].data.custom[value.split(":")[0]] = value.split(":")[1].replace(';', '');
                                    }
                                });
                            }
                        });
                        scope.area.css = result;
                        rootScope.$emit(styleService.serviceName + "-" + styleService.keys.buttonCssKey + "-updated");
                        //styleService.setButtonCssData(scope.area.uuid, result);
                    });
                }
            }
        }];
});