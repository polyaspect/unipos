//# sourceURL=reportPreview.js
define([
    'angular',
    'moment',
    'css!report.reportPreviewController'
], function (angular, moment) {
    return ["$scope", 'report.reportPreviewService', '$http', '$uibModal', 'initOptions',
        function (scope, reportPreviewService, http, uibModal, initOptions) {
            scope.stores = reportPreviewService.findAll();
            scope.reports = [
                {
                    "name": "Produktbericht",
                    "exports": {
                        html: {
                            "name": "HTML",
                            "url": "/report/productReports/generateProductReport"
                        },
                        pdf: {
                            "name": "PDF",
                            "url": "/report/productReports/generateProductReportAsPdf"
                        }
                    }
                },
                {
                    "name": "Tagessummen",
                    "exports": {
                        html: {
                            "name": "HTML",
                            "url": "/report/dailySalesReports/generateDailySalesReport"
                        },
                        pdf: {
                            "name": "PDF",
                            "url": "/report/dailySalesReports/generateDailySalesReportAsPdf"
                        }
                    }
                }
                ,
                {
                    "name": "Warengruppenbericht",
                    "exports": {
                        html: {
                            "name": "HTML",
                            "url": "/report/categoryReports/generateCategoryReport"
                        },
                        pdf: {
                            "name": "PDF",
                            "url": "/report/categoryReports/generateCategoryReportAsPdf"
                        }
                    }
                }
                ,
                {
                    "name": "Finanzbericht summiert",
                    "exports": {
                        html: {
                            "name": "HTML",
                            "url": "/report/financialReports/generateFinancialReport"
                        },
                        pdf: {
                            "name": "PDF",
                            "url": "/report/financialReports/generateFinancialReportAsPdf"
                        }
                    }
                }
                ,
                {
                    "name": "Finanzbericht pro Tag",
                    "exports": {
                        html: {
                            "name": "HTML",
                            "url": "/report/financialReports/generateFinancialReportPerDays"
                        },
                        pdf: {
                            "name": "PDF",
                            "url": "/report/financialReports/generateFinancialReportPerDaysAsPdf"
                        }
                    }
                }
            ];
            scope.options = {};
            scope.options.startDate = moment().subtract(7, 'days').toDate();
            scope.options.endDate = moment().subtract(1, 'days').toDate();
            var running = false;

            function openNewTab(link) {
                var frm = $('<form method="get" action="' + link + '" target="_blank"></form>');
                $("body").append(frm);
                frm.submit().remove();
            }

            function startDateBeforeRender($dates) {
                if (scope.options.endDate) {
                    var activeDate = moment(scope.options.endDate);
                    if (!running) {
                        running = true;
                        scope.options.endDate = moment(scope.options.endDate).add(1, 'minute').format("YYYY-MM-DD");
                    } else {
                        running = false;
                    }
                    $dates.filter(function (date) {
                        return date.localDateValue() >= activeDate.valueOf()
                    }).forEach(function (date) {
                        date.selectable = false;
                    })
                }
            }

            function endDateBeforeRender($dates) {
                if (scope.options.startDate) {
                    var activeDate = moment(scope.options.startDate).add(1, 'minute');
                    if (!running) {
                        scope.options.startDate = moment(scope.options.startDate).add(1, 'minute').format("YYYY-MM-DD");
                    } else {
                        running = false;
                    }
                    $dates.filter(function (date) {
                        return date.localDateValue() <= activeDate.valueOf()
                    }).forEach(function (date) {
                        date.selectable = false;
                    })
                }
            }

            scope.endDateBeforeRender = endDateBeforeRender;
            scope.startDateBeforeRender = startDateBeforeRender;

            scope.exportReport = function () {
                if (scope.options.report == undefined) {
                    swal("Kein Bericht ausgewählt", "Bitte wählen Sie den Bericht aus, den Sie ansehen möchten!", "info");
                    return;
                }
                if (scope.options.store == undefined) {
                    swal("Keine Filiale ausgewählt", "Bitte wählen Sie die Filiale aus, von welcher Sie den Bericht ansehen möchten!", "info");
                    return;
                }

                var url = "";
                url += scope.options.report.exports.pdf.url;
                url += "?startDate=" + moment(scope.options.startDate).format("YYYY-MM-DDTHH:mm:ss.SSSz");
                url += "&endDate=" + moment(scope.options.endDate).format("YYYY-MM-DDTHH:mm:ss.SSSz");
                url += "&storeGuid=" + scope.options.store;
                window.open(url);
            };
            scope.showReport = function () {
                if (scope.options.report == undefined) {
                    swal("Kein Bericht ausgewählt", "Bitte wählen Sie den Bericht aus, den Sie ansehen möchten!", "info");
                    return;
                }
                if (scope.options.store == undefined) {
                    swal("Keine Filiale ausgewählt", "Bitte wählen Sie die Filiale aus, von welcher Sie den Bericht ansehen möchten!", "info");
                    return;
                }

                var modalInstance = uibModal.open({
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    templateUrl: initOptions.baseDir + 'components/reportPreview/modal/reportModal.html',
                    controller: 'report.reportPreviewModal',
                    size: 'lg',
                    resolve: {
                        model: scope.options,
                    }
                });
            };
        }];
});