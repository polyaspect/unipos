//# sourceURL=pos.app.js
require.config({
    paths: {
        'pos.angularRoute': 'assets/js/angularjs/1.4.3/angular-route',
        'pos.angularResource': 'assets/js/angularjs/1.4.3/angular-resource',
        'pos.ngAnimate': 'assets/js/angularjs/1.4.3/angular-animate',
        'pos.ngAria': 'assets/js/angular-aria/1.3.15/angular-aria',
        'pos.ngCookies': 'assets/js/angularjs/1.4.3/angular-cookies',
        'pos.hyper': 'assets/js/hyper/jquery.hypher',
        'pos.hyperDE': 'assets/js/hyper/de',
        'pos.ngMessages': 'assets/js/angularjs/1.4.3/angular-messages',
        'pos.angularRipple': 'components/general/angular-ripple',
        'pos.aSlick': 'assets/js/angular-slick/1.5.7/slick',
        'pos.angularSlick': 'assets/js/angular-slick/1.5.7/angular-slick',
        'pos.uiGrid': 'assets/js/ui-grid/ui-grid',
        'pos.uuid2': 'assets/js/angular-uuid2/angular-uuid2',
        'pos.moment': 'assets/js/moment/moment',

        'pos.sweetAlert': 'assets/js/sweetalert/sweetalert',
        'pos.sweetAlertCss': 'assets/css/sweetalert/sweetalert',
        'pos.ngSweetAlert': 'assets/js/sweetalert/SweetAlert.min',

        //CUSTOM
        'pos.posController': 'components/pos/posController',
        'pos.loginController': 'components/login/loginController',
        'pos.logoutController': 'components/logout/logoutController',
        'pos.posRoutes': 'components/pos/posRoutes',

        'pos.categoryService': 'components/category/categoryService',
        'pos.categoryPanelService': 'components/category/categoryPanelService',

        'pos.productService': 'components/product/productService',
        'pos.productPanelService': 'components/product/productPanelService',
        'pos.producSocketHandler': 'components/product/productSocketHandler',

        'pos.functionsController': 'components/function/functionsController',
        'pos.functionsDirective': 'components/function/functionsDirective',
        'pos.functionsService': 'components/function/functionsService',

        'pos.numpadController': 'components/numpad/numpadController',

        'pos.areaDirective': 'components/area/areaDirective',
        'pos.areaService': 'components/area/areaService',
        'pos.areaConfigDirective': 'components/area/areaConfigDirective',

        'pos.hyperDirective': 'components/general/hyperDirective',
        'pos.keyboardListener': 'components/general/keyboardListener',
        'pos.jkVisible': 'components/general/jkVisible',

        'pos.jkSlider': 'components/general/jkSlider/jkSlider',
        'pos.showPrevOrNextSlidesDirective': 'components/category/showPrevOrNextSlidesDirective',
        'pos.jkSliderService': 'components/general/jkSlider/jkSliderService',

        'pos.jkNumpad': 'components/numpad/jkNumpad',
        'pos.numpadPanelService': 'components/numpad/numpadPanelService',
        'pos.quantityService': 'components/quantity/quantityService',

        'pos.jkPanel': 'components/general/jkPanel/jkPanel',
        'pos.jkPanelService': 'components/general/jkPanel/jkPanelService',

        'pos.dataService': 'components/general/dataService',
        'pos.enterService': 'components/general/enterService',

        'pos.orderService': 'components/order/orderService',
        'pos.orderDirectiveService': 'components/order/orderDirectiveService',
        'pos.positionService': 'components/order/positionService',
        'pos.serviceConfig': 'components/service/serviceConfig',
        'pos.invoiceService': 'components/invoices/invoiceService',

        'pos.queueService': 'components/general/queueService',

        'pos.reversalService': 'components/function/functions/reversalService',
        'pos.discountService': 'components/function/functions/discountService',
        'pos.switchScreenService': 'components/function/functions/switchScreenService',
        'pos.subTotalService': 'components/function/functions/subTotalService',
        'pos.deviceService': 'components/general/deviceService',
        'pos.loginService': 'components/login/loginService',
        'pos.adminMenuService': 'components/admin/adminMenuService',
        'pos.logoutService': 'components/logout/logoutService',
        'pos.showInvoicesService': 'components/invoices/showInvoicesService',
        'pos.showInvoicesGridService': 'components/invoices/showInvoicesGridService',

        'pos.addNewDeviceController': 'components/store/addNewDevice/addNewDeviceController',
        'pos.addNewDeviceService': 'components/store/addNewDevice/addNewDeviceService',

        'pos.paymentController': 'components/payment/paymentController',
        'pos.paymentDirective': 'components/payment/paymentDirective',
        'pos.paymentService': 'components/payment/paymentService',

        'pos.addPrinterToDeviceController': "components/printer/addPrinterToDeviceController",
        'pos.printerService': 'components/printer/printerService',
        'pos.printerGridService': 'components/printer/printerGridService',

        'pos.slick': 'assets/css/angular-slick/1.5.7/slick',
        'pos.angular-textfill': 'assets/js/angular-textfill/angular-textfill',
        'pos.linq': 'assets/js/linq/linq',

        //Socket
        'pos.sockJs': 'assets/js/ng-stomp/sockjs',
        'pos.ngStomp': 'assets/js/ng-stomp/ng-stomp',
        'pos.stomp': 'assets/js/ng-stomp/stomp',
        'pos.stompie': 'assets/js/ng-stomp/stompie',

        'pos.posCss': 'components/pos/pos',
        'pos.paymentDirectiveCss': 'components/payment/paymentDirective',
        'pos.orderDirectiveCss': 'components/order/orderDirectiveService',
        'pos.jkNumpadCss': 'components/numpad/jkNumpad',
        'pos.jkSliderCss': 'components/general/jkSlider/jkSlider',
        'pos.jkPanelCss': 'components/general/jkPanel/jkPanel',
        'pos.functionsDirectiveCss': 'components/function/functionsDirective',
        'pos.categoryDirectiveCss': 'components/category/categoryDirective',
        'pos.areaDirectiveCss': 'components/area/areaDirective',
        'pos.areaConfigDirectiveCss': 'components/area/areaConfigDirective',
        'pos.fontAwesome': 'assets/css/font-awesome/4.3.0/css/font-awesome.min',

        'pos.jkStoreChooserService': 'components/store/jkStoreChooser/jkStoreChooserService',
        'pos.cashbookEntryService': 'components/cashBook/cashbookEntryService',
        'pos.cashbookEntryGridService': 'components/cashBook/cashbookEntryGridService',

        "pos.design.angularSortableView": "assets/js/angular-sortable-view/angular-sortable-view",
        "pos.design.jqueryUi": "assets/js/jquery-ui/1.11.4/jquery-ui",
        "pos.design.jqueryUiCss": "assets/css/jquery-ui/1.11.4/jquery-ui",

        "pos.design.jkNumpadDirectiveSettings": "components/designer/designerSettings/jkNumpadSettings/jkNumpadSettings",
        "pos.design.jkNumpadDirectiveSettingsCss": "components/designer/designerSettings/jkNumpadSettings/jkNumpadSettings",

        "pos.design.globalSettings": "components/designer/globalSettings/globalSettings",
        "pos.design.screenSetSettings": "components/designer/screenSetSettings/screenSetSettings",
        "pos.design.screenSettings": "components/designer/screenSettings/screenSettings",

        "pos.loadingController": "components/loading/loadingController",
        "pos.loadingService": "components/loading/loadingService",

        "pos.socketService": "components/socket/socketService",

        "pos.strategyConfig": "components/strategies/strategyConfig",
        "pos.designerConfig": "components/designer/designerConfig",
        "pos.uiConfig": "components/ui/uiConfig",

        "pos.chooseOrderService" : "components/chooseOrder/chooseOrderService",
        "pos.chooseOrderGridService" : "components/chooseOrder/chooseOrderGridService",

        "pos.chooseOrderItemService" : "components/chooseOrderItem/chooseOrderItemService",
        "pos.chooseOrderItemGridService" : "components/chooseOrderItem/chooseOrderItemGridService",

        "pos.openOrderService" : "components/openOrder/openOrderService",
        "pos.openOrderButtonService" : "components/openOrder/openOrderButtonService",

        "pos.splitOrderService" : "components/splitOrder/splitOrderService",
        "pos.splitOrderButtonService" : "components/splitOrder/splitOrderButtonService",

        "pos.revertInvoiceService" : "components/revertInvoice/revertInvoiceService",
        "pos.revertInvoiceButtonService" : "components/revertInvoice/revertInvoiceButtonService",

        "pos.reprintInvoiceService" : "components/reprintInvoice/reprintInvoiceService",
        "pos.reprintInvoiceButtonService" : "components/reprintInvoice/reprintInvoiceButtonService",

        "pos.vAccordion": "assets/js/v-accordion/v-accordion",
        "pos.vAccordionCss": "assets/css/v-accordion/v-accordion",

        'pos.bootstrapCss': 'node_modules/bootstrap/dist/css/bootstrap',
        'pos.angularUiBootstrap': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap',
        'pos.angularUiBootstrapTpls': 'node_modules/angular-ui-bootstrap/dist/ui-bootstrap-tpls',

        'pos.angularUiSelect': 'node_modules/ui-select/dist/select',

        'pos.angularBootstrapCheckbox': 'node_modules/angular-bootstrap-checkbox/angular-bootstrap-checkbox',

        'pos.jkLayoutCss': 'assets/css/jkLayout/jkLayout',

        'pos.angularSlider': 'node_modules/angularjs-slider/dist/rzslider.min'
    },
    shim: {
        'pos.angularRoute': ['angular'],
        'pos.angularResource': ['angular'],
        'pos.ngAnimate': ['angular'],
        'pos.ngAria': ['angular'],
        'pos.hyper': {
            'exports': 'hyper'
        },
        'pos.hyperDE': ['pos.hyper'],
        'pos.ngMessages': ['angular'],
        'pos.angularRipple': ['angular'],
        'pos.angularSlick': ['angular'],
        'pos.uiGrid': ['angular'],
        'pos.ngStomp': ['angular'],
        'pos.stompie': ['angular'],
        'pos.sockJs': ['angular'],
        "pos.design.angularSortableView": ["angular"],
        "pos.sweetAlert": ["angular"],
        "pos.ngSweetAlert": ["angular"],
        "pos.ngCookies": ["angular"],
        "pos.vAccordion": ["angular"],
        'pos.angularUiSelect': ['angular'],
        'pos.angularUiBootstrap': ['angular'],
        'pos.angularUiBootstrapTpls': ['angular', 'pos.angularUiBootstrap'],
        'pos.angularBootstrapCheckbox': ['angular'],
    }
})
;

