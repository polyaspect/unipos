'use strict'
//# sourceURL=infoTextService.js
define([
    'angular',
], function (angular) {
    angular.module('ngTextFill', [])
        .directive('textfill', function ($timeout) {
            return {
                restrict: 'A',
                scope: {
                    textfill: '@',
                    textfillOnSuccess: '=',
                    textfillOnFail: '=',
                    textfillOnComplete: '='
                },
                template: '<span>{{textfill}}</span>',
                link: function (scope, element, attr) {
                    var container = element,
                        options = {
                            innerTag: attr.innerTag || "span",
                            debug: attr.debug || false,
                            minFontPixels: parseInt(attr.minFontPixels) || 4,
                            maxFontPixels: parseInt(attr.maxFontPixels) || 40,
                            widthOnly: attr.widthOnly || false,
                            explicitHeight: attr.explicitHeight || null,
                            explicitWidth: attr.explicitWidth || null,
                            success: scope.textfillOnSuccess || null,
                            fail: scope.textfillOnFail || null,
                            complete: scope.textfillOnComplete || null
                        };

                    container.textfill(options);

                    scope.$watch('textfill', function () {
                        container.textfill(options);
                    });
                }
            };
        });
});
