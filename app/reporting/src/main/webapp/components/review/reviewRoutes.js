//# sourceURL=reviewRoutes.js

define([
        'angular',
        'report.angularRoute'
    ], function () {
        return function (baseUrl, baseDir) {
            return ['$routeProvider', function ($routeProvider) {
                $routeProvider.when(baseUrl + '/journalReview', {
                    templateUrl: baseDir + 'components/review/journalReviewView.html',
                    controller: 'report.reviewController'
                }).when(baseUrl + '/reportPreview', {
                    templateUrl: baseDir + 'components/reportPreview/reportPreview.html',
                    controller: 'report.reportPreviewController'
                }).when(baseUrl + '/journalRepair', {
                    templateUrl: baseDir + 'components/journalRepair/journalRepairView.html',
                    controller: 'report.journalRepairController'
                })
            }];
        }
    }
);

