//# sourceURL=pos.designer.designerOptions.openCollection.js
define([
    'angular',
    'css!pos.designer.designerOptions.openCollection'
], function (angular) {
    return ['$scope', '$uibModalInstance', '$location', 'pos.designer.screenCollectionApiService',
        function (scope, uibModalInstance, location, screenCollectionApiService) {
            scope.screenCollections = screenCollectionApiService.listNames();
            scope.hide = function () {
                uibModalInstance.dismiss()
            };
            scope.open = function (screenCollectionName) {
                location.path("/designer/" + screenCollectionName);
                uibModalInstance.close();
            };
        }];
});