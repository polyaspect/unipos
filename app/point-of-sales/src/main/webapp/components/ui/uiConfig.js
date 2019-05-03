//# sourceURL=pos.strategyConfig.js
require.config({
    paths: {
        'pos.ui.dataSourceService': 'components/ui/dataSource/dataSourceService',
        'pos.ui.screenService': 'components/ui/screen/screenService',

        'pos.ui.elements.text.textDirectiveService': 'components/ui/elements/textDirective/textDirectiveService',
        'pos.ui.elements.text.textDirective': 'components/ui/elements/textDirective/textDirective',

        'pos.ui.elements.grid.gridDirectiveService': 'components/ui/elements/gridDirective/gridDirectiveService',
        'pos.ui.elements.grid.gridDirective': 'components/ui/elements/gridDirective/gridDirective',
        'pos.ui.elements.grid.gridDirectiveConfig': 'components/ui/elements/gridDirective/gridDirectiveConfig',

        'pos.ui.elements.button.buttonDirectiveService': 'components/ui/elements/buttonDirective/buttonDirectiveService',
        'pos.ui.elements.button.buttonDirective': 'components/ui/elements/buttonDirective/buttonDirective',
        'pos.ui.elements.button.buttonDirectiveConfig': 'components/ui/elements/buttonDirective/buttonDirectiveConfig',

        'pos.ui.elements.panel.panelDirectiveService': 'components/ui/elements/panelDirective/panelDirectiveService',
        'pos.ui.elements.panel.panelDirective': 'components/ui/elements/panelDirective/panelDirective'
    }
});
define([
    'angular',

    'pos.ui.dataSourceService',
    'pos.ui.screenService',

    'pos.ui.elements.text.textDirectiveService',
    'pos.ui.elements.text.textDirective',

    'pos.ui.elements.grid.gridDirectiveService',
    'pos.ui.elements.grid.gridDirective',
    'pos.ui.elements.grid.gridDirectiveConfig',

    'pos.ui.elements.button.buttonDirectiveService',
    'pos.ui.elements.button.buttonDirective',
    'pos.ui.elements.button.buttonDirectiveConfig',

    'pos.ui.elements.panel.panelDirectiveService',
    'pos.ui.elements.panel.panelDirective'
], function () {
    return function (module) {
        require('pos.ui.elements.grid.gridDirectiveConfig')(module);
        require('pos.ui.elements.button.buttonDirectiveConfig')(module);

        module.service("pos.ui.dataSourceService", require("pos.ui.dataSourceService"));
        module.service("pos.ui.screenService", require("pos.ui.screenService"));

        module.service("pos.ui.elements.text.textDirectiveService", require("pos.ui.elements.text.textDirectiveService"));
        module.directive("pos.ui.elements.text.textDirective", require("pos.ui.elements.text.textDirective"));

        module.service("pos.ui.elements.grid.gridDirectiveService", require("pos.ui.elements.grid.gridDirectiveService"));
        module.directive("pos.ui.elements.grid.gridDirective", require("pos.ui.elements.grid.gridDirective"));

        module.service("pos.ui.elements.button.buttonDirectiveService", require("pos.ui.elements.button.buttonDirectiveService"));
        module.directive("pos.ui.elements.button.buttonDirective", require("pos.ui.elements.button.buttonDirective"));

        module.service("pos.ui.elements.panel.panelDirectiveService", require("pos.ui.elements.panel.panelDirectiveService"));
        module.directive("pos.ui.elements.panel.panelDirective", require("pos.ui.elements.panel.panelDirective"));
    }
});