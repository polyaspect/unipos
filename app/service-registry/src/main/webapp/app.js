//# sourceURL=app.js

require.config({
    paths: {
        'core.angularRoute': 'assets/js/angularjs/1.4.3/angular-route',
        'core.angularResource': 'assets/js/angularjs/1.4.3/angular-resource',
        'core.ngAnimate': 'assets/js/angularjs/1.4.3/angular-animate',
        'core.ngAria': 'assets/js/angular-aria/1.3.15/angular-aria',

        'core.routes': 'components/routes',

        'core.navController': 'components/nav/navController',
        'core.navDirective': 'components/nav/navDirective',
        'core.navDirectiveCss': 'components/nav/navDirective',

        'core.logController': 'components/log/logController',
        'core.logService': 'components/log/logService',

        'core.activationController': 'components/activation/activationController',
        'core.activationService': 'components/activation/activationService',

        'core.activationSuccessController': 'components/activation/success/activationSuccessController',

        'core.updateController': 'components/update/updateController',
        'core.updateService': 'components/update/updateService',

        'core.redirectController': 'components/nav/redirectController',

        //SweetAlert
        'core.sweetAlert': 'assets/js/sweetAlert/sweetalert.min',
        'core.sweetAlertStyle': 'assets/css/sweetAlert/sweetalert',

        'core.ngMaterialMenuSidenav': 'assets/js/angular-material-sidenav-menu/material-menu-sidenav.min',
        'core.ngMaterialMenuSidenavCss': 'assets/css/angular-material-sidenav-menu/material-menu-sidenav.min',

        'core.bootstrapCss': 'node_modules/bootstrap/dist/css/bootstrap',
        'core.angularUiBootstrap': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap',
        'core.angularUiBootstrapTpls': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap-tpls',

        'core.jkLayoutCss': 'assets/css/jkLayout/jkLayout',

        'core.linq': 'assets/js/linq/linq',

        'core.infiniteScroll': 'node_modules/ng-infinite-scroll/build/ng-infinite-scroll',

        'core.jsonFormatter': 'node_modules/jsonformatter/dist/json-formatter'
    },
    shim: {
        'core.ngAnimate': ['angular'],
        'core.ngAria': ['angular'],
        'core.angularRoute': ['angular'],
        'core.angularResource': ['angular'],
        'core.angularUiBootstrap': ['angular'],
        'core.angularUiBootstrapTpls': ['angular', 'core.angularUiBootstrap'],
        'core.ngMaterialMenuSidenav': ['angular'],
        'core.infiniteScroll': ['angular'],
        'core.jsonFormatter': ['angular']
    }
});

define([
    'angular',
    'core.angularRoute',
    'core.angularResource',
    'core.routes',
    'core.navController',
    'core.navDirective',
    'core.logController',
    'core.logService',
    'core.redirectController',
    'core.activationController',
    'core.activationService',
    'core.activationSuccessController',
    'core.updateController',
    'core.updateService',
    'core.ngAnimate',
    'core.ngAria',
    'css!core.bootstrapCss',
    'core.angularUiBootstrap',
    'core.angularUiBootstrapTpls',
    'css!core.jkLayoutCss',
    'core.ngMaterialMenuSidenav',
    'css!core.ngMaterialMenuSidenavCss',
    'core.infiniteScroll',
    'core.jsonFormatter',
    'css!core.jsonFormatter'
], function (angular,
             ngRoute,
             ngResource,
             routes,
             navController,
             navDirective,
             logController,
             logService,
             redirectController,
             activationController,
             activationService,
             activationSuccessController,
             updateController,
             updateService) {

    var init = function (initOptions) {
        var module = angular.module('unipos.core', [
            'ngRoute',
            'ngAnimate',
            'ngMenuSidenav',
            'ngResource',
            'ui.bootstrap',
            'infinite-scroll',
            'jsonFormatter'
        ]);

        angular.module('infinite-scroll').value('THROTTLE_MILLISECONDS', 250)

        module.controller('navController', navController);
        module.directive('nav', navDirective);

        module.controller('logController', logController);
        module.service('logService', logService);

        module.controller('ActivationController', activationController);
        module.service('activationService', activationService);

        module.controller('ActivationSuccessController', activationSuccessController);

        module.controller('UpdateController', updateController);
        module.service('updateService', updateService);

        module.controller('redirectController', redirectController);

        module.config(routes);

        return module;
    };

    return init;
});