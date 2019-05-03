//# sourceURL=pos.designer.screenCollectionService.js
define([
    'angular'
], function (angular) {
    return ['$http', function (http) {
        this.current = undefined;
        let self = this;
        this.new = function (name) {
            self.current = {
                name: name,
                screens: [],
                settings: {
                    stores: []
                },
                published: false,
                standart: false,
            };
            return self.current;
        };
        this.setCurrent = function (screenCollection) {
            self.current = screenCollection;
        };
    }];
});