//# sourceURL=pos.queueService.js
define([
    'angular'
], function (angular) {
    return ['$interval', '$http', 'pos.urlSettings', function (interval, $http, urlSettings) {
        var queue = [];
        var running = false;
        var self = this;
        this.addToQueue = function (queueItem) {
            queue.push(queueItem);
        };

        interval(function () {
            next();
        }, 100);

        function next() {
            if (queue.length > 0 && !running) {
                running = true;
                var item = queue[0];
                $http({method: item.method, url: item.url, data: item.data, headers: item.headers}).then(function (response) {
                    if (response.status == 200) {
                        if(item.callback != undefined){
                            item.callback(response.data);
                        }
                        queue.splice(0, 1);
                    }
                    running = false;
                }, function (response) {
                    if(response.status == 0){
                        running = false;
                    }
                    else if (response.status == 404) {
                        queue.splice(0, 1);
                        self.addToQueue({
                            url: urlSettings.baseUrl + urlSettings.log,
                            method: "post",
                            data: {
                                event:'Queue404',
                                parameters:{
                                    'url':item.url
                                }
                            }
                        });
                        running = false;
                    }
                    else{
                        queue.splice(0, 1);
                        if(item.errorCallback != undefined){
                            item.errorCallback(response);
                        }
                        running = false;
                    }
                });
            }
        }
    }];
});