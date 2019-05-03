//# sourceURL=pos.ui.elements.panel.panelDirective.js
define([
    'angular',
    'css!pos.ui.elements.panel.panelDirective'
], function (angular) {
    return ['pos.initOptions', 'pos.ui.elements.panel.panelDirectiveService', '$timeout', '$rootScope', 'pos.productService', function (initOptions, panelDirectiveService, timeout, rootScope, productService) {
        function renderSlider(scope, el) {
            var size = scope.area.data.columns * scope.area.data.rows;
            var panelData = panelDirectiveService.getData(scope.area.data);
            var totalSlides = Math.ceil(panelData.length / size);
            var erg = [];
            var indexOfUsed = 0;
            for (var i = 0; i < totalSlides; i++) {
                var slide = [];
                for (var j = 0; j < scope.area.data.rows; j++) {
                    var row = [];
                    for (var k = 0; k < scope.area.data.columns; k++) {
                        if (panelData[indexOfUsed]) {
                            panelData[indexOfUsed].css = scope.area.data.css;
                        }
                        row.push(panelData[indexOfUsed]);
                        indexOfUsed++;
                    }
                    if (row.length == 0) {
                        for (var k = 0; k < scope.area.data.columns; k++) {
                            row.push(undefined);
                        }
                    }
                    slide.push(row);
                }
                erg.push(slide);
            }
            scope.jkSliderData = panelDirectiveService.getPanelOptions();
            scope.jkSliderData.result = erg;
            scope.showPrevAndNextButton = panelData.length > size;
            angular.element(window).trigger("resize");
            return panelDirectiveService.getDataSettings(scope.area.data);
        }

        return {
            restrict: 'E',
            templateUrl: initOptions.baseDir + 'components/ui/elements/panelDirective/panelDirective.html',
            replace: true,
            scope: {
                "area": "="
            },
            link: function (scope, el, attrs) {
                var settings = renderSlider(scope, el);
                rootScope.$on((settings && settings.eventName) || (scope.area.data.service.name + "-" + scope.area.data.id), function () {
                    renderSlider(scope, el);
                });
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