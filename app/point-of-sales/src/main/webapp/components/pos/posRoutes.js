//# sourceURL=pos.posRoutes.js
define([
    'angular',
    'pos.angularRoute'
], function () {
    return function (baseUrl, baseDir) {
        return ['$routeProvider', function ($routeProvider) {
            $routeProvider
                .when(baseUrl + '/pos/:catalogName?', {
                    templateUrl: baseDir + 'components/pos/index.html',
                    controller: 'pos.posController'
                })
                .when(baseUrl + '/login', {
                    templateUrl: baseDir + 'components/pos/index.html',
                    controller: 'pos.loginController'
                })
                .when(baseUrl + '/addNewDevice', {
                    templateUrl: baseDir + 'components/pos/index.html',
                    controller: 'pos.addNewDeviceController'
                })
                .when(baseUrl + '/addPrinterToDevice', {
                    templateUrl: baseDir + 'components/pos/index.html',
                    controller: 'pos.addPrinterToDeviceController'
                })
                .when(baseUrl + '/designer', {
                    templateUrl: baseDir + 'components/designer/designer.html',
                    controller: 'pos.designer.designer'
                })
                .when(baseUrl + '/designer/:collectionName', {
                    templateUrl: baseDir + 'components/designer/designer.html',
                    controller: 'pos.designer.designer'
                })
        }];
    }
});

