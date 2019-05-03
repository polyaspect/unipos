//# sourceURL=pos.strategyConfig.js
require.config({
    paths: {
        'pos.ui.elements.button.showDailySettlementButtonViewController': 'components/ui/elements/buttonDirective/viewController/showDailySettlementButtonViewController',
        'pos.ui.elements.button.abortDailySettlementButtonViewController': 'components/ui/elements/buttonDirective/viewController/abortDailySettlementButtonViewController',
        'pos.ui.elements.button.closeDailySettlementButtonViewController': 'components/ui/elements/buttonDirective/viewController/closeDailySettlementButtonViewController',
    }
});
define([
    'angular',
    'pos.ui.elements.button.showDailySettlementButtonViewController',
    'pos.ui.elements.button.abortDailySettlementButtonViewController',
    'pos.ui.elements.button.closeDailySettlementButtonViewController'

], function () {
    return function (module) {
        module.service("pos.ui.elements.button.showDailySettlementButtonViewController", require("pos.ui.elements.button.showDailySettlementButtonViewController"));
        module.service("pos.ui.elements.button.abortDailySettlementButtonViewController", require("pos.ui.elements.button.abortDailySettlementButtonViewController"));
        module.service("pos.ui.elements.button.closeDailySettlementButtonViewController", require("pos.ui.elements.button.closeDailySettlementButtonViewController"));
    }
});