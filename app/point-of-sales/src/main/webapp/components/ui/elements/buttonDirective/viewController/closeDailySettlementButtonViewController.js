//# sourceURL=pos.showDailySettlementButtonViewController.js
define([
    'angular'
], function (angular) {
    return ['pos.services.dailySettlementService', 'pos.ui.screenService', function (dailySettlementService, screenService) {
        // INTERFACE REQUIREMENTS:

        var self = this;
        this.class = "pos.reportPreviewGridViewController";

        this.getViewData = function (dataSource, metaData) {
        };

        this.onClick = function(metaData){
            if(metaData.monthly){
                dailySettlementService.closeMonthlySettlement(function(){
                    screenService.toScreen(metaData.screenName);
                });
            }else{
                dailySettlementService.closeDailySettlement(function(){
                    screenService.toScreen(metaData.screenName);
                });
            }
        };

        // CUSTOM::

    }];
});