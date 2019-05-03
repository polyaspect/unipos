//# sourceURL=pos.designer.designerSettings.textDirectiveSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.textDirectiveSettings"
], function (angular) {
    return ['$rootScope', '$scope', '$uibModalInstance', 'pos.designer.screenService', "pos.designer.designerSettings.designerSettingsService", 'pos.designer.styleService', 'area', 'options',
        function (rootScope, scope, uibModalInstance, screenService, designerSettingsService, styleService, area, options) {
            scope.modelOptions = options;
            scope.area = area;
            scope.model = {};
            scope.modes = angular.copy(designerSettingsService.getDataByDirectiveAndKey(designerSettingsService.textController, "modes"));
            var textDirectiveAreaConfig = scope.area;
            if (textDirectiveAreaConfig && textDirectiveAreaConfig.data) {
                scope.model.id = textDirectiveAreaConfig.data.id;
                scope.model.text = textDirectiveAreaConfig.data.text;
                scope.model.staticText = textDirectiveAreaConfig.data.staticText ? "true" : "false";
                angular.forEach(scope.modes, function (item, index) {
                    angular.forEach(textDirectiveAreaConfig.data.modes, function (item2, index2) {
                        if (item.name == item2.name) {
                            item.checked = true;
                        }
                    });
                });
            }
            scope.parentCss = styleService.getGlobalCssData().text.data.general.data || {};
            scope.hide = function () {
                uibModalInstance.close();
            };
            scope.selectSourceItem = function (item) {
                item.checked = !item.checked;
            };
            scope.save = function () {
                var textDirectiveAreaConfig = scope.area;
                textDirectiveAreaConfig.data = {};
                textDirectiveAreaConfig.data.id = scope.model.id;
                textDirectiveAreaConfig.data.text = scope.model.text;
                textDirectiveAreaConfig.data.staticText = scope.model.staticText == "true" ? true : false;
                textDirectiveAreaConfig.data.modes = Enumerable.From(scope.modes).Where(function (x) {
                    return x.checked;
                }).Select(function (x) {
                    return {
                        text: x.text,
                        name: x.name
                    };
                }).ToArray();
                textDirectiveAreaConfig.render = true;

                scope.$broadcast("saveStyle-" + scope.area.uuid);
                rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                scope.hide();
            };
        }];
});