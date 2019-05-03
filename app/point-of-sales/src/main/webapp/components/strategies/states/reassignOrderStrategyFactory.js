//# sourceURL=pos.splitOrderStrategyFactory.js
define([
    'angular',
    'pos.reassignOrderChooseUserStrategy'
], function (angular) {
    return {
        createChooseUserStrategy: function () {
            return require("pos.reassignOrderChooseUserStrategy");
        }
    };
});