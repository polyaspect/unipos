/**
 * Created by ggradnig on 2015-04-28
 */

require.config({
    paths: {
        'auth.angularRoute': 'assets/js/angularjs/1.4.3/angular-route',
        'auth.angularResource': 'assets/js/angularjs/1.4.3/angular-resource',
        'auth.ngAnimate': 'assets/js/angularjs/1.4.3/angular-animate',
        'auth.ngAria': 'assets/js/angularjs/1.4.3/angular-aria',
        'auth.ngCookies': 'assets/js/angularjs/1.4.3/angular-cookies',

        'auth.sweetAlert': 'assets/js/sweetAlert/sweetalert.min',
        'auth.sweetAlertStyle': 'assets/css/sweetAlert/sweetalert',

        'auth.userIndexController': 'components/user/userIndex',
        'auth.userEditorController': 'components/user/editor/userEditor',
        'auth.userService': 'components/user/userService',
        'auth.userRoutes': 'components/routes',

        'auth.mitarbeiterPinController': 'components/user/auth/mitarbeiterPin/mitarbeiterPin',
        'auth.mitarbeiterPinService': 'components/user/auth/mitarbeiterPin/mitarbeiterPinService',

        'auth.usernamePasswordController': 'components/user/auth/usernamePassword/usernamePassword',
        'auth.usernamePasswordService': 'components/user/auth/usernamePassword/usernamePasswordService',

        'auth.logoutController': 'components/user/auth/logout/logout',

        'auth.companyService': 'components/company/companyService',

        'auth.roleService': 'components/role/roleService',

        'auth.rightService': 'components/right/rightService',

        'auth.font-awesome': 'assets/css/font-awesome/4.3.0/css/font-awesome.min',

        'auth.uiGrid': 'assets/js/ui-grid/ui-grid',
        'auth.ui-grid': 'assets/css/ui-grid/ui-grid',

        'auth.linq': 'assets/js/linq/linq',

        'auth.bootstrapCss': 'node_modules/bootstrap/dist/css/bootstrap',
        'auth.angularUiBootstrap': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap',
        'auth.angularUiBootstrapTpls': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap-tpls',

        'auth.angularUiSelect': 'node_modules/ui-select/dist/select',

        'auth.angularBootstrapCheckbox': 'node_modules/angular-bootstrap-checkbox/angular-bootstrap-checkbox',


        'auth.jkLayoutCss': 'assets/css/jkLayout/jkLayout',
    },
    shim: {
        'auth.angularRoute': ['angular'],
        'auth.angularResource': ['angular'],
        'auth.angularMocks': ['angular'],
        'auth.ngAnimate': ['angular'],
        'auth.ngAria': ['angular'],
        'auth.ngCookies': ['angular'],
        'auth.uiGrid': ['angular'],
        'auth.angularUiBootstrap': ['angular'],
        'auth.angularUiBootstrapTpls': ['angular', 'auth.angularUiBootstrap'],
        'auth.angularUiSelect': ['angular'],
        'auth.angularBootstrapCheckbox': ['angular']
    }
});

if (deps == undefined) {
    require.config({
        paths: {
            'core.navController': '../components/nav/navController',
            'core.navDirective': '../components/nav/navDirective',
            'core.navService': '../components/nav/navService',
            'core.navDirectiveCss': '../components/nav/navDirective',
            'core.ngMaterialMenuSidenav': '../assets/js/angular-material-sidenav-menu/material-menu-sidenav.min',
            'core.ngMaterialMenuSidenavCss': '../assets/css/angular-material-sidenav-menu/material-menu-sidenav.min'
        },
        shim: {
            'core.ngMaterialMenuSidenav': ['angular']
        }
    });
}
else {
    require.config({
        paths: {
            'core.navController': '../../test/webapp/mock/nav/navController',
            'core.navDirective': '../../test/webapp/mock/nav/navDirective',
            'core.navService': '../../test/webapp/mock/nav/navService',
            'core.navDirectiveCss': '../../test/webapp/mock/nav/navDirective',
            'core.ngMaterialMenuSidenav': '../../test/webapp/mock/assets/angular-material-sidenav-menu/material-menu-sidenav.min',
            'core.ngMaterialMenuSidenavCss': '../../test/webapp/mock/assets/angular-material-sidenav-menu/material-menu-sidenav.min'
        },
        shim: {
            'core.ngMaterialMenuSidenav': ['angular']
        }
    });
}


define([
    'angular',
    'auth.angularResource',
    'auth.angularRoute',
    'auth.userIndexController',
    'auth.userEditorController',
    "auth.userService",
    "auth.userRoutes",
    'auth.companyService',
    'auth.roleService',
    'auth.rightService',
    'auth.mitarbeiterPinController',
    'auth.mitarbeiterPinService',
    'auth.usernamePasswordController',
    'auth.usernamePasswordService',
    'auth.logoutController',
    'core.navController',
    'core.navDirective',
    'css!auth.bootstrapCss',
    'auth.angularUiBootstrap',
    'auth.angularUiBootstrapTpls',
    'auth.angularUiSelect',
    'css!auth.angularUiSelect',
    'auth.angularBootstrapCheckbox',
    'css!auth.jkLayoutCss',
    'core.ngMaterialMenuSidenav',
    "auth.ngAnimate", "auth.ngAria",
    'css!core.ngMaterialMenuSidenavCss',
    "css!auth.font-awesome",
    "css!auth.ui-grid",
    "auth.uiGrid",
    'auth.ngCookies'
], function (angular,
             ngResource,
             ngRoute,
             userIndexController,
             userEditorController,
             userService,
             userRoutes,
             companyService,
             roleService,
             rightService,
             mitarbeiterPinController,
             mitarbeiterPinService,
             usernamePasswordController,
             usernamePasswordService,
             logoutController,
             navController,
             navDirective) {
    var container = {
        configModule: function () {
            return angular.module('unipos.auth', [
                'ngRoute',
                'ngResource',
                'ngAnimate',
                'ngMenuSidenav',
                'ngAria',
                'ngCookies',
                'ui.grid',
                'ui.grid.edit',
                'ui.grid.rowEdit',
                'ui.grid.cellNav',
                'ui.grid.selection',
                'ui.bootstrap',
                "ui.select",
                "ui.checkbox"
            ])
        }
    };

    var init = function (initOptions) {

        var module = container.configModule();
        module.run(function ($rootScope) {
            $rootScope.auth = {};
            $rootScope.auth.baseUrl = initOptions.baseUrl;
            $rootScope.auth.baseDir = initOptions.baseDir;
        });

        module.config(['$routeProvider', function ($routeProvider) {
            $routeProvider.otherwise({redirectTo: initOptions.baseUrl + '/loginByUsername'});
        }]);

        module.service("companyService", companyService);
        module.service("roleService", roleService);
        module.service("rightService", rightService);

        module.controller('mitarbeiterPinController', mitarbeiterPinController);
        module.service("mitarbeiterPinService", mitarbeiterPinService);

        module.controller('usernamePasswordController', usernamePasswordController);
        module.service("usernamePasswordService", usernamePasswordService);

        module.controller('logoutController', logoutController);

        module.service("userService", userService);
        module.controller('userIndexController', userIndexController);
        module.controller('userEditorController', userEditorController);

        module.config(userRoutes(initOptions.baseUrl, initOptions.baseDir));

        module.controller('navController', navController);
        module.directive('nav', navDirective);

        module.value('initOptions', {
            baseUrl: initOptions.baseUrl,
            baseDir: initOptions.baseDir
        });

        return container;
    };

    return init;
});