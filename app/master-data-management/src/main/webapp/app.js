/**
 * Created by ggradnig on 2015-04-28
 */
//# sourceURL=data.app.js

require.config({
    paths: {
        'data.angularRoute': 'node_modules/angular-route/angular-route',
        'data.angularResource': 'node_modules/angular-resource/angular-resource',
        'data.ngAnimate': 'node_modules/angular-animate/angular-animate',
        'data.ngAria': 'node_modules/angular-aria/angular-aria',

        'data.categoryController': 'components/category/categoryController',
        'data.categoryEditorController': 'components/category/editor/categoryEditor',
        'data.categoryService': 'components/category/categoryService',
        'data.categoryStyle': 'components/category/category',

        'data.productController': 'components/product/productController',
        'data.productEditorController': 'components/product/editor/productEditor',
        'data.productService': 'components/product/productService',
        'data.productRoutes': 'components/product/productRoutes',
        'data.productStyle': 'components/product/product',

        'data.paymentMethodController': 'components/paymentMethod/paymentMethodController',
        'data.paymentMethodEditorController': 'components/paymentMethod/editor/paymentMethodEditor',
        'data.paymentMethodService': 'components/paymentMethod/paymentMethodService',
        'data.paymentMethodStyle': 'components/paymentMethod/paymentMethod',

        'data.discountController': 'components/discount/discountController',
        'data.discountEditorController': 'components/discount/editor/discountEditor',
        'data.discountService': 'components/discount/discountService',
        'data.discountStyle': 'components/discount/discount',

        'data.taxRateController': 'components/taxRate/taxRateController',
        'data.taxRateEditorController': 'components/taxRate/editor/taxRateEditor',
        'data.taxRateService': 'components/taxRate/taxRateService',
        'data.taxRateStyle': 'components/taxRate/taxRate',

        'data.storeController': 'components/store/storeController',
        'data.storeEditorController': 'components/store/editor/storeEditor',
        'data.storeService': 'components/store/storeService',
        'data.storeStyle': 'components/store/store',

        'data.companyController': 'components/company/companyController',
        'data.companyEditorController': 'components/company/editor/companyEditor',
        'data.companyService': 'components/company/companyService',
        'data.companyStyle': 'components/company/company',

        'data.uiGrid': 'node_modules/angular-ui-grid/ui-grid',
        'data.uiGridDraggableRows': 'node_modules/ui-grid-draggable-rows/js/draggable-rows',
        'data.uiGridDraggableRowsCss': 'assets/css/uiGridDraggable/uiGridDraggable',

        'data.font-awesome': 'node_modules/font-awesome/css/font-awesome',

        'data.bootstrapCss': 'node_modules/bootstrap/dist/css/bootstrap',
        'data.angularUiBootstrap': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap',
        'data.angularUiBootstrapTpls': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap-tpls',

        'data.angularUiSelect': 'node_modules/ui-select/dist/select',

        'data.angularBootstrapCheckbox': 'node_modules/angular-bootstrap-checkbox/angular-bootstrap-checkbox',

        'data.sweetAlert': 'assets/js/sweetAlert/sweetalert.min',
        'data.sweetAlertStyle': 'assets/css/sweetAlert/sweetalert',

        'data.jkLayoutCss': 'assets/css/jkLayout/jkLayout',

        'data.linq': 'assets/js/linq/linq',
    },
    shim: {
        'data.angularRoute': ['angular'],
        'data.angularResource': ['angular'],
        'angularMocks': ['angular'],
        'data.ngAnimate': ['angular'],
        'data.ngAria': ['angular'],
        'data.uiGrid': ['angular'],
        'data.uiGridDraggableRows': ['angular'],
        'data.angularUiSelect': ['angular'],
        'data.angularUiBootstrap': ['angular'],
        'data.angularUiBootstrapTpls': ['angular', 'data.angularUiBootstrap'],
        'data.angularBootstrapCheckbox': ['angular'],
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
    'data.angularRoute',
    'data.productController',
    'data.productEditorController',
    'data.productRoutes',
    'data.productService',
    'data.categoryController',
    'data.categoryEditorController',
    'data.categoryService',
    'data.paymentMethodController',
    'data.paymentMethodEditorController',
    'data.paymentMethodService',
    'data.discountController',
    'data.discountEditorController',
    'data.discountService',
    'data.taxRateController',
    'data.taxRateEditorController',
    'data.taxRateService',
    'data.storeController',
    'data.storeEditorController',
    'data.storeService',
    'data.companyController',
    'data.companyEditorController',
    'data.companyService',
    'core.navController',
    'core.navDirective',
    'core.ngMaterialMenuSidenav',
    'data.linq',
    'css!core.ngMaterialMenuSidenavCss',
    "data.ngAnimate", "data.ngAria",
    'css!data.bootstrapCss', 'data.angularUiBootstrap', 'data.angularUiBootstrapTpls',
    "data.uiGrid",
    "css!data.uiGrid",
    'data.uiGridDraggableRows',
    'css!data.uiGridDraggableRowsCss',
    "css!data.font-awesome",
    "data.angularUiSelect",
    'data.angularBootstrapCheckbox',
    'css!data.angularUiSelect'
], function (angular,
             ngRoute,
             productController,
             productEditorController,
             productRoutes,
             productService,
             categoryController,
             categoryEditorController,
             categoryService,
             paymentMethodController,
             paymentMethodEditorController,
             paymentMethodService,
             discountController,
             discountEditorController,
             discountService,
             taxRateController,
             taxRateEditorController,
             taxRateService,
             storeController,
             storeEditorController,
             storeService,
             companyController,
             companyEditorController,
             companyService,
             navController,
             navDirective) {
    var container = {
        configModule: function () {
            return angular.module('unipos.data', [
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
                'ui.grid.draggable-rows',
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
            $routeProvider.otherwise({redirectTo: initOptions.baseUrl + '/products'});
        }]);

        module.service("productService", productService);
        module.service("categoryService", categoryService);
        module.service("taxRateService", taxRateService);
        module.service("paymentMethodService", paymentMethodService);
        module.service("discountService", discountService);
        module.service("taxRateService", taxRateService);
        module.service("storeService", storeService);
        module.service("companyService", companyService);

        module.controller('ProductController', productController);
        module.controller('ProductEditorController', productEditorController);
        module.controller("PaymentMethodController", paymentMethodController);
        module.controller('PaymentMethodEditorController', paymentMethodEditorController);
        module.controller("DiscountController", discountController);
        module.controller("DiscountEditorController", discountEditorController);
        module.controller("CategoryController", categoryController);
        module.controller('CategoryEditorController', categoryEditorController);
        module.controller("TaxRateController", taxRateController);
        module.controller("TaxRateEditorController", taxRateEditorController);
        module.controller("StoreController", storeController);
        module.controller("StoreEditorController", storeEditorController);
        module.controller("CompanyController", companyController);
        module.controller("CompanyEditorController", companyEditorController);
        module.config(productRoutes(initOptions.baseUrl, initOptions.baseDir));

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