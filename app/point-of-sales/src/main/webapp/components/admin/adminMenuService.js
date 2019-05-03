//# sourceURL=pos.logoutService.js
define([
    'angular'
], function (angular) {
    return ['pos.initOptions', 'pos.ui.elements.text.textDirectiveService', '$cookies', '$location', 'pos.loginService','pos.socketService', function (initOptions, textService, cookies, location, loginService,socketService) {
        var self = this;
        this.doAction = function (cell) {
            swal({
                title: "Ins Admin-Menü wechseln?",
                text: "Dadurch verlassen Sie die Kassenoberfläche und wechseln ins Admin-Menü!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#388E3C",
                confirmButtonText: "Wechseln",
                cancelButtonText: "Abbrechen",
                closeOnConfirm: false,
                closeOnCancel: true
            }, function (isConfirm) {
                if (isConfirm) {
                    window.location = "/report/";
                } else {

                }
            });
        }
    }];
});