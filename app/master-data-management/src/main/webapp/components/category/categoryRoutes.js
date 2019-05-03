define([
    'angular',
    'data.angularRoute'
], function(){
        return function(urlPrefix) {
            return ['$routeProvider', function ($routeProvider) {
                $routeProvider.when(urlPrefix + 'categories', {
                    templateUrl: 'components/category/categoryView.html',
                    controller: 'CategoryController'
                })
            }];
        }
    }
);

