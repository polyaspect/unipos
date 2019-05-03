//# sourceURL=logService.js

define([], function () {
    return ["$resource", function ($resource) {
        var logResource = $resource('/logs', {}, {
            showLogs: {
                url: '/logs/showLogs',
                method: 'GET',
                isArray: true
            }
        });

        this.showLogs = function (pageRequest) {
            return logResource.showLogs(pageRequest);
        };
    }]
});