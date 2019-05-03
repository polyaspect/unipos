//# sourceURL=pos.jkVisible.js
define([
    'angular'
], function (angular) {
    return function () {
        return {
            scope: {
                'pos.jkVisible': '@'
            },
            link: function (scope, el, attrs) {
                scope.$watch(attrs["pos.jkVisible"], function (visible) {
                    el.css('visibility', attrs["pos.jkVisible"] ? 'visible' : 'hidden');
                    if (attrs["pos.jkVisible"] && attrs["pos.jkVisible"].name && attrs["pos.jkVisible"].name == "") {
                        el.css('visibility', 'hidden');
                    }
                });
            }
        }
    }
});