/**
 * Created by ggradnig on 2015-04-28
 */
if (typeof baseUrl == 'undefined') {
    baseUrl = '';
}

if (typeof deps == 'undefined') {
    deps = null;
}

require.config({
    baseUrl: baseUrl,
    waitSeconds: 120,

    // alias libraries paths.  Must set 'angular'
    paths: {
        'angular': 'assets/js/angularjs/1.4.3/angular',
        'angularMocks': 'assets/js/angularjs/1.4.3/angular-mocks',
        'jquery': 'node_modules/jquery/dist/jquery'
    },
    map: {
        '*': {
            'css': 'node_modules/require-css/css' // or whatever the path to require-css is
        }
    },
    shim: {
        'angular': {'exports': 'angular'},
        'angularMocks': ['angular']
    },

    priority: [
        "angular"
    ],
    deps: deps
});

require([
        'angular',
        'app',
        'jquery'
    ], function (angular, app) {
        var initOptions = {};
        initOptions.baseUrl = "";
        initOptions.baseDir = "";
        app(initOptions);

        angular.module('unipos', ['unipos.report']);

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