//# sourceURL=pos.quantityInputStrategy.js
define([
    'angular'
], function (angular) {
    return {
        numberInput: function (service, mode, text, char) {
            if (mode == service.textModes.quantity) {
                var abstractStrategyFactory = angular.element(document.body).injector().get('pos.abstractStrategyFactory');
                service.setText(mode, abstractStrategyFactory.getFilter().validate(text, char));
            }
        },
        formatText: function(mode, text, injector) {
            if(mode == "quantity"){
                return "Menge:  " + text + "\nArtikel auswählen";
            }else{
                return text;
            }
        },
        deleteChar: function (injector, mode, text) {
            var textService = injector.get("pos.ui.elements.text.textDirectiveService");
            if (mode == textService.textModes.quantity) {
                var abstractStrategyFactory = injector.get('pos.abstractStrategyFactory');
                var text = abstractStrategyFactory.getFilter().removeChar(text);
                if(text == undefined || text == ""){
                    text = "1";
                }
                textService.setText(mode, text);
            }
        },
        deleteAll: function(injector, modes){
            var textService = injector.get("pos.ui.elements.text.textDirectiveService");
            var abstractStrategyFactory = injector.get("pos.abstractStrategyFactory");

            textService.setTextMultipleModes(modes, "");
            textService.setText("infoMessages", "Mengeneingabe abgebrochen");
            abstractStrategyFactory.changeState("ORDER_OPENED");
        }
    };
});