// adding nav bar of core module

if (deps == undefined) {
    require.config({
        paths: {
            'core.navController': '../components/nav/navController',
            'core.navDirective': '../components/nav/navDirective',
            'core.navService': '../components/nav/navService',
            'core.navDirectiveCss': '../components/nav/navDirective',
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
            'core.navDirectiveCss': '../../test/webapp/mock/nav/navDirective'
        },
        shim: {
            'core.ngMaterialMenuSidenav': ['angular']
        }
    });
}

var defineArray = [];
defineArray.push('angular', 'pos.angularRoute', 'pos.ngAnimate', 'pos.ngAria', "pos.ngCookies"); //Angular Stuff
defineArray.push('pos.posController', 'pos.loginController', 'pos.posRoutes');  //PosComponent Suff
defineArray.push('pos.categoryService', 'pos.categoryPanelService'); //CategoryComponent Stuff
defineArray.push('pos.productService', 'pos.productPanelService', 'pos.producSocketHandler'); //Product Stuff
defineArray.push('pos.numpadController');
defineArray.push('pos.areaDirective', 'pos.areaService', 'pos.areaConfigDirective');
defineArray.push('pos.hyperDirective', 'pos.keyboardListener');
defineArray.push('pos.jkVisible');
defineArray.push('pos.jkSlider', 'pos.showPrevOrNextSlidesDirective', 'pos.jkSliderService');
defineArray.push('pos.jkNumpad', 'pos.numpadPanelService');
defineArray.push('pos.quantityService');
defineArray.push('pos.jkPanel', 'pos.jkPanelService');
defineArray.push('pos.dataService', 'pos.enterService');
defineArray.push('pos.orderService', 'pos.positionService', 'pos.serviceConfig', 'pos.printerService', 'pos.addPrinterToDeviceController');
defineArray.push('pos.queueService');
defineArray.push('pos.orderDirectiveService', 'pos.jkStoreChooserService', 'pos.cashbookEntryService', 'pos.cashbookEntryGridService', 'pos.printerGridService');

