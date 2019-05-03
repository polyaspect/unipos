//# sourceURL=pos.designer.designerSettingsService.js
define([
    'angular'
], function (angular) {
    return ['pos.dataService', '$timeout', 'pos.designer.screenService', '$location', 'pos.initOptions', function (dataService, timeout, screenService, location, initOptions) {
        this.serviceName = "design.designerSettingsService";
        var self = this;
        this.textController = "design.textDirectiveSettings";
        dataService.setByServiceKey(self.serviceName, {});

        this.setDirectiveData = function (directive, data) {
            dataService.setByDirectiveKey(self.serviceName, directive, data);
        };
        this.getDirectiveData = function (directive) {
            return dataService.getByDirectiveKey(self.serviceName, directive);
        };
        this.setDataByDirectiveAndKey = function (directive, key, data) {
            var directiveData = self.getDirectiveData(directive);
            if (!directiveData) {
                directiveData = {};
            }
            directiveData[key] = data;
            self.setDirectiveData(directive, directiveData);
        };
        this.getDataByDirectiveAndKey = function (directive, key) {
            return self.getDirectiveData(directive)[key];
        };
        this.getDimension = function (portrait, size, actWidth, actHeight) {
            let baseHeight;
            let baseWidth;
            let liveViewHeight;
            let liveViewWidth;
            let viewFactor = {
                landscape: {
                    S: 0.4,
                    M: 0.6,
                    L: 0.8,
                    XL: 1.0,
                },
                portrait: {
                    S: 0.6,
                    M: 0.8,
                    L: 1.0,
                }
            };
            if (portrait) {
                if (((actHeight / 16) * 9 > actWidth)) {
                    baseHeight = (actWidth / 9) * 16;
                    baseWidth = actWidth;
                } else {
                    baseHeight = actHeight;
                    baseWidth = (actHeight / 16) * 9;
                }
                if (size == 'XL') {
                    size = 'S';
                }
                liveViewWidth = baseWidth * viewFactor.portrait[size];
                liveViewHeight = baseHeight * viewFactor.portrait[size];
            } else {
                if (((actHeight / 9) * 16) > actWidth) {
                    baseHeight = (actWidth / 16) * 9;
                    baseWidth = actWidth;
                } else {
                    baseHeight = actHeight;
                    baseWidth = (actHeight / 9) * 16;
                }
                liveViewWidth = baseWidth * viewFactor.landscape[size];
                liveViewHeight = baseHeight * viewFactor.landscape[size];
            }
            return {
                width: Math.floor(liveViewWidth/100)*100 + 'px',
                height: Math.floor(liveViewHeight/100)*100 + 'px',
                top: (actHeight / 2) - (Math.floor(liveViewHeight/100)*100 / 2) + 'px'
            }
        };
    }];
});
