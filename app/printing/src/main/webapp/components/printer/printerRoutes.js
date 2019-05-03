define([
    'angular',
    'printer.angularRoute'
], function(){
        return function(baseUrl, baseDir) {
            return ['$routeProvider', function ($routeProvider) {
                $routeProvider.when(baseUrl + '/printers', {
                    templateUrl: baseDir + 'components/printer/printerIndexView.html',
                    controller: 'PrinterController'
                })
            }];
        }
    }
);

