//# sourceURL=productRoutes.js
define([
        'angular',
        'data.angularRoute'
    ], function () {
        return function (baseUrl, baseDir) {
            return ['$routeProvider', function ($routeProvider) {
                $routeProvider.when(baseUrl + '/products', {
                        templateUrl: baseDir + 'components/product/productIndexView.html',
                        controller: 'ProductController'
                    })
                    .when(baseUrl + '/paymentMethods', {
                        templateUrl: baseDir + 'components/paymentMethod/paymentMethodIndexView.html',
                        controller: 'PaymentMethodController'
                    })
                    .when(baseUrl + '/discounts', {
                        templateUrl: baseDir + 'components/discount/discountIndexView.html',
                        controller: 'DiscountController'
                    })
                    .when(baseUrl + '/categories', {
                        templateUrl: baseDir + 'components/category/categoryView.html',
                        controller: 'CategoryController'
                    })
                    .when(baseUrl + '/taxRates', {
                        templateUrl: baseDir + 'components/taxRate/taxRateView.html',
                        controller: 'TaxRateController'
                    })
                    .when(baseUrl + '/stores', {
                        templateUrl: baseDir + 'components/store/storeView.html',
                        controller: 'StoreController'
                    })
                    .when(baseUrl + '/companies', {
                        templateUrl: baseDir + 'components/company/companyView.html',
                        controller: 'CompanyController'
                    })
                    .otherwise({ redirectTo: '/'});
            }];
        }
    }
);

