//# sourceURL=pos.enterButton.splitOrderEnterStrategy.js
define([
    'angular'
], function (angular) {
    return {
        enterButton: function (injector) {
            injector.get("pos.splitOrderService").confirm();
        }
    };
});