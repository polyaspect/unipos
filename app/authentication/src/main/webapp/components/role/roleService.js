//# sourceURL=roleService.js
/**
 * Created by dominik on 04.08.15.
 */

define(["angular"], function (angular) {
    return ["$resource", function ($resource) {

        var roleResource = $resource('/auth/roles', {}, {
            deleteRoleByGuid: {
                method: 'DELETE',
                isArray:false,
                url: '/auth/roles/guid'
            }
        });

        this.findAll = function () {
            return roleResource.query();
        };

        this.save = function(role) {
            return roleResource.save(role);
        };

        this.checkIfResourceInstance = function(instance) {
            return (instance instanceof roleResource);
        };

        this.update = function (role) {
            return role.$save();
        };

        this.save = function(role) {
            return roleResource.save(role);
        };

        this.deleteByGuid = function(guid) {
            return roleResource.deleteRoleByGuid({guid: guid});
        };
    }];
});