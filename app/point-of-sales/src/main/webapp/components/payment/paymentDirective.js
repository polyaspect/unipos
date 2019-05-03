//# sourceURL=pos.paymentDirective.js
define([
    'angular',
    'css!pos.paymentDirectiveCss'
], function (angular) {
    return ['pos.jkSliderService', '$rootScope','pos.initOptions', function (jkSliderService, rootScope, initOptions) {
        function setData(scope) {
            var data = jkSliderService.getDirectiveData(scope.area.data.id);
            scope.jkSliderData.scrollButtons.prev.css = data.scrollButton.prev.css;
            scope.jkSliderData.scrollButtons.next.css = data.scrollButton.next.css;
            scope.jkSliderData.slider.columns = data.sliderDataWrapper.columns;
            scope.jkSliderData.slider.rows = data.sliderDataWrapper.rows;
            scope.jkSliderData.slider.data = data.sliderDataWrapper.data;
        }

        return {
            restrict: 'E',
            templateUrl: initOptions.baseDir + 'components/payment/paymentDirective.html',
            replace: true,
            link: function (scope, el, attrs) {
                jkSliderService.setDirectiveData(scope.area.data.id, scope.area.data);
                rootScope.$on(jkSliderService.serviceName + "-" + scope.area.data.id, function () {
                    setData(scope);
                });
                scope.jkSliderData = {
                    scrollButtons: {
                        prevOrNextButtonRoot: ".payment-directive",
                        nextButton: ".scroll-button.next",
                        prevButton: ".scroll-button.prev",
                        slickPrevButton: "#slick-buttons .slick-prev",
                        slickNextButton: "#slick-buttons .slick-next",
                        prev: {},
                        next: {}
                    },
                    slider: {
                        appendArrows: ".payment-directive #slick-buttons",
                        buttonCallback: scope.buttonClick
                    }
                };
                setData(scope);
            }
        }
    }];
});