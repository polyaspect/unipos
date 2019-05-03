//# sourceURL=updateService.js

define([], function () {
    return ["$resource", function ($resource) {
        var $this = this;

        var updateResource = $resource('/update', {}, {
            findUpdatableModules: {
                url:'/update/updatableModules',
                method: 'GET',
                isArray: true
            },
            getCurrentModuleVersionsInstalled: {
                url:'/update/getCurrentModuleVersionsInstalled',
                method: 'GET',
                isArray: true
            },
            processUpdate: {
                url: '/update',
                method: 'POST',
                isArray: true
            }
        });

        $this.findUpdatableModules = function() {
            return updateResource.findUpdatableModules();
        };

        $this.getCurrentModuleVersionsInstalled = function() {
            return updateResource.getCurrentModuleVersionsInstalled();
        };

        $this.processUpdate = function () {
            return updateResource.processUpdate();
        };
    }]

});