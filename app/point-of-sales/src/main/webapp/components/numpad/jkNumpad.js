//# sourceURL=pos.jkNumpad.js
define([
    'angular',
    'css!pos.jkNumpadCss'
], function (angular) {
    return ['pos.initOptions', function (initOptions) {
        return {
            templateUrl: initOptions.baseDir + "components/numpad/jkNumpad.html",
            replace: true,
            link: function (scope, el, attrs) {
                scope.jkSliderData = {
                    scrollButtons: {
                        prevOrNextButtonRoot: ".jk-numpad",
                        nextButton: ".scroll-button.next",
                        prevButton: ".scroll-button.prev",
                        slickPrevButton: "#slick-buttons .slick-prev",
                        slickNextButton: "#slick-buttons .slick-next",
                        prev: {
                            css: scope.area.data.scrollButton.prev.css
                        },
                        next: {
                            css: scope.area.data.scrollButton.next.css
                        }
                    },
                    slider: {
                        columns: scope.area.data.sliderDataWrapper.columns,
                        rows: scope.area.data.sliderDataWrapper.rows,
                        appendArrows: ".jk-numpad #slick-buttons",
                        data: scope.area.data.sliderDataWrapper.data
                    }
                };
            }
        }
    }]
});