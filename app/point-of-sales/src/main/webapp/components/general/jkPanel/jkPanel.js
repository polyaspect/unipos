//# sourceURL=pos.jkPanel.js
define([
    'angular',
    'css!pos.jkPanelCss'
], function (angular) {
    return ['pos.initOptions', function (initOptions) {
        function calculatePercentageFromPosLayoutNumber(posLayoutNumber) {
            return (posLayoutNumber / 24) * 100;
        }

        return {
            restrict: 'E',
            templateUrl: initOptions.baseDir + 'components/general/jkPanel/jkPanel.html',
            replace: true,
            link: function (scope, el, attrs) {
                scope.jkPanelData = [];
                scope.shownSlide = 0;
                angular.forEach(scope.asdf, function (value, outerIndex) {
                    var tempArray = [];
                    angular.forEach(value, function (item, innerIndex) {
                        item.css = {
                            width: calculatePercentageFromPosLayoutNumber(item.width) + "%",
                            height: calculatePercentageFromPosLayoutNumber(item.height) + "%",
                            top: calculatePercentageFromPosLayoutNumber(item.posY) + "%",
                            left: calculatePercentageFromPosLayoutNumber(item.posX) + "%"
                        };
                        tempArray.push(item)
                    });
                    scope.jkPanelData.push(tempArray);
                });
                el.find(".prev").click(function () {
                    if (scope.shownSlide > 0) {
                        scope.shownSlide--;
                    }
                });
                el.find(".next").click(function () {
                    if (scope.shownSlide < scope.jkPanelData.length - 1) {
                        scope.shownSlide++;
                    }
                });
            }
        };
    }];
});