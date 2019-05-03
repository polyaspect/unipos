//# sourceURL=pos.paymentController.js
define([
    'angular',
    'pos.angularResource',
], function (angular) {
    return ['$scope', 'pos.quantityService', 'pos.orderService', 'pos.directiveIdsConst', 'pos.areaNames', 'pos.paymentService',
        function (scope, quantityService, orderService, directiveIdsConst, areaNames, paymentService) {
            scope.buttonClick = function (cell) {
                paymentService.addPaymentOrderItem(cell);
            }
        }];
});