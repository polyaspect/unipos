//# sourceURL=signatureOptionsEditorController.js
define([
    'signature.sweetAlert',
    'angular',
    'css!signature.signatureOptionsEditorController'
], function (swal) {
    return ["$scope", 'signatureOptionsService', 'initOptions', '$uibModal', '$uibModalInstance', 'model', 'create',
        function (scope, signatureOptionsService, initOptions, uibModal, uibModalInstance, model, create) {
            scope.model = model;
            scope.initOptions = initOptions;
            scope.create = create;

            scope.model.stores = signatureOptionsService.findAllStores();

            if(scope.model.secretKeyPlainText == null){
                var key = signatureOptionsService.randomString(8);
                scope.model.secretKeyPlainText = key;
                scope.model.secretKeyPlainTextRepeat = key;
            }else{
                scope.model.secretKeyPlainTextRepeat = scope.model.secretKeyPlainText;
            }

            if(scope.model.id != null){
                signatureOptionsService.findEncryptionDetailsById(scope.model.id).$promise.then(function(data){
                    scope.model.encodedPassword = data.encodedPassword;
                    scope.model.verificationValue = data.verificationValue;
                });
            }else{
                scope.model.encodedPassword = "Zuerst speichern";
                scope.model.verificationValue = "Zuerst speichern";
            }

            scope.rkas = [{
                    id:'R1-AT1',
                    text:'R1-AT1 : A-Trust'
                },
                {
                    id:'R1-AT2',
                    text:'R1-AT2 : Global-Trust'
                }
            ];

            scope.save = function () {
                if(model.secretKeyPlainText != model.secretKeyPlainTextRepeat){
                    swal({
                        title: "Passwörter stimmen nicht überein",
                        text: "Überprüfen Sie, ob das Verschlüsselung-Passwort zweimal korrekt eingegeben wurde.",
                        type: "error",
                        showConfirmButton: true
                    });
                    return;
                }
                uibModalInstance.close(true);
            };
            scope.delete = function () {
                if (signatureOptionsService.checkIfResourceInstance(scope.model)) {
                    var promise = signatureOptionsService.deleteByStoreGuid(scope.model.storeGuid);
                    promise.$promise.then(function () {
                        uibModalInstance.close(false);
                        swal({
                            title: "Gelöscht",
                            text: "Die Signatur-Option wurde erfolgreich gelöscht!",
                            type: "success",
                            timer: 2000,
                            showConfirmButton: false
                        });
                    });
                }
            };
        }];
});
