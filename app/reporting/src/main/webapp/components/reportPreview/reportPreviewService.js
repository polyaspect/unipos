//# sourceURL=reportPreviewService.js
define([], function () {
    return ["$resource", "$http", function ($resource, $http) {
        var storeResource = $resource('/data/stores/findByUser', {}, {
            findAllStoreGuids: {
                method: "GET",
                url: "/data/stores/findByUser",
                isArray: true
            }
        });

        this.findAll = function () {
            return storeResource.query();
        };
    }];
});