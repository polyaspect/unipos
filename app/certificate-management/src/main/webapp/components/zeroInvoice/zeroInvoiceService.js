//# sourceURL=zeroInvoiceService.js
define([], function () {
    return ["$resource", "$http", function ($resource, $http) {

        var invoiceResource = $resource('/pos/invoices', {}, {
            findByCompanyGuidAndSignatureInvoiceType: {
                url: '/pos/invoices/findByCompanyGuidAndSignatureInvoiceType',
                method: 'GET',
                isArray:true
            },
            findZeroInvoicesByCompanyGuid: {
                url: '/pos/invoices/findZeroInvoicesByCompanyGuid/:companyGuid',
                method: 'GET',
                isArray:true
            },
            getUserCompanyGuid: {
                url: '/auth/auth/getCompanyGuidByAuthToken',
                method: 'GET',
                isArray: false
            }
        });

        var zeroInvoiceResource = $resource('/signature/signatures', {}, {
            createZeroInvoice: {
                url: '/signature/signatures/createNullInvoice',
                method: 'POST',
                isArray: false
            },
            createStartInvoice: {
                url: '/signature/signatures/createStartInvoice',
                method: 'POST',
                isArray: false
            }
        });

        this.getInvoicePreviewText = function(invoice) {
            return $http.post("/report/invoiceReports/getInvoiceText", invoice);
        };
        this.reprintInvoice = function(invoice) {
            return $http.post("/report/invoiceReports/reprintInvoice", invoice);
        };
        this.findByCompanyGuidAndSignatureInvoiceType = function(companyGuid, signatureInvoiceType) {
            return invoiceResource.findByCompanyGuidAndSignatureInvoiceType({companyGuid:companyGuid, signatureInvoiceType:signatureInvoiceType});
        };
        this.findZeroInvoicesByCompanyGuid = function(companyGuid) {
            return invoiceResource.findZeroInvoicesByCompanyGuid({companyGuid:companyGuid});
        };

        this.createZeroInvoice = function(invoiceType){
            return $http({
                method: 'POST',
                url: "/signature/signatures/createNullInvoice",
                data: "invoiceType=" + invoiceType,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        };
        this.createStartInvoice = function(){
            return zeroInvoiceResource.createStartInvoice();
        };

        this.getCompanyGuidByAuthToken = function () {
            return $http.get("/auth/auth/getCompanyGuidByAuthToken");
        };
    }];
});