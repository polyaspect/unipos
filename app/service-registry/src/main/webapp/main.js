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
        'angularRoute': 'assets/js/angularjs/1.4.3/angular-route',
        'angularResource': 'assets/js/angularjs/1.4.3/angular-resource',
        'angularMocks': 'assets/js/angularjs/1.4.3/angular-mocks'
    },
    map: {
        '*': {
            'css': 'assets/js/require-css/0.1.8/css' // or whatever the path to require-css is
        }
    },
    shim: {
        'angular': {'exports': 'angular'},
        'angularRoute': ['angular'],
        'angularResource': ['angular'],
        'angularMocks': ['angular']
    },

    priority: [
        "angular"
    ],
    deps: deps
});

var modules = [];
var angularModules = [];

$.get("modules/started", function (data) {
    for (i = 0; i < data.length; i++) {
        modules.push(data[i].name);
        angularModules.push("unipos." + data[i].name);
    }
});

require([
        'angular',
        'app'
    ], function (angular, app) {
        var initOptions = {};
        initOptions.moduleUrl = "/";
        app(initOptions);

        //nextModule(modules, 0);
        start();
    }
)
;

function nextModule(modules, i) {
    var moduleName = modules[i];
    require.config({'baseUrl': modules[i] + "/"});
    requirejs.s.contexts._.config.paths[moduleName] = "app";

    require([modules[i]], function (m) {
        var initOptions = {};
        initOptions.baseUrl = "/" + modules[i];
        initOptions.baseDir = modules[i] + "/";

        if (m !== undefined) {
            m(initOptions);

            i++;
            if (i < modules.length) {
                nextModule(modules, i);
            }

            else {
                start();
            }
        }
        else {
            start();
        }
    }, function(error){
        var index = angularModules.indexOf('unipos.' + modules[i]);
        if (index > -1) {
            angularModules.splice(index, 1);
        }
        i++;
        nextModule(modules, i);
    });
}

function start() {
    angularModules.push("unipos.core");
    var module = angular.module('unipos', ["unipos.core"]);

    // $('<nav />').appendTo('body');

    /*module.config(['$routeProvider',
        function($routeProvider) {
            $routeProvider.
                otherwise({
                    redirectTo: '/pos/pos'
                });
        }]);*/

    var $html = angular.element(document.getElementsByTagName('html')[0]);
    angular.element().ready(function () {
        // bootstrap the app manually
        angular.bootstrap(document, ['unipos']);

        /*setTimeout(function(){
            $("#loadingCircle").fadeOut(function(){
                $("#ready").fadeIn().css("display","block");;
            });
        }, 500);*/
    });

    if (baseUrl != '') {
        window.__karma__.start();
    }
}