//# sourceURL=pos.designerOptionsConfig.js
require.config({
    paths: {
        'pos.designer.designerOptions.newElements': 'components/designer/designerOptions/newElements/newElements',
        'pos.designer.designerOptions.collectionSettings': 'components/designer/designerOptions/collectionSettings/collectionSettings',
        'pos.designer.designerOptions.screenSettings': 'components/designer/designerOptions/screenSettings/screenSettings',
        'pos.designer.designerOptions.openCollection': 'components/designer/designerOptions/openCollection/openCollection',
        'pos.designer.designerOptions.styleSettings': 'components/designer/designerOptions/styleSettings/styleSettings',
    }
});
define([
    'angular',
    'pos.designer.designerOptions.newElements',
    'pos.designer.designerOptions.collectionSettings',
    'pos.designer.designerOptions.screenSettings',
    'pos.designer.designerOptions.openCollection',
    'pos.designer.designerOptions.styleSettings'
], function () {
    return function (module) {
        module.controller('pos.designer.designerOptions.newElements', require('pos.designer.designerOptions.newElements'));
        module.controller('pos.designer.designerOptions.screenSettings', require('pos.designer.designerOptions.screenSettings'));
        module.controller('pos.designer.designerOptions.collectionSettings', require('pos.designer.designerOptions.collectionSettings'));
        module.controller('pos.designer.designerOptions.openCollection', require('pos.designer.designerOptions.openCollection'));

        module.directive('pos.designer.designerOptions.styleSettings', require('pos.designer.designerOptions.styleSettings'));
    };
});