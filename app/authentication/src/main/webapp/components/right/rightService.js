//# sourceURL=rightService

/**
 * Created by domin on 21.05.2016.
 */

define([
    "angular"
], function (angular) {
    return ["$resource", "$httpParamSerializer", function ($resource, $httpParamSerializer) {
        self = this;

        var rightResource = $resource("/auth/rights", {}, {
            rightsPerPartition: {
                method: 'GET',
                url: "/auth/rights/rightsPerPartition",
                isArray:true
            },
            assignRightsToUser: {
                method: 'POST',
                url: '/auth/rights/assignRightsToUser',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            },
            rightsOfUser: {
                method: 'GET',
                url: '/auth/rights/rightsOfUser/:userId',
                isArray:true
            }
        });

        self.findAll = function () {
            return rightResource.query();
        };

        self.getRightsPerPermission = function () {
            return rightResource.rightsPerPartition();
        };

        self.assignRightsToUser = function (userId, rightGuids) {
            return rightResource.assignRightsToUser($httpParamSerializer({userId:userId, rightGuids: rightGuids}));
        };

        self.getRightsOfUser = function (userId) {
            return rightResource.rightsOfUser({userId:userId});
        };
    }]
})
;

