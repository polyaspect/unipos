//# sourceURL=pos.cashbookEntryService.js
define([
    'angular'
], function (angular) {
    return ["pos.abstractStrategyFactory", "pos.areaService", "pos.ui.elements.text.textDirectiveService", "$http", 'uuid2', 'pos.urlSettings', 'pos.queueService', 'pos.dataService', '$rootScope',
        function (abstractStrategyFactory, areaService, textDirectiveService, http, uuid, urlSettings, queueService, dataService, rootScope) {
            this.serviceName = "pos.cashbookService";
            this.keys = {};
            this.cashbookEntries = [];
            this.lastDailySettlement = {};
            this.options = undefined;
            var self = this;
            dataService.setByServiceKey(self.serviceName, {});
            this.setDirectiveData = function (directiveId, data) {
                dataService.setByDirectiveKey(self.serviceName, directiveId, data);
            };

            this.getDirectiveData = function (directiveId) {
                return dataService.getByDirectiveKey(self.serviceName, directiveId);
            };
            this.addCashBookEntry = function (areaData) {
                self.options = areaData.serviceData.options;
                self.toNextStep(undefined);
            };
            this.toNextStep = function (currentState) {

                var currentItem = Enumerable.From(self.options.steps).FirstOrDefault(undefined, function (x) {
                    return x.state == currentState;
                });
                var index = self.options.steps.indexOf(currentItem) + 1;
                if (!currentState) {
                    index = 0;
                }

                if(index == 0){
                }


                if (index != 0 && index == self.options.steps.length - 1) {
                    self.toLastStep(self.options.steps[index].screen);
                } else {
                    abstractStrategyFactory.changeState(self.options.steps[index].state);
                    if(self.options.steps[index].state == "CASHBOOK_AMOUNT_INPUT")
                        textDirectiveService.setText(textDirectiveService.textModes.price, "0,00");
                    if(self.options.steps[index].state == "CASHBOOK_DESCRIPTION_INPUT")
                        textDirectiveService.setText(textDirectiveService.textModes.keyboard, "");

                    areaService.toScreen(self.options.steps[index].screen);
                }
            };
            this.toLastStep = function (screen) {
                var amount = parseFloat(textDirectiveService.getText("price").replace(',', '.'));
                var description = textDirectiveService.getText("keyboard");
                var cashbookEntry = {
                    "type": self.options.in ? "IN" : "OUT",
                    "amount": amount,
                    "description": description,
                    "reference": self.options.private ? "PRIVATE" : "COMMERCIAL",
                    "guid": uuid.newuuid()
                };
                queueService.addToQueue({
                    url: urlSettings.baseUrl + urlSettings.addCashBookEntry,
                    method: "post",
                    data: cashbookEntry,
                    callback: function (response) {
                        self.cashbookEntries.push(cashbookEntry);
                        rootScope.$emit("pos.cashbookEntryService-");
                    }
                });
                abstractStrategyFactory.changeState(abstractStrategyFactory.states.orderOpened);
                textDirectiveService.setText("price", "");
                textDirectiveService.setText("keyboard", "");
                areaService.toScreen(screen);
            };
            this.getAllCashbookEntries = function () {
                http.get(urlSettings.baseUrl + urlSettings.getCashBookEntries).then(function (response) {
                    self.cashbookEntries = response.data;
                    rootScope.$emit("pos.cashbookEntryService-");
                });
                http.get(urlSettings.baseUrl + urlSettings.lastClosedDailySettlement).then(function (response) {
                    self.lastDailySettlement = response.data;
                })
            };
        }];
});