//# sourceURL=pos.designer.designerOptions.newElements.js
define([
    'angular',
    'css!pos.designer.designerOptions.newElements'
], function (angular) {
    return ['$scope', '$uibModalInstance', 'uuid2',  function (scope, uibModalInstance, uuid2) {
        scope.draggableDirective = [
            {
                "name": "panel-directive",
                "directive": "pos.ui.elements.panel.panel-directive"
            }, {
                "name": "grid-directive",
                "directive": "pos.ui.elements.grid.grid-directive"
            }, {
                "name": "button-directive",
                "directive": "pos.ui.elements.button.button-directive"
            }, {
                "name": "text-directive",
                "directive": "pos.ui.elements.text.text-directive"
            }
        ];
        scope.hide = function (item) {
            uibModalInstance.dismiss();
        };

        scope.selectItem = function (item) {
            let areaItem = {
                "uuid": uuid2.newuuid(),
                "name": item.directive,
                "settingsName": item.name,
                "clickDisabled": true
            };
            uibModalInstance.close(areaItem);
        };
    }];
});