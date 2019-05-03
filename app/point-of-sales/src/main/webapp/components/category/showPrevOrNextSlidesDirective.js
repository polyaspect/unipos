//# sourceURL=pos.showPrevOrNextSlidesDirective.js
define([
    'angular',
], function (angular) {
    return function () {
        return {
            restrict: 'A',
            link: function (scope, el, attrs) {
                el.bind('click', function () {
                    if (attrs.showPrevOrNext == "next") {
                        angular.element(attrs.slickButtonRoot + " " + attrs.slickNextButton).click();
                        if (angular.element(attrs.slickButtonRoot + " " + attrs.slickNextButton).attr("aria-disabled") == "true") {
                            el.attr("disabled", "disabled");
                            angular.element(attrs.slickButtonRoot + " " + attrs.prevButton).removeAttr("disabled")
                        } else {
                            el.removeAttr("disabled");
                            angular.element(attrs.slickButtonRoot + " " + attrs.prevButton).attr("disabled", "disabled");
                        }
                    } else if (attrs.showPrevOrNext == "prev") {
                        angular.element(attrs.slickButtonRoot + " " + attrs.slickPrevButton).click();
                        if (angular.element(attrs.slickButtonRoot + " " + attrs.slickPrevButton).attr("aria-disabled") == "true") {
                            el.attr("disabled", "disabled");
                            angular.element(attrs.slickButtonRoot + " " + attrs.nextButton).removeAttr("disabled")
                        } else {
                            el.removeAttr("disabled");
                            angular.element(attrs.slickButtonRoot + " " + attrs.prevButton).attr("disabled", "disabled");
                        }
                    }
                    el.blur();
                });
            }
        }
    };
});