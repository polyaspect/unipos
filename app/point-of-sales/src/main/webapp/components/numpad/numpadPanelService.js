//# sourceURL=pos.numpadPanelService.js
define([
    'angular'
], function (angular) {
    return ['pos.quantityService', function (quantityService) {
        this.serviceName = "pos.numpadPanelService";
        var numpad = [['7'], ['8'], ['9'], ['4'], ['5'], ['6'], ['1'], ['2'], ['3'], ['0'], ['00'], ['-'], ['AC'], ['C']];
        var tasten = [
            ['`', '~'], ['1', '!'], ['2', '@'], ['3', '#'], ['4', '$'], ['5', '%'], ['6', '^'], ['7', '&'], ['8', '*'], ['9', '('], ['0', ')'], ['-', '_'], ['=', '+'], ['Bksp', 'Bksp'],
            ['Caps', 'Caps'], ['q', 'Q'], ['w', 'W'], ['e', 'E'], ['r', 'R'], ['t', 'T'], ['y', 'Y'], ['u', 'U'], ['i', 'I'], ['o', 'O'], ['p', 'P'], ['[', '{'], [']', '}'], ['\\', '|'],
            ['Shift', 'Shift'], ['a', 'A'], ['s', 'S'], ['d', 'D'], ['f', 'F'], ['g', 'G'], ['h', 'H'], ['j', 'J'], ['k', 'K'], ['l', 'L'], [';', ':'], ['\'', '"'], ['Enter', 'Enter'], [],
            [], ['z', 'Z'], ['x', 'X'], ['c', 'C'], ['v', 'V'], ['b', 'B'], ['n', 'N'], ['m', 'M'], [',', '<'], ['.', '>'], ['/', '?'], [' ', ' '], [], []
        ];
        this.options = {
            shift: false,
            caps: false
        };
        var self = this;
        this.getKeyboardData = function (cell) {
            var result = [];
            angular.forEach(tasten, function (value, index) {
                var item = {};
                item.serviceName = "pos.numpadPanelService";
                item.serviceAction = "doAction";
                item.serviceData = {
                    modes: cell.service.modes,
                    values: value,
                    keyboard: true,
                    directiveId: cell.id
                };
                if (cell.service.enterButton && value[0] == "Enter") {
                    item.name = "Enter";
                    item.serviceName = "pos.enterService";
                    item.serviceAction = "doAction";
                } else if (value[0] == "Enter") {
                } else if (angular.isArray(value)) {
                    if (value.length == 0) {
                        item = undefined;
                    } else {
                        if (self.options.shift || self.options.caps) {
                            item.name = value[1];
                        } else {
                            item.name = value[0];
                        }
                    }
                } else {
                    item.name = value;
                }
                result.push(item);
            });
            return result;
        };
        this.getNumpadData = function (cell) {
            var result = [];
            angular.forEach(numpad, function (value, index) {
                var item = {};
                if (angular.isArray(value)) {
                    if (value.length == 0) {
                        item = undefined;
                    } else {
                        item.name = value[0];
                    }
                } else {
                    item.name = value;
                }
                item.serviceName = "pos.numpadPanelService";
                item.serviceAction = "doAction";
                item.serviceData = {
                    modes: cell.service.modes,
                    values: name,
                    keyboard: false
                };
                result.push(item);
            });
            if (cell.service.enterButton) {
                var item = {};
                item.name = "Enter";
                item.serviceName = "pos.enterService";
                item.serviceAction = "doAction";
                item.serviceData = {};
                result.push(item);
            } else {
                result.push(undefined);
            }
            return result;
        };
        this.doAction = function (cell) {
            quantityService.doAction(cell)
        };
    }];
});