//Functions Stuff
defineArray.push('pos.functionsController', 'pos.functionsDirective', 'pos.functionsService');
defineArray.push('pos.reversalService', 'pos.discountService', 'pos.switchScreenService', 'pos.subTotalService', 'pos.deviceService', 'pos.loginService');
defineArray.push('pos.addNewDeviceService', 'pos.addNewDeviceController');

defineArray.push('pos.paymentController', "pos.paymentDirective", 'pos.paymentService');

defineArray.push('pos.loadingService', 'pos.loadingController');
defineArray.push('pos.socketService');
defineArray.push('pos.logoutService');
defineArray.push('pos.showInvoicesService');
defineArray.push('pos.showInvoicesGridService');

defineArray.push('pos.invoiceService');
defineArray.push('pos.adminMenuService');

defineArray.push('pos.strategyConfig');
defineArray.push('pos.designerConfig');
defineArray.push('pos.uiConfig');

defineArray.push('pos.chooseOrderService');
defineArray.push('pos.chooseOrderGridService');
defineArray.push('pos.chooseOrderItemService');
defineArray.push('pos.chooseOrderItemGridService');
defineArray.push('pos.openOrderService');
defineArray.push('pos.openOrderButtonService');
defineArray.push('pos.splitOrderService');
defineArray.push('pos.splitOrderButtonService');
defineArray.push('pos.revertInvoiceService');
defineArray.push('pos.revertInvoiceButtonService');
defineArray.push('pos.reprintInvoiceService');
defineArray.push('pos.reprintInvoiceButtonService');

