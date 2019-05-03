//# sourceURL=pos.cashbookGridService.js
define([
    'angular',
    'pos.moment'
], function (angular, moment) {
    return ['pos.dataService', 'pos.cashbookEntryService', '$filter', function (dataService, cashbookService, filter) {
        this.getColumnDefs = function () {
            return [{
                name: 'Datum',
                field: 'date',
                width: "10%",
            }, {
                name: 'Uhrzeit',
                field: 'time',
                width: "10%",
            }, {
                name: 'typ',
                field: 'typ',
                width: "10%",
            }, {
                name: 'grund',
                field: 'reason',
                width: "50%",
            }, {
                name: 'betrag',
                field: 'amount',
                width: "20%",
            }];
        };
        this.getData = function (areaData) {
            var displayData = [];
            var index = 1;
            var data = {};
            if (areaData.data.service.options.modeAll) {
                data = angular.copy(cashbookService.cashbookEntries);
            } else {
                data = Enumerable.From(angular.copy(cashbookService.cashbookEntries)).Where(function (x) {
                    return moment(x.creationDate).isAfter(cashbookService.lastDailySettlement.dateTime);
                }).ToArray();
            }
            angular.forEach(data, function (item, index) {
                if (!item.css) {
                    item.css = {};
                }
                if (item.type == "IN") {
                    item.css.evenRows = (areaData.data && areaData.data.css && areaData.data.css.evenRows) || {};
                    item.css.oddRows = (areaData.data && areaData.data.css && areaData.data.css.evenRows) || {};
                } else {
                    item.css.evenRows = (areaData.data && areaData.data.css && areaData.data.css.oddRows) || {};
                    item.css.oddRows = (areaData.data && areaData.data.css && areaData.data.css.oddRows) || {};
                }
                item.css.selectedRow = (areaData.data && areaData.data.css && areaData.data.css.selectedRow) || {};
                item.css.cell = (areaData.data && areaData.data.css && areaData.data.css.cell) || {};

                item.index = index;
                index++;
                displayData.push({
                    "date": moment(item.creationDate).format("DD-MM-YYYY"),
                    "time": moment(item.creationDate).format("HH:mm:ss"),
                    "typ": item.type,
                    "reason": item.description,
                    "amount": filter('number')(item.amount, 2) + " €",
                    "value": item
                });
            });
            return displayData.reverse();
        };
    }];
});