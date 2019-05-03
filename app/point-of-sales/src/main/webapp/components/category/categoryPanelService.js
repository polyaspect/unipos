//# sourceURL=pos.categoryPanelService.js
define([
    'angular'
], function (angular) {
    return ['pos.categoryService', function (categoryService) {
        this.getCategories = function (cell) {
            var result = Enumerable.From(angular.copy(categoryService.getCategories())).Where(function (x) {
                return Enumerable.From(cell.service.categoryIds).Any(function (y) {
                    return y == x.guid;
                });
            }).Select(function (x) {
                return {
                    directiveId: cell.id,
                    name: x.name,
                    serviceName: "pos.categoryService",
                    serviceAction: "doAction",
                    serviceData: {
                        "category": x
                    },
                    info: {
                    }
                };
            }).ToArray();
            return result;
        };
        this.getAllCategories = function (cell) {
            var result = [];
            angular.forEach(angular.copy(categoryService.getCategories()), function (item, index) {
                result.push({
                    directiveId: cell.id,
                    name: item.name,
                    serviceName: "pos.categoryService",
                    serviceAction: "doAction",
                    serviceData: {
                        "category": item
                    },
                    info: {
                    }
                });
            });
            return result;
        };
    }];
});