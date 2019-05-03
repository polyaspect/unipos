define([
    'angular',
    'data.angularRoute'
], function(){
        return function(urlPrefix) {
            return ['$routeProvider', function ($routeProvider) {
                $routeProvider.when(urlPrefix + 'paymentMethods', {
                    templateUrl: 'components/paymentMethod/paymentMethodIndexView.html',
                    controller: 'PaymentMethodController'
                })
            }];
        }
    }
);

