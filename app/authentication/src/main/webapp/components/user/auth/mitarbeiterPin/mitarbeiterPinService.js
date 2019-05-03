//# sourceURL=MitarbeiterPinService.js
/**
 * Created by dominik on 04.08.15.
 */

define(["angular"], function (angular) {
    return ["$resource", "$q", "$http", function ($resource, $q, $http) {

        var mitarbeiterPinResource = $resource('/auth/mitarbeiterPin', {}, {
            deleteUserByGuid: {
                method: 'DELETE',
                isArray: false,
                url: '/auth/users/guid'
            },
            findByMitarbeiterId: {
                method: 'GET',
                isArray: false,
                url: '/auth/mitarbeiterPin/mitarbeiterId'
            },
            findByUserGuid: {
                method: 'GET',
                isArray: false,
                url: '/auth/mitarbeiterPin/findByUserGuid'
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

        this.findByMitarbeiterId = function (mitarbeiterId) {
            return mitarbeiterPinResource.findByMitarbeiterId({mitarbeiterId: mitarbeiterId})
        };
        this.findByUserGuid = function (userGuid) {
            return mitarbeiterPinResource.findByUserGuid({userGuid: userGuid})
        };

        this.pinLogin = function (mitarbeiternr, pin) {
            var deferred = $q.defer();
            $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
            $http({
                method: 'POST',
                url: '/auth/auth/pin_login',
                data: "mitarbeiternr=" + mitarbeiternr + "&pin=" + pin,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (data) {
                deferred.reject(data);
            });
            return deferred.promise;
        };
    }];
});