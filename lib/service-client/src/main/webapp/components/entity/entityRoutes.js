define([
    'angular',
    'angularRoute'
], function(){
        return function(urlPrefix) {
            return ['$routeProvider', function ($routeProvider) {
                $routeProvider.when(urlPrefix + 'entities', {
                    templateUrl: 'components/entity/entityView.html',
                    controller: 'EntityController'
                })
            }];
        }
    }
);

