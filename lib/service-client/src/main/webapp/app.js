/**
 * Created by ggradnig on 2015-04-28
 */

define([
    'angular',
    'angularRoute',
    'components/entity/entityController',
    'components/entity/entityRoutes'
], function(
    angular,
    ngRoute,
    entityController,
    entityRoutes) {
    var container = {
        configModule: function () {
            return angular.module('unipos.common', [
                'ngRoute',
                'ngResource'
            ])
        },
        configRoutes : function(ngModule, urlPrefix)
        {
            ngModule.config(['$routeProvider', function($routeProvider) {
                $routeProvider.otherwise({redirectTo: urlPrefix + 'entities'});
            }]);
        }
    };

    var init = function(initOptions){

        var module = container.configModule();
        container.configRoutes(module, initOptions.moduleUrl);

        module.controller('EntityController', entityController);
        module.config(entityRoutes(initOptions.moduleUrl));

        return container;
    };

    return init;
});