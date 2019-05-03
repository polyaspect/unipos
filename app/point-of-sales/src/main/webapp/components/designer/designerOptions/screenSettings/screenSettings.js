//# sourceURL=pos.designer.designerOptions.screenSettings.js
define([
    'angular',
    'css!pos.designer.designerOptions.collectionSettings'
], function (angular) {
    return ['$scope', '$uibModalInstance', 'modal',
        function (scope, uibModalInstance, model) {
            scope.functions = [
                {
                    icon: "fa-lock",
                    value: "LOGIN",
                    text: "Anmelden"
                }, {
                    icon: "fa-home",
                    value: "HOME",
                    text: "Hauptbildschirm"
                }, {
                    icon: "fa-building",
                    value: "SELECTSTORE",
                    text: "Filialauswahl"
                }, {
                    icon: "fa-file",
                    value: "SELECTPRINTER",
                    text: "Druckerauswahl"
                }];
            scope.selectedFunction = Enumerable.From(scope.functions).FirstOrDefault(undefined, function (x) {
                return x.value == model.screen.settings.function;
            });
            scope.selectFunction = function (func) {
                scope.selectedFunction = func;
            };
            scope.cancel = function () {
                uibModalInstance.dismiss();
            };
            scope.save = function () {
                uibModalInstance.close(scope.selectedFunction);
            };
        }];
});