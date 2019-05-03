//# sourceURL=activationService.js

define([], function () {
    return ["$resource", function ($resource) {
        var activationResource = $resource('/licenseManagement', {}, {
            activate: {
                method: 'POST',
                isArray: false,
                url: '/licenseManagement/activate'
            },
            install: {
                method: 'POST',
                isArray: false,
                url: '/licenseManagement/install'
            }
        });
        this.activate = function (productKey) {
            return activationResource.activate(productKey);
        };
        this.install = function (productKey) {
            return activationResource.install(productKey);
        };
    }];
});