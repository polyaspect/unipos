/**
 * Created by ggradnig on 2015-04-28
 */

require.config({
    paths: {
        'signature.angularRoute': 'assets/js/angularjs/1.4.3/angular-route',
        'signature.angularResource': 'assets/js/angularjs/1.4.3/angular-resource',
        'signature.ngAnimate': 'assets/js/angularjs/1.4.3/angular-animate',
        'signature.ngAria': 'assets/js/angularjs/1.4.3/angular-aria',
        'signature.uiGrid': 'assets/js/ui-grid/ui-grid',
        'signature.linq': 'assets/js/linq/linq',
        'signature.signatureRoutes' : 'components/routes',

        //signature Dependencies
        'signature.sockJs': 'assets/js/ng-stomp/sockjs',
        'signature.ngStomp': 'assets/js/ng-stomp/ng-stomp',
        'signature.stomp': 'assets/js/ng-stomp/stomp',
        'signature.stompie': 'assets/js/ng-stomp/stompie',

        //SweetAlert
        'signature.sweetAlert': 'assets/js/sweetAlert/sweetalert2.min',
        'signature.sweetAlertStyle': 'assets/css/sweetAlert/sweetalert2.min',

        //CUSTOM
        'signature.signatureOptionsController': 'components/signatureOptions/signatureOptionsController',
        'signature.signatureOptionsService': 'components/signatureOptions/signatureOptionsService',
        'signature.signatureStyle':'components/signatureOptions/signature',
        'signature.signatureOptionsEditorController' : 'components/signatureOptions/editor/signatureOptionsEditor',

        //CUSTOM
        'signature.zeroInvoiceController': 'components/zeroInvoice/zeroInvoiceController',
        'signature.zeroInvoiceService': 'components/zeroInvoice/zeroInvoiceService',
        'signature.zeroInvoiceStyle':'components/zeroInvoice/zeroInvoice',
        'signature.zeroInvoiceEditorController' : 'components/zeroInvoice/editor/zeroInvoiceEditor',

        'signature.bootstrapCss': 'node_modules/bootstrap/dist/css/bootstrap',
        'signature.angularUiBootstrap': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap',
        'signature.angularUiBootstrapTpls': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap-tpls',
        'signature.font-awesome': 'assets/css/font-awesome/4.3.0/css/font-awesome.min',
        'signature.angularBootstrapCheckbox': 'node_modules/angular-bootstrap-checkbox/angular-bootstrap-checkbox',
        'signature.ui-grid': 'assets/css/ui-grid/ui-grid',
        'signature.jkLayoutCss': 'assets/css/jkLayout/jkLayout',

        'signature.angularUiSelect': 'node_modules/ui-select/dist/select',

    },
    shim: {
        'signature.angularRoute': ['angular'],
        'signature.angularResource': ['angular'],
        'signature.angularMocks': ['angular'],
        'signature.ngAnimate': ['angular'],
        'signature.uiGrid': ['angular'],
        'signature.ngAria': ['angular'],
        'signature.angularUiBootstrap': ['angular'],
        'signature.angularUiBootstrapTpls': ['angular'],
        'signature.angularUiBootstrapTpls': ['signature.angularUiBootstrap'],
        'signature.ngStomp': ['angular', 'signature.sockJs'],
        'signature.stompie': ['angular'],
        'signature.angularBootstrapCheckbox': ['angular'],
        'signature.angularUiSelect': ['angular'],
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
            'core.ngMaterialMenuSidenavCss': '../assets/css/angular-material-sidenav-menu/material-menu-sidenav.min',
            'core.ngMaterial': '../assets/css/angular-material/0.10.1/angular-material.min'
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
            'core.ngMaterialMenuSidenavCss': '../../test/webapp/mock/assets/angular-material-sidenav-menu/material-menu-sidenav.min',
        },
        shim: {
            'core.ngMaterialMenuSidenav': ['angular']
        }
    });
}

define([
    'angular',
    'signature.angularRoute',
    'signature.signatureRoutes',
    'signature.signatureOptionsController',
    'signature.signatureOptionsService',
    'signature.signatureOptionsEditorController',
    'signature.zeroInvoiceController',
    'signature.zeroInvoiceService',
    'signature.zeroInvoiceEditorController',
    'core.navController',
    'core.navDirective',
    'signature.angularBootstrapCheckbox',
    "signature.ngAnimate",
    "signature.ngAria",
    'css!signature.bootstrapCss',
    'signature.angularUiBootstrap',
    'signature.angularUiBootstrapTpls',
    "css!signature.ui-grid",
    "signature.uiGrid",
    "css!signature.font-awesome",
    "css!signature.jkLayoutCss",
    'signature.angularUiSelect',
    'css!signature.angularUiSelect'
], function (angular,
             ngRoute,
             signatureRoutes,
             signatureOptionsController,
             signatureOptionsService,
             signatureOptionsEditorController,
             zeroInvoiceController,
             zeroInvoiceService,
             zeroInvoiceEditorController,
             navController,
             navDirective) {
    var container = {
        configModule: function () {
            return angular.module('unipos.signature', [
                'ngRoute',
                'ngResource',
                'ngAnimate',
                'ngAria',
                'ui.grid',
                'ui.grid.edit',
                'ui.grid.rowEdit',
                'ui.grid.cellNav',
                'ui.grid.selection',
                'ui.bootstrap',
                "ui.checkbox",
                'ui.select',
            ]);
        }
    };

    var init = function (initOptions) {

        var module = container.configModule();
        module.run(function ($rootScope) {
            $rootScope.data = {};
            $rootScope.data.baseUrl = initOptions.baseUrl;
        });

        module.config(['$routeProvider', function ($routeProvider) {
            $routeProvider.otherwise({redirectTo: initOptions.baseUrl + '/signatureOptions'});
        }]);

        module.controller('SignatureOptionsController', signatureOptionsController);
        module.controller('SignatureOptionsEditorController', signatureOptionsEditorController);

        module.controller('ZeroInvoiceController', zeroInvoiceController);
        module.controller('ZeroInvoiceEditorController', zeroInvoiceEditorController);

        module.controller('navController', navController);

        module.service('signatureOptionsService', signatureOptionsService);
        module.service('zeroInvoiceService', zeroInvoiceService);

        module.directive('nav', navDirective);

        module.config(signatureRoutes(initOptions.baseUrl, initOptions.baseDir));

        module.value('initOptions', {
            baseUrl: initOptions.baseUrl,
            baseDir: initOptions.baseDir
        });

        return container;
    };

    return init;
});