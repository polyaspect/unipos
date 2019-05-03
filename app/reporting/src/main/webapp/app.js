//# sourceURL=reportApp.js
require.config({
    paths: {
        'report.angularRoute': 'assets/js/angularjs/1.4.3/angular-route',
        'report.angularResource': 'assets/js/angularjs/1.4.3/angular-resource',
        'report.ngAnimate': 'assets/js/angularjs/1.4.3/angular-animate',
        'report.ngAria': 'assets/js/angularjs/1.4.3/angular-aria',
        'report.ngLocale': 'assets/js/angularjs/1.4.3/i18n/angular-locale_de-at',
        //CUSTOM

        'report.reportPreviewController': 'components/reportPreview/reportPreview',
        'report.reportPreviewService': 'components/reportPreview/reportPreviewService',
        'report.reportPreviewModal': 'components/reportPreview/modal/reportModal',

        'report.reviewController': 'components/review/reviewController',
        'report.reviewEditorController': 'components/review/editor/reviewEditor',
        'report.reviewService': 'components/review/reviewService',
        'report.reviewStyle': 'components/review/review',
        'report.reviewRoutes': 'components/review/reviewRoutes',

        'report.journalRepairController': 'components/journalRepair/journalRepairController',
        'report.journalRepairService': 'components/journalRepair/journalRepairService',

        'report.companyService': 'components/company/companyService',

        'report.font-awesome': 'assets/css/font-awesome/4.3.0/css/font-awesome.min',

        'report.uiGrid': 'assets/js/ui-grid/ui-grid',
        'report.ui-grid': 'assets/css/ui-grid/ui-grid',

        'report.linq': 'assets/js/linq/linq',

        'report.sweetAlert': 'assets/js/sweetAlert/sweetalert.min',
        'report.sweetAlertStyle': 'assets/css/sweetAlert/sweetalert',

        'report.bootstrapCss': 'node_modules/bootstrap/dist/css/bootstrap',
        'report.angularUiBootstrap': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap',
        'report.angularUiBootstrapTpls': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap-tpls',

        'report.bootstrap-waitingfor': 'node_modules/bootstrap-waitingfor/build/bootstrap-waitingfor',

        'report.angularUiSelect': 'node_modules/ui-select/dist/select',

        'report.angularBootstrapCheckbox': 'node_modules/angular-bootstrap-checkbox/angular-bootstrap-checkbox',

        'moment': 'node_modules/moment/moment',

        'report.angularBootstrapDatetimepicker': 'node_modules/angular-bootstrap-datetimepicker/src/js/datetimepicker',
        'report.angularBootstrapDatetimepickerTemplate': 'node_modules/angular-bootstrap-datetimepicker/src/js/datetimepicker.templates',
        'report.angularBootstrapDatetimepickerCss': 'node_modules/angular-bootstrap-datetimepicker/src/css/datetimepicker',

        'report.jkLayoutCss': 'assets/css/jkLayout/jkLayout'
    },
    shim: {
        'report.angularRoute': ['angular'],
        'report.angularResource': ['angular'],
        'report.angularMocks': ['angular'],
        'report.ngAnimate': ['angular'],
        'report.ngAria': ['angular'],
        'report.uiGrid': ['angular'],
        'report.angularUiSelect': ['angular'],
        'report.angularUiBootstrap': ['angular'],
        'report.angularUiBootstrapTpls': ['angular', 'report.angularUiBootstrap'],
        'report.angularBootstrapDatetimepicker': ['angular', 'moment'],
        'report.angularBootstrapDatetimepickerTemplate': ['report.angularBootstrapDatetimepicker'],
        'report.ngLocale':['angular']
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
    'report.angularRoute',
    'report.reportPreviewController',
    'report.reportPreviewService',
    'report.reportPreviewModal',
    'report.reviewController',
    'report.reviewEditorController',
    'report.reviewService',
    'report.journalRepairService',
    'report.journalRepairController',
    'report.reviewRoutes',
    'report.companyService',
    'core.navController',
    'core.navDirective',
    'report.angularUiSelect',
    'moment',
    'report.angularBootstrapDatetimepicker',
    'report.angularBootstrapDatetimepickerTemplate',
    'css!report.angularBootstrapDatetimepickerCss',
    'css!report.angularUiSelect',
    'core.ngMaterialMenuSidenav',
    'css!core.ngMaterialMenuSidenavCss',
    "report.ngAnimate", "report.ngAria",
    "css!report.font-awesome",
    "css!report.ui-grid",
    "report.uiGrid",
    'css!report.bootstrapCss',
    'report.angularUiBootstrap',
    'report.angularUiBootstrapTpls',
    'css!report.jkLayoutCss',
    'report.ngLocale'
], function (angular,
             ngRoute,
             reportPreviewController,
             reportPreviewService,
             reportPreviewModal,
             reviewController,
             reviewEditorController,
             reviewService,
             journalRepairService,
             journalRepairController,
             reviewRoutes,
             companyService,
             navController,
             navDirective) {
    var container = {
        configModule: function () {
            return angular.module('unipos.report', [
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
                'ui.select',
                'ui.bootstrap',
                'ui.bootstrap.datetimepicker'
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
            $routeProvider.otherwise({redirectTo: initOptions.baseUrl + '/journalReview'});
        }]);
        module.controller('report.reviewController', reviewController);
        module.controller('ReviewEditorController', reviewEditorController);

        module.controller('report.journalRepairController', journalRepairController);
        module.service('journalRepairService', journalRepairService);

        module.service('reviewService', reviewService);
        module.service('companyService', companyService);

        module.controller("report.reportPreviewController", reportPreviewController);
        module.service('report.reportPreviewService', reportPreviewService);
        module.controller('report.reportPreviewModal', reportPreviewModal);

        module.config(reviewRoutes(initOptions.baseUrl, initOptions.baseDir));

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