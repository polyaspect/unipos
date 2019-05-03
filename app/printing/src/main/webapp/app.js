/**
 * Created by ggradnig on 2015-04-28
 */
//# sourceURL=app.js

require.config({
    paths: {
        'printer.angularRoute': 'assets/js/angularjs/1.4.3/angular-route',
        'printer.angularResource': 'assets/js/angularjs/1.4.3/angular-resource',
        'printer.ngAnimate': 'assets/js/angularjs/1.4.3/angular-animate',
        'printer.ngAria': 'assets/js/angularjs/1.4.3/angular-aria',
        'printer.uiGrid': 'assets/js/ui-grid/ui-grid',
        'printer.flowjs': 'assets/js/flowjs/ng-flow.min',
        'printer.sweetAlert': 'assets/js/sweetAlert/sweetalert.min',
        'printer.sweetAlertStyle': 'assets/css/sweetAlert/sweetalert',
        'printer.linq': 'assets/js/linq/linq',

        //CUSTOM

        'printer.printerController': 'components/printer/printerController',
        'printer.printerEditorController': 'components/printer/editor/printerEditor',
        'printer.printerService': 'components/printer/printerService',
        'printer.printerRoutes': 'components/printer/printerRoutes',
        'printer.printerStyle': 'components/printer/printer',

        'printer.storeService': 'components/store/storeService',
        'printer.companyService': 'components/company/companyService',

        'printer.ui-grid': 'assets/css/ui-grid/ui-grid',
        'printer.font-awesome': 'assets/css/font-awesome/4.3.0/css/font-awesome.min',
        'printer.materialTable': 'assets/css/materialTable/materialTable',

        'printer.bootstrapCss': 'node_modules/bootstrap/dist/css/bootstrap',
        'printer.angularUiBootstrap': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap',
        'printer.angularUiBootstrapTpls': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap-tpls',
        'printer.angularUiSelect': 'node_modules/ui-select/dist/select',
        'printer.angularBootstrapCheckbox': 'node_modules/angular-bootstrap-checkbox/angular-bootstrap-checkbox',
        'printer.jkLayoutCss': 'assets/css/jkLayout/jkLayout'
    },
    shim: {
        'printer.angularRoute': ['angular'],
        'printer.angularResource': ['angular'],
        'printer.angularMocks': ['angular'],
        'printer.ngAnimate': ['angular'],
        'printer.ngAria': ['angular'],
        'printer.uiGrid': ['angular'],
        'printer.flowjs': ['angular'],
        'printer.angularUiSelect': ['angular'],
        'printer.angularUiBootstrap': ['angular'],
        'printer.angularUiBootstrapTpls': ['angular'],
        'printer.angularUiBootstrapTpls': ['printer.angularUiBootstrap'],
        'printer.angularBootstrapCheckbox': ['angular']
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
            //'core.ngMaterialMenuSidenav': ['printer.ngMaterial']
        }
    });
}

define([
    'angular',
    'printer.angularRoute',
    'printer.printerController',
    'printer.printerEditorController',
    'printer.printerService',
    'printer.printerRoutes',
    'printer.storeService',
    'printer.companyService',
    'core.navController',
    'core.navDirective',
    'core.ngMaterialMenuSidenav',
    'css!core.ngMaterialMenuSidenavCss',
    "printer.ngAnimate", "printer.ngAria",
    'css!printer.bootstrapCss', 'printer.angularUiBootstrap', 'printer.angularUiBootstrapTpls',
    "css!printer.ui-grid",
    "printer.uiGrid",
    "printer.flowjs",
    "printer.angularUiSelect",
    "css!printer.angularUiSelect",
    'printer.angularBootstrapCheckbox',
    "css!printer.font-awesome"
], function (angular,
             ngRoute,
             printerController,
             printerEditorController,
             printerService,
             printerRoutes,
             storeService,
             companyService,
             navController,
             navDirective) {
    var container = {
        configModule: function () {
            return angular.module('unipos.printer', [
                'ngRoute',
                'ngResource',
                'ngAnimate',
                'ngMenuSidenav',
                'ngAria',
                'ui.grid',
                'ui.grid.edit',
                'ui.grid.rowEdit',
                'ui.grid.cellNav',
                'ui.grid.selection',
                'flow',
                'ui.select',
                'ui.bootstrap',
                "ui.checkbox"
            ])
        }
    };

    var init = function (initOptions) {

        var module = container.configModule();
        module.run(function ($rootScope) {
            $rootScope.data = {};
            $rootScope.data.baseUrl = initOptions.baseUrl;
            $rootScope.data.baseDir = initOptions.baseDir;
        });

        module.config(['$routeProvider', function ($routeProvider) {
            $routeProvider.otherwise({redirectTo: initOptions.baseUrl + '/printers'});
        }]);

        module.config(['flowFactoryProvider', function (flowFactoryProvider) {
            flowFactoryProvider.defaults = {
                target: '/printer/printing/uploadImage',
                permanentErrors: [404, 500, 501],
                maxChunkRetries: 1,
                chunkRetryInterval: 5000,
                simultaneousUploads: 4,
                singleFile: true
            };
            flowFactoryProvider.on('catchAll', function (event) {
                console.log('catchAll', arguments);
            });
            // Can be used with different implementations of Flow.js
            // flowFactoryProvider.factory = fustyFlowFactory;
        }]);

        module.service("printerService", printerService);
        module.service("storeService", storeService);
        module.service("companyService", companyService);
        module.controller('PrinterController', printerController);
        module.controller('PrinterEditorController', printerEditorController);
        module.config(printerRoutes(initOptions.baseUrl, initOptions.baseDir));

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