//# sourceURL=pos.numpadInputStrategy.js
define([
    'angular'
], function (angular) {
    return {
        numberInput: function (service, mode, text, char) {
            if (mode == service.textModes.numpad) {
                var abstractStrategyFactory = angular.element(document.body).injector().get('pos.abstractStrategyFactory');
                service.setText(mode, abstractStrategyFactory.getFilter().validate(text, char));
            }
        },
        formatText: function(mode, text, injector) {
            return text;
        },
        deleteChar: function (injector, mode, text) {
            var textService = injector.get("pos.ui.elements.text.textDirectiveService");

            var abstractStrategyFactory = injector.get('pos.abstractStrategyFactory');
            textService.setText(mode, abstractStrategyFactory.getFilter().removeChar(text));
        },
        deleteAll: function(injector, modes){
            var textService = injector.get("pos.ui.elements.text.textDirectiveService");
            var abstractStrategyFactory = injector.get("pos.abstractStrategyFactory");

            textService.setTextMultipleModes(modes, "");
        }
    };
});