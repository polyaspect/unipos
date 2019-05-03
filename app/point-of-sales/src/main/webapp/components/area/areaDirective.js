//# sourceURL=pos.areaDirective.js
define([
    'angular',
    'css!pos.areaDirectiveCss'
], function (angular) {
    return ['$compile', 'pos.initOptions', function ($compile, initOptions) {
        function calculatePercentageFromPosLayoutNumber(posLayoutNumber) {
            return (posLayoutNumber);
        }

        return {
            restrict: 'E',
            templateUrl: initOptions.baseDir + 'components/area/areaDirective.html',
            replace: true,
            link: function (scope, el, attrs) {
                var area = scope.area;
                el.css("position", "absolute");
                el.css("left", calculatePercentageFromPosLayoutNumber(area.posX) + "%");
                el.css("top", calculatePercentageFromPosLayoutNumber(area.posY) + "%");
                el.css("width", calculatePercentageFromPosLayoutNumber(area.width) + "%");
                el.css("height", calculatePercentageFromPosLayoutNumber(area.height) + "%");
                var newDirective = $compile("<" + area.name + " area='area'></" + area.name + ">")(scope);
                el.find(".area-inner").append(newDirective);
            }
        }
    }];
});