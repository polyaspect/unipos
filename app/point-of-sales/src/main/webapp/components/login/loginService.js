//# sourceURL=pos.loginService.js
define([
    'angular'
], function (angular) {
    return ['pos.initOptions', 'pos.ui.elements.text.textDirectiveService', '$http', '$location', '$cookies', '$rootScope', '$q', 'pos.addNewDeviceService',
        function (initOptions, textService, http, location, cookies, rootScope, q, addNewDeviceService) {
            var self = this;
            var user = {
                username: "",
                password: ""
            };
            this.doAction = function (cell) {
                if (user.username == undefined || user.username == "") {
                    user.username = textService.getText(textService.textModes.numpad);
                    if (user.username == undefined) {
                        swal("Keine Benutzer-Nummer eingegeben", "Bitte geben Sie die Nummer des Benutzer ein, mit welchem Sie sich anmelden wollen!", "info");
                        return;
                    }
                    textService.setText(textService.textModes.numpad, "");
                    textService.setText(textService.textModes.info, "PIN eingeben:");
                } else if (user.password == undefined || user.password == "") {
                    user.password = textService.getText(textService.textModes.numpad);
                    if (user.password == undefined) {
                        swal("Kein PIN eingegeben", "Bitte geben Sie den PIN des Benutzer ein, mit welchem Sie sich anmelden wollen!", "info");
                        return;
                    }
                    textService.setText(textService.textModes.numpad, "");
                    self.login(user.username, user.password).then(function (data) {
                        self.setInitValues().then(function () {
                            location.path(initOptions.baseUrl + "pos/");
                        });
                    }, function (data) {
                        if (data.status = 401) {
                            swal("Falsche Anmeldeinformation", "Die eingegebene Kombination aus Benutzer-Nummer und PIN konnte keinem Benutzer zugeordnet werden. Bitte versuchen Sie es erneut.", "warning");
                        }
                        textService.setText(textService.textModes.info, "Auth Failed");
                        user.username = "";
                        user.password = "";
                    });
                }
            };
            this.login = function (username, pin) {
                return http({
                    method: 'POST',
                    url: '/auth/auth/pin_login',
                    data: "mitarbeiternr=" + username + "&pin=" + pin,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                });
            };
            this.clearLoginData = function () {
                user.username = undefined;
                user.password = undefined;
                if (rootScope.pos && rootScope.pos.user) {
                    rootScope.pos.user = {};
                }
            };
            this.setInitValues = function () {
                var deferred = q.defer();
                if (!rootScope.pos) {
                    rootScope.pos = {};
                    if (!rootScope.pos.user) {
                        rootScope.pos.user = {};
                    }
                }
                http.get('/pos/orders/getDefaultCashierId?usage=Pos').then(function (response) {
                    rootScope.pos.user.id = response.data.guid;
                    deferred.resolve();
                });
                return deferred.promise;
            };
        }];
});