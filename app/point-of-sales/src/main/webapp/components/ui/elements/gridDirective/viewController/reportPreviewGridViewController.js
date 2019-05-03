//# sourceURL=pos.reportPreviewGridService.js
define([
    'angular'
], function (angular) {
    return ['pos.dataService', '$http', function (dataService, http) {
        // INTERFACE REQUIREMENTS:

        var self = this;
        this.class = "pos.reportPreviewGridViewController";

        this.getViewData = function (dataSource, metaData) {
            var width = Math.floor((angular.element("." + metaData.uuid).width() - 40) / getTextWidth("a", "13pt monospace"));
            var gridData = dataSource;
            var displayData = [];
            var index = 1;
            angular.forEach(gridData, function (item, index) {
                if (!item.css) {
                    item.css = {};
                }
                item.css.evenRows = (metaData.data && metaData.data.css && metaData.data.css.evenRows) || {};
                item.css.oddRows = (metaData.data && metaData.data.css && metaData.data.css.oddRows) || {};
                item.css.selectedRow = (metaData.data && metaData.data.css && metaData.data.css.selectedRow) || {};
                item.css.cell = (metaData.data && metaData.data.css && metaData.data.css.cell) || {};
                var anyWidth = Enumerable.From(item.formats).Any(function (x) {
                    return x == "DOUBLE_WIDTH";
                });
                var anyHeight = Enumerable.From(item.formats).Any(function (x) {
                    return x == "DOUBLE_HEIGHT";
                });
                if (anyWidth && anyHeight) {
                    self.setStyle(item.css.evenRows, self.doubleBothStyle);
                    self.setStyle(item.css.oddRows, self.doubleBothStyle);
                    self.setStyle(item.css.selectedRow, self.doubleBothStyle);
                } else if (anyWidth) {
                    self.setStyle(item.css.evenRows, self.doubleWidthStyle);y
                    self.setStyle(item.css.oddRows, self.doubleWidthStyle);
                    self.setStyle(item.css.selectedRow, self.doubleWidthStyle);
                } else if (anyHeight) {
                    self.setStyle(item.css.evenRows, self.doubleHeightStyle);
                    self.setStyle(item.css.oddRows, self.doubleHeightStyle);
                    self.setStyle(item.css.selectedRow, self.doubleHeightStyle);
                }
                item.index = index;
                index++;
                displayData.push({
                    "text": self.addWhiteSpacesToLine(item.line, width),
                    "value": item
                });
            });
            return displayData;
        };

        // CUSTOM::

        this.doubleWidthStyle = {
            "transform": "scale(2.0, 1.0)"
        };
        this.doubleHeightStyle = {
            "transform": "scaleY(1.5)"
        };
        this.doubleBothStyle = {
            "transform": "scale(1.5, 1.5)"
        };
        dataService.setByServiceKey(self.serviceName, {});


        // DEPRECATED
        this.setDirectiveData = function (directiveId, data) {
            alert("this.setDirectiveData is deprecated and not implemented in this viewController");
        };
        this.getDirectiveData = function (directiveId) {
            alert("this.getDirectiveData is deprecated and not implemented in this viewController");
        };
        // END DEPRECATED


        function getTextWidth(text, font) {
            var canvas = getTextWidth.canvas || (getTextWidth.canvas = angular.element("<canvas></canvas>")[0]);
            var context = canvas.getContext("2d");
            context.font = font;
            var metrics = context.measureText(text);
            angular.element("canvas").remove();
            return metrics.width;
        }

        this.getColumnDefs = function () {
            return [{
                name: 'Text',
                field: 'text',
                width: "100%",
            }];
        };
        this.setStyle = function (cssData, style) {
            angular.forEach(style, function (value, key) {
                cssData[key] = value;
            });
        };
        this.addWhiteSpacesToLine = function (line, width) {
            width = width - line.length;
            while (width > 0) {
                line = " " + line + " ";
                width -= 2;
            }
            return line;
        };


    }];
});