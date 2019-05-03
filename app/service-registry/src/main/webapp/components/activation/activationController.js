//# sourceURL=activationController.js
define([
    'angular',
    'css!core.activationController',
    'core.sweetAlert',
    'css!core.sweetAlertStyle'
], function () {
    return ["$scope", "$location", "$rootScope", "activationService", function (scope, location, rootScope, activationService) {
        var reason = window.location.href.replace(/%2F/g, "/").split("reason=")[1]
        if (reason == "noLicenseFile"){
            swal("Software nicht aktiviert", "Es wurde keine gültige Lizenz für dieses Produkt gefunden! Bevor Sie fortfahren können, müssen Sie die Aktivierung abschließen.", "error");
        }
        if (reason == "noValidLicenseFileForUser"){
            swal("Unternehmen nicht aktiviert", "Es wurde keine gültige Lizenz für das Unternehmen des aktuell angemeldeten Benutzers gefunden! Bevor Sie fortfahren können, müssen Sie die Aktivierung abschließen.", "error");
        }
        rootScope.showNav = false;
        var $this = this;
        scope.checkActivation = function () {
            if (scope.productKey && scope.productKey != "") {
                activationService.activate(scope.productKey).$promise.then(function (response) {
                    location.path("/license/activateSuccess");
                }, function (error) {
                    swal("Ungültiger Produktschlüssel", "Bitte geben Sie einen gültigen Produktschlüssel ein", "error");
                });
            }
        };
    }];
});