//# sourceURL=pos.functionsDirective.js
define([
    'angular',
    'css!pos.functionsDirectiveCss'
], function (angular) {
    return ['pos.jkSliderService', '$rootScope', 'pos.initOptions', function (jkSliderService, rootScope, initOptions) {
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
            templateUrl: initOptions.baseDir + 'components/function/functionsDirective.html',
            replace: true,
            link: function (scope, el, attrs) {
                scope.asdf = [
                    [
                        {
                            name: "Storno",
                            posX: 0,
                            posY: 0,
                            width: 5,
                            height: 5
                        },
                        {
                            name: "Storno2",
                            posX: 10,
                            posY: 0,
                            width: 5,
                            height: 5
                        },
                        {
                            name: "Storno3",
                            posX: 5,
                            posY: 10,
                            width: 5,
                            height: 14
                        }
                    ],
                    [
                        {
                            name: "Storno4",
                            posX: 0,
                            posY: 0,
                            width: 5,
                            height: 5
                        },
                        {
                            name: "Storno5",
                            posX: 10,
                            posY: 0,
                            width: 5,
                            height: 5
                        },
                        {
                            name: "Storno6",
                            posX: 5,
                            posY: 10,
                            width: 5,
                            height: 14
                        }
                    ],
                ];
            }
        };
    }];
});