//# sourceURL=pos.cashbookDescriptionInputStrategy.js
define([
    'angular'
], function (angular) {
    return {
        numberInput: function (service, mode, text, char) {
            if (mode == service.textModes.keyboard) {
                var abstractStrategyFactory = angular.element(document.body).injector().get('pos.abstractStrategyFactory');
                service.setText(mode, abstractStrategyFactory.getFilter().validate(text, char));
            }
        },
        formatText: function(mode, text, injector) {
            if(mode == "keyboard"){
                return "Grund eingeben: " + text + "\nBestätigen mit ENTER";
            }
            return text;
        },
        deleteChar: function (injector, mode, text) {
            var textService = injector.get("pos.ui.elements.text.textDirectiveService");
            if (mode == textService.textModes.keyboard) {
                var abstractStrategyFactory = injector.get('pos.abstractStrategyFactory');
                textService.setText(mode, abstractStrategyFactory.getFilter().removeChar(text));
            }
        },
        deleteAll: function(injector, modes){
            var areaService = injector.get("pos.areaService");
            var textService = injector.get("pos.ui.elements.text.textDirectiveService");
            var abstractStrategyFactory = injector.get("pos.abstractStrategyFactory");

            textService.setTextMultipleModes(modes, "");
            abstractStrategyFactory.changeState("ORDER_OPENED");
            textService.setText("infoMessages", "Ein-/Ausgang abgebrochen");
            areaService.toDefaultScreen();
        }
    };
});