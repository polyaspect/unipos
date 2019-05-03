//# sourceURL=pos.designer.gridDirectiveSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.gridDirectiveSettings"
], function (angular) {
    return ['$scope', '$uibModalInstance', 'pos.categoryService', 'pos.designer.screenService', '$compile', '$timeout', 'area', 'options',
        function (scope, uibModalInstance, categoryService, screenService, $compile, timeout, area, options) {
            scope.area = area;
            scope.modelOptions = options;
            scope.options = {
                showTypes: true
            };
            scope.types = [{
                text: "Aktuelle Rechnung",
                name: "pos.orderDirectiveService",
                action: "getData",
                settingsDirective: 'pos.designer.designer-settings.grid-directive-settings.order-grid-settings'
            }, {
                text: "Abgeschlossene Rechnungen",
                name: "pos.showInvoicesGridService",
                action: "getData",
                settingsDirective: 'pos.designer.designer-settings.grid-directive-settings.invoices-grid-settings'
            }, {
                text: "Filiale auswählen",
                name: "pos.jkStoreChooserService",
                action: "getStores",
                settingsDirective: 'pos.designer.designer-settings.grid-directive-settings.stores-grid-settings'
            }, {
                text: "Report anzeigen",
                name: "pos.reportPreviewGridService",
                action: "getData",
                settingsDirective: 'pos.designer.designer-settings.grid-directive-settings.report-preview-grid-settings'
            }, {
                text: "Drucker auswählen",
                name: "pos.printerGridService",
                action: "getData",
                settingsDirective: 'pos.designer.designer-settings.grid-directive-settings.printer-grid-settings'
            }, {
                text: "Cashbookeinträge anzeigen",
                name: "pos.cashbookEntryGridService",
                action: "getData",
                settingsDirective: 'pos.designer.designer-settings.grid-directive-settings.cashbook-entry-grid-settings'
            }, {
                text: "Rechnungspositionen auswählen",
                name: "pos.chooseOrderItemGridService",
                action: "getData",
                settingsDirective: 'pos.designer.designer-settings.grid-directive-settings.choose-order-item-grid-settings'
            }, {
                text: "Bestellungen anzeigen",
                name: "pos.chooseOrderGridService",
                action: "getData",
                settingsDirective: 'pos.designer.designer-settings.grid-directive-settings.choose-order-grid-settings'
            }, {
                text: "Benutzer auswählen",
                name: "pos.usersGridService",
                action: "getData",
                settingsDirective: 'pos.designer.designer-settings.grid-directive-settings.users-grid-settings'
            }];

            scope.selectType = function (item) {
                scope.options.showTypes = false;
                let tabs = $compile('<' + item.settingsDirective + "></" + item.settingsDirective + '>')(scope);
                angular.element(".grid-directive-settings .directive-tabs").append(tabs);
            };

            if (scope.area && scope.area.data && scope.area.data.service) {
                var action = Enumerable.From(scope.types).FirstOrDefault(undefined, function (x) {
                    return x.name == scope.area.data.service.name && (!x.action || (x.action && x.action == scope.area.data.service.data));
                });

                timeout(function () {
                    scope.selectType(action);
                }, 10);
            }
            scope.hide = function () {
                uibModalInstance.close();
            };
        }];
});