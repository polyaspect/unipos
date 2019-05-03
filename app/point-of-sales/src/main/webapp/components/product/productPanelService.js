//# sourceURL=pos.productPanelService.js
define([
    'angular'
], function (angular) {
    return ['pos.productService', 'pos.categoryService', function (productService, categoryService) {
        this.getProductsSettings = function () {
            return {
                eventName: productService.serviceName + '-products'
            };
        };
        this.getProducts = function (cell) {
            var result = Enumerable.From(angular.copy(productService.getAllProducts())).Where(function (x) {
                return Enumerable.From(cell.service.productsIds).Any(function (y) {
                    return y == x.guid;
                });
            }).Select(function (x) {
                return {
                    name: x.name,
                    serviceName: "pos.productService",
                    serviceAction: "doAction",
                    serviceData: {
                        "id": x.id,
                        "categoryId": x.category.id,
                        "sortOrder": x.sortOrder
                    },
                    info: {
                        bottom: {
                            right: x.stockAmount > 0 ? x.stockAmount : ''
                        }
                    }
                };
            }).OrderBy(function (x) {
                return x.serviceData.sortOrder;
            }).ToArray();
            return result;
        };
        this.getAllProductsSettings = function () {
            return {
                eventName: productService.serviceName + '-products'
            };
        };
        this.getAllProducts = function (cell) {
            var result = [];
            angular.forEach(angular.copy(productService.getAllProducts()), function (item, index) {
                result.push({
                    name: item.name,
                    serviceName: "pos.productService",
                    serviceAction: "doAction",
                    serviceData: {
                        "id": item.id,
                        "categoryId": item.category.id,
                        "sortOrder": item.sortOrder
                    },
                    info: {
                        bottom: {
                            right: item.stockAmount > 0 ? item.stockAmount : ''
                        }
                    }
                });
            });
            return Enumerable.From(result).OrderBy(function (x) {
                return x.serviceData.sortOrder;
            }).ToArray();
        };
        this.getProductsPerCategorySettings = function () {
            return {
                eventName: categoryService.serviceName + '-currentCategory'
            };
        };
        this.getProductsPerCategory = function (cell) {
            var categoryGuid;
            if (categoryService.currentCategory[cell.categoryDirectiveId]) {
                categoryGuid = categoryService.currentCategory[cell.categoryDirectiveId].category.guid;
            } else {
                categoryGuid = categoryService.getCategories()[0].guid;
            }
            return angular.copy(Enumerable.From(productService.getAllProducts()).Where(function (x) {
                return x.category.guid == categoryGuid;
            })).Select(function (x) {
                return {
                    name: x.name,
                    serviceName: "pos.productService",
                    serviceAction: "doAction",
                    serviceData: {
                        "id": x.id,
                        "categoryId": x.category.id,
                        "sortOrder": x.sortOrder
                    },
                    info: {
                        bottom: {
                            right: x.stockAmount > 0 ? x.stockAmount : ''
                        }
                    }
                }
            }).OrderBy(function (x) {
                return x.serviceData.sortOrder;
            }).ToArray();
        };
        this.getProductsBySingleCategorySettings = function (cell) {
            return {};
        };
        this.getProductsBySingleCategory = function (cell) {
            return angular.copy(Enumerable.From(productService.getAllProducts()).Where(function (x) {
                return x.category.guid == cell.categoryId;
            })).Select(function (x) {
                return {
                    name: x.name,
                    serviceName: "pos.productService",
                    serviceAction: "doAction",
                    serviceData: {
                        "id": x.id,
                        "categoryId": x.category.id,
                        "sortOrder": x.sortOrder
                    },
                    info: {
                        bottom: {
                            right: x.stockAmount > 0 ? x.stockAmount : ''
                        }
                    }
                }
            }).OrderBy(function (x) {
                return x.serviceData.sortOrder;
            }).ToArray();
        };
    }];
});