defineArray.push('core.navController', 'core.navDirective');

//Sachen die nicht als funktionsparameter gebracuhtt werden
defineArray.push('pos.aSlick', 'pos.angularSlick');
defineArray.push('css!pos.slick');
defineArray.push('pos.hyper', 'pos.hyperDE'); //Hyper Stuff
defineArray.push('pos.angularRipple', 'css!components/general/angular-ripple.css'); //Material Design Ripple Stuff
defineArray.push('css!assets/css/styles.css'); //Styles for this module
defineArray.push('pos.angular-textfill');
defineArray.push('pos.ngMessages');
defineArray.push('pos.uiGrid', 'css!assets/css/ui-grid/ui-grid.css', 'css!assets/css/ui-grid/ui-grid-material.css');
defineArray.push('pos.uuid2');
defineArray.push('pos.sweetAlert', 'css!pos.sweetAlertCss', "pos.ngSweetAlert", "pos.vAccordion", "css!pos.vAccordionCss");
defineArray.push("pos.angularUiSelect",
    'pos.angularBootstrapCheckbox',
    'css!pos.angularUiSelect',
    'css!pos.bootstrapCss', 'pos.angularUiBootstrap', 'pos.angularUiBootstrapTpls');
defineArray.push('css!pos.jkLayoutCss');
defineArray.push('pos.angularSlider', 'css!pos.angularSlider');
define(defineArray,
    function (angular,
              ngRoute,
              ngAnimate,
              ngAria,
              ngCookies,
              posController,
              loginController,
              posRoutes,
              categoryService,
              categoryPanelService,
              productService,
              productPanelService,
              producSocketHandler,
              numpadController,
              areaDirective,
              areaService,
              areaConfigDirective,
              hyperDirective,
              keyboardListener,
              jkVisible,
              jkSlider,
              showPrevOrNextSlidesDirective,
              jkSliderService,
              jkNumpad,
              numpadPanelService,
              quantityService,
              jkPanel,
              jkPanelService,
              dataService,
              enterService,
              orderService,
              positionService,
              serviceConfig,
              printerService,
              addPrinterToDeviceController,
              queueService,
              orderDirectiveService,
              jkStoreChooserService,
              cashbookEntryService,
              cashbookEntryGridService,
              printerGridService,
              functionsController,
              functionsDirective,
              functionsService,
              reversalService,
              discountService,
              switchScreenService,
              subTotalService,
              deviceService,
              loginService,
              addNewDeviceService,
              addNewDeviceController,
              paymentController,
              paymentDirective,
              paymentService,
              loadingService,
              loadingController,
              socketService,
              logoutService,
              showInvoicesService,
              showInvoicesGridService,
              invoiceService,
              adminMenuService,
              strategyConfig,
              designerConfig,
              uiConfig,
              chooseOrderService,
              chooseOrderGridService,
              chooseOrderItemService,
              chooseOrderItemGridService,
              openOrderService,
              openOrderButtonService,
              splitOrderService,
              splitOrderButtonService,
              revertInvoiceService,
              revertInvoiceButtonService,
              reprintInvoiceService,
              reprintInvoiceButtonService,
              navController,
              navDirective) {
        var container = {
            configModule: function () {
                return angular.module('unipos.pos', [
                    'ngRoute',
                    'ngResource',
                    'ngAnimate',
                    'ngMessages',
                    'ngAria',
                    'slick',
                    'angularRipple',
                    'ngTextFill',
                    'ui.grid',
                    'ui.grid.selection',
                    'ui.grid.autoResize',
                    'angularUUID2',
                    'ngMockE2E',
                    'ngStomp',
                    'angular-sortable-view',
                    'oitozero.ngSweetAlert',
                    'ngCookies',
                    'vAccordion',
                    'ui.select',
                    'ui.bootstrap',
                    "ui.checkbox",
                    'rzModule'
                ]);
            },
            configRoutes: function (ngModule, url) {
                ngModule.config(['$routeProvider', function ($routeProvider) {
                    $routeProvider.otherwise({redirectTo: url + '/pos'});
                }]);
            }
        };


        var init = function (initOptions) {
            var module = container.configModule();

            module.run(function ($httpBackend) {
                String.prototype.paddingLeft = function (c, n) {
                    if (!this || !c || this.length >= n) {
                        return this.substr(0, n);
                    }
                    var max = (n - this.length) / c.length;
                    var s = this;
                    for (var i = 0; i < max; i++) {
                        s = c + s;
                    }
                    return s;
                };
                String.prototype.paddingRight = function (c, n) {
                    if (!this || !c || this.length >= n) {
                        return this.substr(0, n);
                    }
                    var max = (n - this.length) / c.length;
                    var s = this;
                    for (var i = 0; i < max; i++) {
                        s += c;
                    }
                    return s;
                };
                $httpBackend.whenGET().passThrough();
                $httpBackend.whenPOST().passThrough();
                $httpBackend.whenDELETE().passThrough();
            });

            container.configRoutes(module, initOptions.baseUrl);
            module.filter('quantityFilter', function () {
                return function (value) {
                    return value + " x";
                };
            });

            module.filter('euroFilter', function () {
                return function (value) {
                    return "€ " + value;
                };
            });

            uiConfig(module);
            strategyConfig(module);
            designerConfig(module);
            serviceConfig(module);

            module.controller('pos.posController', posController);
            module.controller('pos.loginController', loginController);
            module.service('pos.dataService', dataService);
            module.service('pos.enterService', enterService);

            module.service('pos.categoryService', categoryService);
            module.service('pos.categoryPanelService', categoryPanelService);

            module.service('pos.productService', productService);
            module.service('pos.productPanelService', productPanelService);
            module.service('pos.productSocketHandler', producSocketHandler);

            module.controller('pos.functionsController', functionsController);
            module.directive('pos.functionsDirective', functionsDirective);
            module.service('pos.functionsService', functionsService);
            module.service('pos.reversalService', reversalService);
            module.service('pos.discountService', discountService);
            module.service('pos.switchScreenService', switchScreenService);
            module.service('pos.subTotalService', subTotalService);
            module.service('pos.deviceService', deviceService);
            module.service('pos.loginService', loginService);
            module.service('pos.logoutService', logoutService);
            module.service('pos.showInvoicesService', showInvoicesService);
            module.service('pos.showInvoicesGridService', showInvoicesGridService);

            module.controller('pos.addNewDeviceController', addNewDeviceController);
            module.service('pos.addNewDeviceService', addNewDeviceService);

            module.directive('pos.hyperDirective', hyperDirective);
            module.directive('pos.keyboardListener', keyboardListener);

            module.directive('pos.areaDirective', areaDirective);
            module.directive('pos.areaConfigDirective', areaConfigDirective);
            module.service('pos.areaService', areaService);

            module.controller('pos.numpadController', numpadController);
            module.directive('pos.jkNumpad', jkNumpad);
            module.service('pos.numpadPanelService', numpadPanelService);

            module.service('pos.orderService', orderService);
            module.service('pos.positionService', positionService);
            module.service('pos.printerService', printerService);
            module.controller('pos.addPrinterToDeviceController', addPrinterToDeviceController);
            module.service('pos.quantityService', quantityService);
            module.service('pos.queueService', queueService);

            module.directive('pos.jkPanel', jkPanel);
            module.service('pos.jkPanelService', jkPanelService);

            module.directive('pos.jkVisible', jkVisible);

            module.directive('pos.jkSlider', jkSlider);
            module.directive('pos.showPrevOrNextSlidesDirective', showPrevOrNextSlidesDirective);
            module.service('pos.jkSliderService', jkSliderService);

            module.controller('pos.paymentController', paymentController);
            module.directive('pos.paymentDirective', paymentDirective);
            module.service('pos.paymentService', paymentService);


            module.controller('pos.loadingController', loadingController);
            module.service('pos.loadingService', loadingService);

            module.service('pos.jkStoreChooserService', jkStoreChooserService);
            module.service('pos.orderDirectiveService', orderDirectiveService);

            module.service('pos.cashbookEntryService', cashbookEntryService);
            module.service('pos.cashbookEntryGridService', cashbookEntryGridService);
            module.service('pos.printerGridService', printerGridService);

            module.service('pos.socketService', socketService);

            module.service('pos.invoiceService', invoiceService);
            module.service('pos.adminMenuService', adminMenuService);


            module.service('pos.chooseOrderService', chooseOrderService);
            module.service('pos.chooseOrderGridService', chooseOrderGridService);
            module.service('pos.chooseOrderItemService', chooseOrderItemService);
            module.service('pos.chooseOrderItemGridService', chooseOrderItemGridService);
            module.service('pos.openOrderService', openOrderService);
            module.service('pos.openOrderButtonService', openOrderButtonService);
            module.service('pos.splitOrderService', splitOrderService);
            module.service('pos.splitOrderButtonService', splitOrderButtonService);

            module.service('pos.revertInvoiceService', revertInvoiceService);
            module.service('pos.revertInvoiceButtonService', revertInvoiceButtonService);

            module.service('pos.reprintInvoiceService', reprintInvoiceService);
            module.service('pos.reprintInvoiceButtonService', reprintInvoiceButtonService);

            module.config(posRoutes(initOptions.baseUrl, initOptions.baseDir));

            module.value('pos.initOptions', {
                baseUrl: initOptions.baseUrl,
                baseDir: initOptions.baseDir
            });
            module.value("pos.textModes", {
                quantity: "quantity"
            });
            module.value('pos.directiveIdsConst', {
                paymentPriceEnterDisplay: "infoDisplay2",
                quantityDisplay: "infoDisplay",
                orderItemGrid: "orderItemGrid",
                functions1: "functions1",
                currentAreaConfig: "currentConfig",
                areaConfigAll: "allAreaConfigs",
                categorySlider: "categorySlider",
                productSlider: "productSlider"
            });
            module.value('pos.areaNames', {
                home: "default",
                payment: "paymentScreen",
                third: "thirdScreen",
                login: "login"
            });
            module.value('pos.urlSettings', {
                baseUrl: "",
                newProductOrderItem: "/pos/productOrderItem/new",
                newReversalOrderItem: "/pos/reversalOrderItem/new",
                newPaymentOrderItem: "/pos/paymentOrderItem/new",
                newChangeOrderItem: "/pos/changeOrderItem/new",
                newOrderDiscountOrderItem: "/pos/orderDiscountOrderItem/new",
                newOrderItemDiscountOrderItem: "/pos/orderItemDiscountOrderItem/new",
                createFromOrder: "/pos/invoices/createFromOrder",
                getOrdersByUser: "/pos/orders/getByUserAndDevice",
                getAllOrders: "/pos/orders/getByUser",
                getAllInvoices: "/pos/invoices",
                getAllAreaConfigs: "/design/screenset/getScreensets",
                getAllPaymentMethods: "/data/paymentMethods",
                getAllOrderDiscounts: "/data/discounts",
                getAllCategories: "/data/categories",
                getAllProducts: "/data/products",
                createNewOrder: "/pos/orders/createNewOrder",
                createNewDevice: "/socket/device/addStoreToDevice",
                deleteOrderItem: "/pos/orderItems/deleteByOrderItemId",
                addCashBookEntry: "/pos/cashbook",
                getCashBookEntries: "/pos/cashbook/sinceLastDailySettlement",
                lastClosedDailySettlement: "/pos/dailySettlements/lastClosed",
                reduceStockAmountForProductGuid: "/data/products/reduceStockAmountForProductGuid",
                log: "/pos/log"
            });
            return container;
        };

        return init;
    }
)
;
defineArray.push('angularMocks');
defineArray.push('pos.linq', 'pos.ngStomp', "pos.design.jqueryUi", "css!pos.design.jqueryUiCss", "pos.design.angularSortableView");
