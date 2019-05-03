//# sourceURL=updateController.js

define([
    'angular',
    'core.sweetAlert',
    'css!core.sweetAlertStyle'
], function (angular) {
    return ["$resource", "$scope", "updateService", function ($resource, $scope, updateService) {
        var $this = this;

        $this.showCheckUpdateDialog = function() {
            updateService.findUpdatableModules().$promise.then(function (data) {
                    if (data.length > 0) {
                        swal({
                                title: "Update verfügbar!!!",
                                text: "Folgende Module können aktualisiert werden: \n\n" + data.toString().replace(/,/g,"\n"),
                                type: "info",
                                showCancelButton: true,
                                cancelButtonText: "Abbrechen",
                                confirmButtonColor: "#DD6B55",
                                confirmButtonText: "Aktualisieren!",
                                showLoaderOnConfirm: true,
                                closeOnConfirm: false
                            },
                            function () {
                                updateService.processUpdate().$promise.then(function (updatedModules) {
                                        swal({
                                            title: "Update abgeschlossen",
                                            text: "Folgenden Module wurden aktualisiert: \n\n" + updatedModules.toString().replace(/,/g,"\n"),
                                            type: "success"
                                        });
                                    },
                                    function () {
                                        swal({
                                            title: "Update nicht erfolgreich durchgeführt",
                                            text: "In den Logs können Sie weitere Informationen beziehen",
                                            type: "error"
                                        })
                                    });
                            });
                    } else {
                        swal({
                            title: "Keine Updates zurzeit verfügbar.",
                            type: "info"
                        });
                    }
                },
                function () {
                    swal({
                        title: "Update-Status konnte nicht eingesehen werden",
                        text: "In den Logs können Sie weitere Informationen zu diesem Fehler einsehen",
                        type: "error"
                    });
                });
        };

        $scope.showCheckUpdateDialog = $this.showCheckUpdateDialog;

        $this.showCheckUpdateDialog();
    }]
});