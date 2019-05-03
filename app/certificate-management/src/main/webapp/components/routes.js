//# sourceURL=signatureRoutes.js
define([
        'angular',
        'signature.angularRoute'
    ], function () {
        return function (baseUrl, baseDir) {
            return ['$routeProvider', function ($routeProvider) {
                $routeProvider.when(baseUrl + '/signatureOptions', {
                        templateUrl: baseDir + 'components/signatureOptions/signatureOptionsView.html',
                        controller: 'SignatureOptionsController'
                    }).when(baseUrl + '/zeroInvoices', {
                    templateUrl: baseDir + 'components/zeroInvoice/zeroInvoiceView.html',
                    controller: 'ZeroInvoiceController'
                })
            }];
        }
    }
);

