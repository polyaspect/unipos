//# sourceURL=userService.js
/**
 * Created by dominik on 04.08.15.
 */

define(["angular"], function (angular) {
    return ["$resource", "$q", "$http", function ($resource, $q, $http) {

        var userResource = $resource('/auth/users', {}, {
            deleteUserByGuid: {
                method: 'DELETE',
                isArray:false,
                url: '/auth/users/guid'
            }
        });

        this.findAll = function () {
                var users = userResource.query();
            return users;
        };

        this.save = function(user) {
            return userResource.save(user);
        };

        this.checkIfResourceInstance = function(instance) {
            return (instance instanceof userResource);
        };

        this.update = function (user) {
            return user.$save();
        };

        this.save = function(user) {
            return userResource.save(user);
        };

        this.deleteByGuid = function(guid) {
            return userResource.deleteUserByGuid({guid: guid});
        };
    }];
});