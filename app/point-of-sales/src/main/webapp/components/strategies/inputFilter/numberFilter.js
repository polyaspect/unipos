//# sourceURL=pos.inputFilter.numberFilter.js
define([
    'angular'
], function (angular) {
    return {
        validate: function (text, char) {
            if (!char) {
                return text;
            }

            if(char == '-'){
                if(text.length >= 1 && text[0] == "-")
                    return text.substr(1, text.length - 1);
                else
                    return "-" + text;
            }

            if ((text + char).match(/^[\-]?[1-9][0-9]*[,]?[0-9]{0,4}/) != null) {
                return (text + char).match(/^[\-]?[1-9][0-9]*[,]?[0-9]{0,4}/)[0];
            } else if (char == "," && text == "") {
                return "0" + char;
            } else {
                return text;
            }
        },
        removeChar: function (text) {
            if (text != undefined && text != "") {
                return text.substr(0, text.length - 1);
            }
        }
    };
});
