//# sourceURL=pos.textDirective.js
define([
    'angular',
    'css!pos.ui.elements.text.textDirective'
], function (angular) {
    return ["pos.ui.elements.text.textDirectiveService", "$rootScope", function (textService, rootScope) {
        return {
            template: "<div class='info-text-directive' ng-style='area.data.css.text'>{{text}}</div>",
            replace: true,
            scope: {
                area: "="
            },
            link: function (scope, el, attrs) {
                scope.text = scope.area.data.text;
                textService.setDirectiveData(scope.area.data.id, scope.area.data);
                if (scope.area.data.staticText) {
                    scope.text = scope.area.data.text;
                } else {
                    rootScope.$on(textService.serviceName + "-" + textService.modesKey, function (event, eventMode) {
                        var data = textService.getDirectiveData(scope.area.data.id);
                        angular.forEach(data.modes, function (mode, index) {
                            if (mode.name == eventMode) {
                                scope.text = textService.getViewData(eventMode);
                            }
                        });
                    });
                }
            }
        }
    }];
});