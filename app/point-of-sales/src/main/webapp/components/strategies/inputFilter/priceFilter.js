//# sourceURL=pos.inputFilter.priceFilter.js
define([
    'angular'
], function (angular) {
    function replaceAt(text, index, character) {
        return text.substr(0, index) + character + text.substr(index + character.length);
    }

    return {
        validate: function (text, char) {
            if (!char) {
                return text;
            }
            if (text.indexOf("Rest") > -1 || text.indexOf("Gesamt") > -1 || text.indexOf("Rückgeld") > -1) {

            }
            var value = text.replace(',', '.');
            value = (value * 10).toFixed(2);
            value = replaceAt(value, value.length - 1, char).replace('.', ',');
            if (value.match(/^[0-9]*[,]?[0-9]*$/) == null) {
                return "0,0" + char;
            }
            if (value.match(/^([1-9][0-9]*[,]?[0-9]{0,2}|0,\d{0,2})/) != null) {
                return value.match(/^([1-9][0-9]*[,]?[0-9]{0,2}|0,\d{0,2})/)[0];
            } else {
                return text;
            }
        },
        removeChar: function (text) {
            if (text != undefined) {
                text = text.replace(',', '.');
                text = Math.floor(text * 10);
                text = (text / 100).toFixed(2);

                return text;
            }
            else return "";
        }
    };
});
