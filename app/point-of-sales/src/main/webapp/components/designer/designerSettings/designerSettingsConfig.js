//# sourceURL=pos.designerSettingsConfig.js
require.config({
    paths: {
        'pos.designer.designerSettings.buttonDirectiveSettings.buttonDirectiveSettingsConfig': 'components/designer/designerSettings/buttonDirectiveSettings/buttonDirectiveSettingsConfig',
        'pos.designer.designerSettings.gridDirectiveSettings.gridDirectiveSettingsConfig': 'components/designer/designerSettings/gridDirectiveSettings/gridDirectiveSettingsConfig',
        'pos.designer.designerSettings.panelDirectiveSettings.panelDirectiveSettingsConfig': 'components/designer/designerSettings/panelDirectiveSettings/panelDirectiveSettingsConfig',

        'pos.designer.designerSettings.textDirectiveSettings': 'components/designer/designerSettings/textDirectiveSettings/textDirectiveSettings',

        'pos.designer.designerSettings.designerSettingsService': 'components/designer/designerSettings/designerSettingsService',
    }
});
define([
    'angular',
    'pos.designer.designerSettings.buttonDirectiveSettings.buttonDirectiveSettingsConfig',
    'pos.designer.designerSettings.gridDirectiveSettings.gridDirectiveSettingsConfig',
    'pos.designer.designerSettings.panelDirectiveSettings.panelDirectiveSettingsConfig',
    'pos.designer.designerSettings.textDirectiveSettings',
    'pos.designer.designerSettings.designerSettingsService',
], function () {
    return function (module) {
        module.controller('pos.designer.designerSettings.textDirectiveSettings', require('pos.designer.designerSettings.textDirectiveSettings'));
        module.service('pos.designer.designerSettings.designerSettingsService', require('pos.designer.designerSettings.designerSettingsService'));

        require('pos.designer.designerSettings.buttonDirectiveSettings.buttonDirectiveSettingsConfig')(module);
        require('pos.designer.designerSettings.gridDirectiveSettings.gridDirectiveSettingsConfig')(module);
        require('pos.designer.designerSettings.panelDirectiveSettings.panelDirectiveSettingsConfig')(module);
    }
});