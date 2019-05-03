//# sourceURL=pos.areaConfigDirective.js
define([
    'angular',
    'css!pos.areaConfigDirectiveCss'
], function (angular) {
    return ['$rootScope', 'pos.areaService', '$timeout', 'pos.initOptions', function (rootScope, areaService, timeout, initOptions) {
        return {
            restrict: 'E',
            templateUrl: initOptions.baseDir + 'components/area/areaConfigDirective.html',
            replace: true,
            scope: {
                'areas': '=areaConfig',
                'areaConfigName': '=areaConfigName',
                'areaConfigIndex': '=areaConfigIndex'
            },
            link: function (scope, el, attrs) {
                rootScope.$on(areaService.serviceName + "currentConfig", function (event, configName) {
                    if (scope.areaConfigName == configName) {
                        el.css("z-index", "1");
                    } else {
                        el.css("z-index", "-1");
                    }
                });
                if ((scope.areaConfigIndex == 0)) {
                    el.css("z-index", "1");
                } else {
                    el.css("z-index", "-1");
                }
            }
        }
    }];
});