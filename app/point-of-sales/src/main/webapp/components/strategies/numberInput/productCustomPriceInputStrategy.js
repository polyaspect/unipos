//# sourceURL=pos.priceInputStrategy.js
define([
    'angular'
], function (angular) {
    return {
        numberInput: function (service, mode, text, char) {
            if (mode == service.textModes.price) {
                var abstractStrategyFactory = angular.element(document.body).injector().get('pos.abstractStrategyFactory');
                service.setText(mode, abstractStrategyFactory.getFilter().validate(text, char));
            }
        },
        formatText: function(mode, text, injector) {
            if(mode == "price"){
                return "Preis eingeben: € " + text + "\nBestätigen mit ENTER";
            }
            return text;
        },
        deleteChar: function (injector, mode, text) {
            var textService = injector.get("pos.ui.elements.text.textDirectiveService");
            if (mode == textService.textModes.price) {
                var abstractStrategyFactory = injector.get('pos.abstractStrategyFactory');
                textService.setText(mode, abstractStrategyFactory.getFilter().removeChar(text));
            }
        },
        deleteAll: function(injector, modes){
            var textService = injector.get("pos.ui.elements.text.textDirectiveService");
            var abstractStrategyFactory = injector.get("pos.abstractStrategyFactory");

            textService.setTextMultipleModes(modes, "");
            textService.setText("infoMessages", "Preiseingabe abgebrochen");
            abstractStrategyFactory.changeState("ORDER_OPENED");
        }
    };
});