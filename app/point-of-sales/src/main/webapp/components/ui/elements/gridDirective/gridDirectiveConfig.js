//# sourceURL=pos.strategyConfig.js
require.config({
    paths: {
        'pos.ui.elements.grid.reportPreviewGridViewController': 'components/ui/elements/gridDirective/viewController/reportPreviewGridViewController',
    }
});
define([
    'angular',
    'pos.ui.elements.grid.reportPreviewGridViewController'

], function () {
    return function (module) {
        module.service("pos.ui.elements.grid.reportPreviewGridViewController", require("pos.ui.elements.grid.reportPreviewGridViewController"));
    }
});