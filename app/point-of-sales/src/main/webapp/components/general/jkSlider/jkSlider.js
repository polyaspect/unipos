//# sourceURL=pos.jkSlider.js
define([
    'angular',
    'css!pos.jkSliderCss'
], function (angular) {
    return ['$timeout', 'pos.initOptions', function (timeout, initOptions) {
        function renderSlider(scope, el) {
            var size = scope.jkSliderData.slider.columns * scope.jkSliderData.slider.rows;
            var totalSlides = Math.ceil(scope.jkSliderData.slider.data.length / size);
            var erg = [];
            var indexOfUsed = 0;
            for (var i = 0; i < totalSlides; i++) {
                var slide = [];
                for (var j = 0; j < scope.jkSliderData.slider.rows; j++) {
                    var row = [];
                    for (var k = 0; k < scope.jkSliderData.slider.columns; k++) {
                        row.push(scope.jkSliderData.slider.data[indexOfUsed]);
                        indexOfUsed++;
                    }
                    if (row.length == 0) {
                        for (var k = 0; k < scope.jkSliderData.slider.columns; k++) {
                            row.push(undefined);
                        }
                    }
                    slide.push(row);
                }
                erg.push(slide);
            }
            scope.jkSliderData.result = erg;
            scope.showPrevAndNextButton = scope.jkSliderData.slider.data.length > size;
            angular.element(window).trigger("resize");
        }

        return {
            restrict: 'E',
            templateUrl: initOptions.baseDir + 'components/general/jkSlider/jkSlider.html',
            replace: true,
            link: function (scope, el, attrs) {
                scope.$watch('jkSliderData.slider.data', function () {
                    renderSlider(scope, el);
                });
                renderSlider(scope, el);
                angular.element(window).on("resize", function () {
                    var slick = angular.element(el).find("slick");
                    if (slick != undefined && slick.length > 0 && slick[0].slick != undefined) {
                        timeout(function () {
                            angular.element(el).find("slick")[0].slick.setPosition();
                        }, 100);
                    }
                });
            }
        };
    }];
});