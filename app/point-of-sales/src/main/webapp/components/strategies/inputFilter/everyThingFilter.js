//# sourceURL=pos.inputFilter.everyThingFilter.js
define([
    'angular'
], function (angular) {
    return {
        validate: function (text, char) {
            return text + char;
        },
        removeChar: function (text) {
            if(text.length == 0)
                return "";
            if (text != undefined && text != "") {
                return text.substr(0, text.length - 1);
            }
        }
    };
});
