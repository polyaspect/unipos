//# sourceURL=pos.positionService.js
define([], function () {
    return ['pos.orderService', function (orderService) {
        this.getMaxPosition = function (order) {
            if (order.orderItems.length > 0) {
                return Enumerable.From(order.orderItems).Max(function (x) {
                        return x.position;
                    }) + 1;
            } else {
                return 1;
            }
        };
    }];
});