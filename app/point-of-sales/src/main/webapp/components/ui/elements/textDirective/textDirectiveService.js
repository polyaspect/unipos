//# sourceURL=pos.ui.elements.text.textDirectiveService.js
define([
    'angular',
    'pos.linq'
], function (angular) {
    return ['pos.dataService', 'pos.abstractStrategyFactory', '$injector', function (dataService, abstractStrategyFactory, injector) {
        this.serviceName = "textService";
        this.modesKey = "modes";
        var self = this;
        this.textModes = {
            numpad: "numpad",
            keyboard: "keyboard",
            quantity: "quantity",
            price: "price",
            error: "errorMessages",
            info: "infoMessages"
        };
        dataService.setByServiceKey(self.serviceName, {});

        this.setDirectiveData = function (directiveId, data) {
            dataService.setByDirectiveKey(self.serviceName, directiveId, data);
        };
        this.getDirectiveData = function (directiveId) {
            return dataService.getByDirectiveKey(self.serviceName, directiveId);
        };
        this.setText = function (mode, text) {
            var modes = self.getDirectiveData(self.modesKey);
            angular.forEach(modes, function (value, index) {
                if (value.name === mode) {
                    value.text = text;
                }
            });
            dataService.setByDirectiveKey(self.serviceName, self.modesKey, modes, undefined, mode);
        };
        this.getViewData = function (mode) {
            var modes = Enumerable.From(self.getDirectiveData(self.modesKey)).FirstOrDefault(undefined, function (x) {
                return x.name == mode;
            });
            return (modes && abstractStrategyFactory.createNumberInputStrategy().formatText(mode, modes.text, injector)) || "";
        };
        this.getText = function (mode) {
            var modes = Enumerable.From(self.getDirectiveData(self.modesKey)).FirstOrDefault(undefined, function (x) {
                return x.name == mode;
            });
            return (modes && modes.text) || "";
        };
        this.setTextMultipleModes = function (modes, text) {
            angular.forEach(modes, function (mode, index) {
                self.setText(mode.name, text);
            });
        };
        this.getMode = function (mode) {
            return Enumerable.From(self.getDirectiveData(self.modesKey)).FirstOrDefault(undefined, function (x) {
                return x.name == mode;
            });
        };
        this.setAllModes = function (text) {
            angular.forEach(self.getDirectiveData(self.modesKey), function (mode, index) {
                var service = injector.get("pos.quantityService");
                abstractStrategyFactory.createNumberInputStrategy().numberInput(self, mode.name, service.getText(mode.name), text);
            });
        };
        this.getModes = function () {
            return self.getDirectiveData(self.modesKey);
        };
        dataService.setByDirectiveKey(self.serviceName, self.modesKey, [{
            "name": "quantity",
            "inputType": "unformatted"
        }, {
            "name": "price",
            "inputType": "priceFormating"
        }, {
            "name": "numpad",
            "inputType": "unformatted"
        }, {
            "name": "keyboard",
            "inputType": "noformat"
        }, {
            "name": "errorMessages",
            "inputType": "unformatted"
        }, {
            "name": "infoMessages",
            "inputType": "unformatted"
        }, {
            "name": "loggedInUser",
            "inputType": "unformatted"
        }]);
    }];
});