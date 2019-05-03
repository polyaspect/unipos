//# sourceURL=pos.design.customValidator.js
define([
    'angular'
], function (angular) {
    return ['pos.designer.screenService', function (screenService) {
        return {
            require: 'ngModel',
            link: function (scope, element, attrs, ngModel) {
                var options = attrs["pos.designer.customerValidator"].split("|");
                console.log(attrs);
                function panelIdExists(value) {
                    ngModel.$setValidity('panelIdExists', !screenService.panelIdExists(value, attrs["panelIdExists"]));
                }

                ngModel.$parsers.push(function (value) {
                    angular.forEach(options, function (item, index) {
                        if (item == "panelIdExists") {
                            panelIdExists(value);
                        }
                    });
                    return value;
                });
            }
        }
    }];
});