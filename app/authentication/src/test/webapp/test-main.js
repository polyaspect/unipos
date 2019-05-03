/**
 * Created by ggradnig on 2015-04-28
 */

var allTestFiles = [];
var TEST_REGEXP = /Spec\.js$/i;
for (var file in window.__karma__.files) {

    var pathToModule = function(path) {
        return path.replace(/^\/base\/app\//, '').replace(/\.js$/, '');
    };

    Object.keys(window.__karma__.files).forEach(function(file) {
        if (TEST_REGEXP.test(file)) {
            // Normalize paths to RequireJS module names.
            allTestFiles.push(file);
        }
    });
}

var baseDependencyUrl = '../../../target/dependency/META-INF/resources/webjars/';

requirejs.config({
    baseUrl: '/base/src/main/webapp/',


    paths: {
        'angular': baseDependencyUrl + 'angularjs/1.2.16/angular',
        'angularResource': baseDependencyUrl + 'angularjs/1.2.16/angular-resource',
        'angularMocks': baseDependencyUrl + 'angularjs/1.2.16/angular-mocks',
        'angularRoute': baseDependencyUrl + 'angularjs/1.2.16/angular-route'
    },
    shim: {
        'angular': { exports: 'angular' },
        'angularRoute': ['angular'],
        'angularResource': ['angular'],
        'angularMocks': ['angular']
    },
    deps: allTestFiles  // add tests array to load our tests
});

require([
    'angular',
    'app'
], function(angular, app) {
    var initOptions = {};
    initOptions.moduleUrl = "/";
    app(initOptions);

    angular.module('unipos', [
        'unipos.auth'
    ]);

    var $html = angular.element(document.getElementsByTagName('html')[0]);
    angular.element().ready(function() {
        // bootstrap the app manually
        angular.bootstrap(document, ['unipos']);
    });

    window.__karma__.start();
});