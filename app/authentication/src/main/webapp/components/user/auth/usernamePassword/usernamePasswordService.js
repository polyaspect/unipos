//# sourceURL=userService.js
/**
 * Created by dominik on 04.08.15.
 */

define(["angular"], function (angular) {
    return ["$resource", "$q", "$http", function ($resource, $q, $http) {

        var mitarbeiterPinResource = $resource('/auth/usernamePassword', {}, {
            deleteUserByGuid: {
                method: 'DELETE',
                isArray: false,
                url: '/auth/users/guid'
            },
            findByMitarbeiterId: {
                method: 'GET',
                isArray: false,
                url: '/auth/usernamePassword/username'
            },
            findByUserGuid: {
                method: 'GET',
                isArray: false,
                url: '/auth/usernamePassword/findByUserGuid'
            }
        });

        this.findAll = function () {
            var users = mitarbeiterPinResource.query();
            return users;
        };

        this.save = function (user) {
            return mitarbeiterPinResource.save(user);
        };

        this.checkIfResourceInstance = function (instance) {
            return (instance instanceof mitarbeiterPinResource);
        };

        this.update = function (user) {
            return user.$save();
        };

        this.save = function (user) {
            return mitarbeiterPinResource.save(user);
        };

        this.delete = function (user) {
            return mitarbeiterPinResource.delete(user);
        };

        this.deleteByGuid = function (guid) {
            return mitarbeiterPinResource.deleteUserByGuid({guid: guid});
        };

        this.findByUsername = function (username) {
            return mitarbeiterPinResource.findByMitarbeiterId({username: username})
        };
        this.findByUserGuid = function (userGuid) {
            return mitarbeiterPinResource.findByUserGuid({userGuid: userGuid})
        };

        this.passwordLogin = function (username, password) {
            var deferred = $q.defer();
            $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
            $http({
                method: 'POST',
                url: '/auth/auth/account_login',
                data: "username=" + username + "&password=" + password,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (data) {
                deferred.reject(data);
            });
            return deferred.promise;
        }
    }];
});