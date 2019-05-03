//# sourceURL=pos.deviceService.js
define([
    'angular'
], function (angular) {
    return [function () {
        var self = this;
        this.approxValues = {
            P: {
                S: 500,
                M: 800,
                L: 1200
            },
            L: {
                S: 500,
                M: 800,
                L: 1200,
                XL: 1800
            }
        };
        this.getApproxSize = function () {
            let height = angular.element(document).height();
            let width = angular.element(document).width();
            let result = {
                orientation: width > height ? 'LANDSCAPE' : 'PORTRAIT',
                data: []
            };
            Enumerable.From(width > height ? self.approxValues.L : self.approxValues.P).ForEach(function (x) {
                var tmp = {
                    size: x.Key,
                    diff: Math.abs(width > height ? width - x.Value : height - x.Value)
                };
                result.data.push(tmp);
            });
            result.data = Enumerable.From(result.data).OrderBy(function (x) {
                return x.diff;
            }).ToArray();
            return result;
        };
        this.getDevice = function () {
            return "widescreen";
        };
    }
    ]
        ;
})
;