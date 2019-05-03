//# sourceURL=pos.quantityService.js
define([
    'angular',
], function (angular) {
    function replaceAt(text, index, character) {
        return text.substr(0, index) + character + text.substr(index + character.length);
    }

    return ['pos.ui.elements.text.textDirectiveService', 'pos.abstractStrategyFactory', '$injector', '$rootScope',
        function (textService, abstractStrategyFactory, injector, rootScope) {
            this.modes = ["noformat", "unformatted", "priceFormating"];
            this.validators = {};
            this.deleteLastCharFunctions = {};
            var self = this;

            this.doAction = function (cell) {
                if (cell.name == "AC") {
                    abstractStrategyFactory.createNumberInputStrategy().deleteAll(injector, cell.serviceData.modes);
                } else if (cell.name == "C" || cell.name == "Bksp") {
                    angular.forEach(cell.serviceData.modes, function (mode, index2) {
                        var text = textService.getMode(mode.name).text || "";
                        abstractStrategyFactory.createNumberInputStrategy().deleteChar(injector, mode.name, text);
                    });
                } else if (cell.name == "Shift") {
                    var service = injector.get("pos.numpadPanelService");
                    service.options.shift = !service.options.shift;
                    service.options.caps = false;
                    rootScope.$emit(service.serviceName + "-" + cell.serviceData.directiveId);
                } else if (cell.name == "Caps") {
                    var service = injector.get("pos.numpadPanelService");
                    service.options.caps = !service.options.caps;
                    service.options.shift = false;
                    rootScope.$emit(service.serviceName + "-" + cell.serviceData.directiveId);
                } else {
                    if (angular.isNumber(cell.name)) {
                        cell.name = cell.name.toString();
                    }
                    angular.forEach(cell.name.split(''), function (char, index) {
                        angular.forEach(cell.serviceData.modes, function (mode, index2) {
                            var text = textService.getMode(mode.name).text || "";

                            //ALTE IMPLEMENTIERUNG: In allen TextDirectives mit diesem Mode wird direkt der Text gesetzt
                            //textService.setText(mode.name, text);

                            //NEUE IMPLEMENTIERUNG: Je nach aktuellem Zustand wird entweder geschrieben, oder nicht
                            abstractStrategyFactory.createNumberInputStrategy().numberInput(textService, mode.name, text, char);
                        });
                    });
                    var service = injector.get("pos.numpadPanelService");
                    if (service.options.shift) {
                        service.options.shift = false;
                        rootScope.$emit(service.serviceName + "-" + cell.serviceData.directiveId);
                    }
                }

            };
            this.getText = function (mode) {
                var text = textService.getText(mode);
                if (text == undefined) {
                    textService.setText(mode, "");
                    return "";
                }
                return text;
            };
            this.setText = function (mode, text) {
                textService.setText(mode, text);
            };
            this.deleteAll = function (modes) {
                textService.setTextMultipleModes(modes, "");
            };
            this.getQuantity = function () {
                var quantity = parseFloat(self.getText(textService.textModes.quantity).replace(',', '.'));
                if (quantity == undefined || quantity == "" || isNaN(quantity)) {
                    quantity = 1;
                }
                return quantity;
            };
            this.getPrice = function () {
                var quantity = parseFloat(self.getText(textService.textModes.price).replace(',', '.'));
                if (quantity == undefined || quantity == "" || isNaN(quantity)) {
                    quantity = 0;
                }
                return quantity;
            };
            this.setSum = function (sum, hasPaymentItems, hasChangeItems, invoiceSum) {
                var text = "";
                if (hasChangeItems != undefined && hasChangeItems) {
                    if (invoiceSum != undefined) {
                        text = "Gezahlt: " + invoiceSum + "\n";
                    }
                    text += "Rückgeld: ";
                }
                text = text + sum;
                self.setText(textService.textModes.info, text);
            };
        }
    ];
});