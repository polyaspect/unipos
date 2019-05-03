//# sourceURL=report.reportPreviewModal.js
define([
    'angular',
    'moment',
    'css!report.reportPreviewModal'
], function (angular, moment) {
    return ["$scope", 'companyService', 'initOptions', '$http', '$uibModal', '$sce', '$uibModalInstance', 'model',
        function (scope, companyService, initOptions, http, uibModal, sce, uibModalInstance, model) {
            scope.model = model;
            scope.initOptions = initOptions;

            var url = "";
            url += scope.model.report.exports.html.url;
            url += "?startDate=" + moment(scope.model.startDate).format("YYYY-MM-DDTHH:mm:ss.SSSz");
            url += "&endDate=" + moment(scope.model.endDate).format("YYYY-MM-DDTHH:mm:ss.SSSz");
            url += "&storeGuid=" + scope.model.store;

            http.get(url).then(function (response) {
                scope.responseText = sce.trustAsHtml(response.data);
            });

            scope.close = function () {
                uibModalInstance.close();
            };
        }];
});
