//# sourceURL=pos.strategyConfig.js
require.config({
    paths: {
        'pos.services.dailySettlementService' : 'components/service/services/dailySettlementService',
        'pos.services.storeService' : 'components/service/services/storeService',
    }
});
define([
    'angular',

    'pos.services.dailySettlementService',
    'pos.services.storeService'

], function () {
    return function (module) {
        module.service("pos.services.dailySettlementService", require("pos.services.dailySettlementService"));
        module.service("pos.services.storeService", require("pos.services.storeService"));
    }
});