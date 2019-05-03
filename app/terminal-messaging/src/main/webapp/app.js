/**
 * Created by ggradnig on 2015-04-28
 */

require.config({
    paths: {
        'socket.angularRoute': 'assets/js/angularjs/1.4.3/angular-route',
        'socket.angularResource': 'assets/js/angularjs/1.4.3/angular-resource',
        'socket.ngAnimate': 'assets/js/angularjs/1.4.3/angular-animate',
        'socket.ngAria': 'assets/js/angularjs/1.4.3/angular-aria',
        'socket.ngMaterial': 'assets/js/angular-material/angular-material',
        'socket.uiGrid': 'assets/js/ui-grid/ui-grid',
        'socket.linq': 'assets/js/linq/linq',

        //Socket Dependencies
        'socket.sockJs': 'assets/js/ng-stomp/sockjs',
        'socket.ngStomp': 'assets/js/ng-stomp/ng-stomp',
        'socket.stomp': 'assets/js/ng-stomp/stomp',
        'socket.stompie': 'assets/js/ng-stomp/stompie',

        //SweetAlert
        'socket.sweetAlert': 'assets/js/sweetAlert/sweetalert.min',
        'socket.sweetAlertStyle': 'assets/css/sweetAlert/sweetalert',

        //CUSTOM
        'socket.socketController': 'components/socket/socketController',
        'socket.socketRoutes': 'components/socket/socketRoutes',

        'socket.deviceTokenController': 'components/deviceToken/deviceTokenController',

        'socket.deviceController': 'components/device/deviceController',
        'socket.deviceEditorController': 'components/device/editor/deviceEditor',
        'socket.deviceService': 'components/device/deviceService',
        'socket.deviceStyle': 'components/device/device',

        'socket.printerService': 'components/printer/printerService',
        'socket.storeService': 'components/store/storeService',

        'socket.bootstrapCss': 'node_modules/bootstrap/dist/css/bootstrap',
        'socket.angularUiBootstrap': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap',
        'socket.angularUiBootstrapTpls': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap-tpls',
        'socket.font-awesome': 'assets/css/font-awesome/4.3.0/css/font-awesome.min',
        'socket.angularBootstrapCheckbox': 'node_modules/angular-bootstrap-checkbox/angular-bootstrap-checkbox',
        'socket.ui-grid': 'assets/css/ui-grid/ui-grid',
        'socket.jkLayoutCss': 'assets/css/jkLayout/jkLayout'

    },
    shim: {
        'socket.angularRoute': ['angular'],
        'socket.angularResource': ['angular'],
        'socket.angularMocks': ['angular'],
        'socket.ngAnimate': ['angular'],
        'socket.uiGrid': ['angular'],
        'socket.ngAria': ['angular'],
        'socket.angularUiBootstrap': ['angular'],
        'socket.angularUiBootstrapTpls': ['angular'],
        'socket.angularUiBootstrapTpls': ['socket.angularUiBootstrap'],
        'socket.ngStomp': ['angular', 'socket.sockJs'],
        'socket.stompie': ['angular'],
        'socket.angularBootstrapCheckbox': ['angular']
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
    'socket.angularRoute',
    'socket.socketController',
    'socket.deviceController',
    'socket.deviceEditorController',
    'socket.deviceService',
    'socket.printerService',
    'socket.storeService',
    'socket.socketRoutes',
    'socket.deviceTokenController',
    'core.navController',
    'core.navDirective',
    'socket.angularBootstrapCheckbox',
    "socket.ngAnimate", "socket.ngAria",
    'css!socket.bootstrapCss', 'socket.angularUiBootstrap', 'socket.angularUiBootstrapTpls',
    'socket.ngStomp',
    "css!socket.ui-grid",
    "socket.uiGrid",
    "css!socket.font-awesome",
    "css!socket.jkLayoutCss"
], function (angular,
             ngRoute,
             socketController,
             deviceController,
             deviceEditorController,
             deviceService,
             printerService,
             storeService,
             socketRoutes,
             deviceTokenController,
             navController,
             navDirective) {
    var container = {
        configModule: function () {
            return angular.module('unipos.socket', [
                'ngRoute',
                'ngResource',
                'ngAnimate',
                'ngAria',
                'ngStomp',
                'ui.grid',
                'ui.grid.edit',
                'ui.grid.rowEdit',
                'ui.grid.cellNav',
                'ui.grid.selection',
                'ui.bootstrap',
                "ui.checkbox"
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
            $routeProvider.otherwise({redirectTo: initOptions.baseUrl + '/sockets'});
        }]);

        module.controller('SocketController', socketController);

        module.controller('DeviceTokenController', deviceTokenController);

        module.controller('DeviceController', deviceController);
        module.controller('DeviceEditorController', deviceEditorController);
        module.service('deviceService', deviceService);

        module.service('printerService', printerService);
        module.service('socket.storeService', storeService);

        module.controller('navController', navController);
        module.directive('nav', navDirective);

        module.config(socketRoutes(initOptions.baseUrl, initOptions.baseDir));

        module.value('initOptions', {
            baseUrl: initOptions.baseUrl,
            baseDir: initOptions.baseDir
        });

        return container;
    };

    return init;
});