//# sourceURL=pos.designerConfig.js
require.config({
    paths: {
        'pos.designer.designerSettings.designerSettingsConfig': 'components/designer/designerSettings/designerSettingsConfig',
        'pos.designer.designerOptions.designerOptionsConfig': 'components/designer/designerOptions/designerOptionsConfig',

        'pos.designer.designer': 'components/designer/designer',

        'pos.designer.screenService': 'components/designer/general/screenService',
        'pos.designer.screenApiService': 'components/designer/general/screenApiService',

        'pos.designer.screenCollectionService': 'components/designer/general/screenCollectionService',
        'pos.designer.screenCollectionApiService': 'components/designer/general/screenCollectionApiService',

        'pos.designer.styleService': 'components/designer/general/styleService',

        'pos.designer.designerLiveviewDirective': 'components/designer/designerLiveview/designerLiveviewDirective',

        'pos.designer.globalSettings': 'components/designer/globalSettings/globalSettings',
        'pos.designer.screenSetSettings': 'components/designer/screenSetSettings/screenSetSettings',
        'pos.designer.screenSettings': 'components/designer/screenSettings/screenSettings',

        'pos.designer.customerValidator': 'components/designer/general/customValidator'
    }
});
define([
    'angular',
    'pos.designer.designerSettings.designerSettingsConfig',
    'pos.designer.designerOptions.designerOptionsConfig',

    'pos.designer.designer',

    'pos.designer.screenService',
    'pos.designer.screenApiService',
    'pos.designer.screenCollectionService',
    'pos.designer.screenCollectionApiService',

    'pos.designer.styleService',
    'pos.designer.designerLiveviewDirective',

    'pos.designer.globalSettings',
    'pos.designer.screenSetSettings',
    'pos.designer.screenSettings',

    'pos.designer.customerValidator'
], function () {
    return function (module) {
        require('pos.designer.designerSettings.designerSettingsConfig')(module);
        require('pos.designer.designerOptions.designerOptionsConfig')(module);

        module.controller('pos.designer.designer', require('pos.designer.designer'));

        module.service('pos.designer.screenService', require('pos.designer.screenService'));
        module.service('pos.designer.screenApiService', require('pos.designer.screenApiService'));

        module.service('pos.designer.screenCollectionService', require('pos.designer.screenCollectionService'));
        module.service('pos.designer.screenCollectionApiService', require('pos.designer.screenCollectionApiService'));

        module.service('pos.designer.styleService', require('pos.designer.styleService'));

        module.directive('pos.designer.designerLiveviewDirective', require('pos.designer.designerLiveviewDirective'));

        module.controller('pos.designer.globalSettings', require('pos.designer.globalSettings'));
        module.controller('pos.designer.screenSetSettings', require('pos.designer.screenSetSettings'));
        module.controller('pos.designer.screenSettings', require('pos.designer.screenSettings'));


        module.directive('pos.designer.customerValidator', require('pos.designer.customerValidator'));
    }
});