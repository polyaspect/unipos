//# sourceURL=pos.inputFilter.percentFilter.js
define([
    'angular'
], function (angular) {
    return {
        validate: function (text, char) {
            if (!char) {
                return text;
            }
            text = text.replace('%', '').trim();
            if ((text).match(/\D/) == null) {
                if ((text + char).match(/^\d{0,2}$/) != null) {
                    return (text + char) + " %";
                } else if (parseInt(text + char) > 100) {
                    return text + ' %';
                } else {
                    return char + ' %';
                }
            }
            else {
                return char + ' %';
            }
        },
        removeChar: function (text) {
            if (text != undefined && text != "") {
                text = text.replace('%', '').trim();
                if (text != "") {
                    if (text.substr(0, text.length - 1) == "") {
                        return ";"
                    } else {
                        return text.substr(0, text.length - 1);
                    }

                }
            }
        }
    };
});
