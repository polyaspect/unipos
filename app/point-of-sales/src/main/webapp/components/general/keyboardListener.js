//# sourceURL=pos.keyboardListener.js
define([
    'angular'
], function (angular) {
    return ["pos.ui.elements.text.textDirectiveService", 'pos.quantityService', 'pos.enterService', 'pos.abstractStrategyFactory', '$injector',
        function (textDirectiveService, quantityService, enterService, abstractStrategyFactory, injector) {
            return {
                restrict: 'E',
                replace: true,
                scope: true,
                link: function (scope, el, attr) {
                    jQuery(document).on('keypress', function (e) {
                            if (e.keyCode >= 32 && e.keyCode <= 126) {
                                textDirectiveService.setAllModes(String.fromCharCode(e.keyCode));
                                if (abstractStrategyFactory.getState() != abstractStrategyFactory.states.doNothing) {
                                    e.preventDefault();
                                }
                            }
                        }
                    );
                    jQuery(document).on("keydown", function (e) {
                        if (event.keyCode == 8) {
                            angular.forEach(textDirectiveService.getModes(), function (mode, index2) {
                                var text = textDirectiveService.getMode(mode.name).text || "";
                                abstractStrategyFactory.createNumberInputStrategy().deleteChar(injector, mode.name, text);
                            });
                            if (abstractStrategyFactory.getState() != abstractStrategyFactory.states.doNothing) {
                                e.preventDefault();
                            }
                        }
                        if (event.keyCode == 13) {
                            enterService.doAction();
                            if (abstractStrategyFactory.getState() != abstractStrategyFactory.states.doNothing) {
                                e.preventDefault();
                            }
                        }
                    });
                }
            };
        }];
});