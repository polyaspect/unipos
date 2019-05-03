define([
    'angular',
    'angularResource'
], function() {
        return ['$scope', '$resource', function ($scope, $resource) {
            var entities = $resource('entities/:entityId');

            $scope.entities = entities.query();
        }];
    }
);