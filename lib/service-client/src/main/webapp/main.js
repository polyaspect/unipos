/**
 * Created by ggradnig on 2015-04-28
 */
if(typeof baseUrl == 'undefined'){
    baseUrl = '';
}

if(typeof deps == 'undefined'){
    deps = null;
}

require.config({
    baseUrl: baseUrl,

    // alias libraries paths.  Must set 'angular'
    paths: {
        'angular':              'assets/js/angularjs/1.4.3/angular',
        'angularRoute':         'assets/js/angularjs/1.4.3/angular-route',
        'angularResource':      'assets/js/angularjs/1.4.3/angular-resource',
        'angularMocks':         'assets/js/angularjs/1.4.3/angular-mocks',
        'angularBootstrap':     'assets/js/angular-ui-bootstrap/0.13.0/ui-bootstrap',
        'angularBootstrapTpls': 'assets/js/angular-ui-bootstrap/0.13.0/ui-bootstrap-tpls'
    },
    map: {
        '*': {
            'css':              'assets/js/require-css/0.1.8/css' // or whatever the path to require-css is
        }
    },
    shim: {
        'angular': {'exports': 'angular'},
        'angularRoute': ['angular'],
        'angularResource': ['angular'],
        'angularMocks': ['angular'],
        'angularBootstrap': ['angular'],
        'angularBootstrapTpls': ['angular'],
        'angularBootstrapTpls': ['angularBootstrap']
    },

    priority: [
        "angular"
    ],
    deps : deps
});

require([
        'angular',
        'angularBootstrap',
        'app',
        'angularBootstrapTpls'
    ], function (angular, angularBootstrap, app, angularBootstrapTpls) {
        var initOptions = {};
        initOptions.moduleUrl = "/";
        app(initOptions);

        angular.module('unipos', ['unipos.common']);

        var $html = angular.element(document.getElementsByTagName('html')[0]);
        angular.element().ready(function () {
            // bootstrap the app manually
            angular.bootstrap(document, ['unipos']);
        });

        if (baseUrl != '') {
            window.__karma__.start();
        }
    }
)
;
