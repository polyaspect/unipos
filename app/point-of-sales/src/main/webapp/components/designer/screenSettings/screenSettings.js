//# sourceURL=pos.design.screenSettings.js
define([
    'angular',
    "css!pos.design.screenSettings"
], function (angular) {
    return ['$scope', '$mdDialog', 'pos.design.styleService', '$rootScope',
        function (scope, mdDialog, styleService, rootScope) {
            scope.css = angular.copy(styleService.getDirectiveData(styleService.keys.screenLabelKey));
            var parentCss = styleService.getScreenSetCssData();

            if (parentCss) {
                angular.forEach(parentCss, function (item, index) {
                    scope.css[index] = {};
                    scope.css[index].checked = false;
                    scope.css[index].customText = "";
                    if (item.customChecked) {
                        scope.css[index].parent = item.custom;
                        scope.css[index].parentText = Enumerable.From(item.custom).Select(function (x) {
                            return x.Key + ":" + x.Value + "\n";
                        }).ToString();
                    } else {
                        scope.css[index].parent = item.parent;
                        scope.css[index].parentText = Enumerable.From(item.parent).Select(function (x) {
                            return x.Key + ":" + x.Value + "\n";
                        }).ToString();
                    }
                });
            }

            scope.hide = function () {
                mdDialog.hide();
            };
            scope.save = function () {
                var result = {};
                angular.forEach(scope.css, function (item, index) {
                    result[index] = {};
                    result[index].customChecked = item.checked;
                    result[index].custom = {};
                    angular.forEach(item.customText.split("\n"), function (value, i) {
                        if (value.trim() != "") {
                            result[index].custom[value.split(":")[0]] = value.split(":")[1].replace(';', '');
                        }
                    });
                    result[index].parent = item.parent;
                });
                styleService.setScreenCssData(result);
                mdDialog.hide();
            };
        }];
});