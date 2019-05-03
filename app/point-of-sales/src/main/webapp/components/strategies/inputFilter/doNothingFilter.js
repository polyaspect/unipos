//# sourceURL=pos.inputFilter.doNothingFilter.js
define([
    'angular'
], function (angular) {
    return {
        validate: function (text, char) {
        },
        removeChar: function (text) {
            if (text != undefined && text != "") {
                return text.substr(0, text.length - 1);
            }
        }
    };
});
