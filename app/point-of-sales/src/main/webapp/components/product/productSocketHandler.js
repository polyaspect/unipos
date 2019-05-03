//# sourceURL=pos.productSocketHandler.js
define([
    'angular'
], function (angular) {
    return ['pos.productService', 'pos.socketService', function (productService, socketService) {
        socketService.subscribeBroadcast("stockAmountChanged", function (data) {
            productService.setAllProducts(true);
        });
    }];
});