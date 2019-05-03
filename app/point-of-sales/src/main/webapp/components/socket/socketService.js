//# sourceURL=pos.socketService.js
define([
    'angular',
    'pos.sockJs',
    'pos.ngStomp',
    'pos.stompie',
    'pos.stomp'
], function (angular) {
    return ["$stomp", "$http", 'pos.urlSettings', '$q',
        function (stomp, http, urlSettings, $q) {

            var self = this;
            this.connected = false;
            this.connectionLost = false;
            this.subscriptions = [];
            this.reloadWhenPossible = false;

            this.readCookie = function (cookieName) {
                var theCookie = " " + document.cookie;
                var ind = theCookie.indexOf(" " + cookieName + "=");
                if (ind == -1) ind = theCookie.indexOf(";" + cookieName + "=");
                if (ind == -1 || cookieName == "") return "";
                var ind1 = theCookie.indexOf(";", ind + 1);
                if (ind1 == -1) ind1 = theCookie.length;
                return unescape(theCookie.substring(ind + cookieName.length + 2, ind1));
            };

            this.setReloaded = function () {
                http.post(urlSettings.baseUrl + "/socket/device/reloaded");
            };

            this.connect = function () {
                self.keepAlive();
                self.setReloaded();
                self.checkForReload();
                self.startConnect();
            };

            window.identity="safecash";
            window.onfocus=function(event){
                if(event.target.identity==="safecash"){
                    self.doCheckForReload();
                }
            };

            this.startConnect = function () {
                if (self.connected)
                    return;

                self.connection = stomp.connect("/socket/endpoint", {}, function (error) {
                });

                self.connection.then(function (frame) {
                    self.connected = true;

                    var timeout = 100;
                    var toProcessSubscriptions = self.subscriptions.length;
                    self.subscriptions.forEach(function (subscription) {
                        setTimeout(function () {
                            stomp.subscribe(subscription.url, subscription.callback);
                            toProcessSubscriptions -= 1;
                            if (toProcessSubscriptions == 0) {
                                http.post(urlSettings.baseUrl + "/socket/clientStarted").then(function (response) {
                                }, function (response) {
                                    if (response.status == 500) {
                                        swal("Technischer Fehler", "Der Server ist fehlerhaft konfiguriert: 'Kein Zugriff auf die Liste der installierten Module'. Details zu diesem Fehler sind in den Server-Logs zu finden.", "error");
                                    }
                                });
                            }
                        }, timeout);
                        timeout += 100;
                    });
                });
            };


            this.checkForReload = function () {
                setInterval(function () {
                    self.doCheckForReload();
                }, 60000);
            };

            this.doCheckForReload = function() {
                http.get(urlSettings.baseUrl + "/socket/device").then(function (response) {
                    if (response.data.reloadRequired != undefined && response.data.reloadRequired == true) {
                        swal({
                            title: "Neuladen erforderlich!",
                            text: "Ein Software-Update wurde installiert. Damit die Änderungen aktiv werden, muss die Anwendung für wenige Sekunden neugeladen werden. Ihre Eingaben bleiben erhalten!",
                            type: "warning",
                            cancelButtonText: 'Abbrechen',
                            confirmButtonText: 'Jetzt neuladen',
                            showCancelButton: true
                        }, function () {
                        });
                    }
                    if(response.data.forceReload != undefined && response.data.forceReload == true) {
                        window.location.reload();
                    }
                    if(response.data.reloadWhenPossible != undefined){
                        self.reloadWhenPossible = response.data.reloadWhenPossible;
                    }

                });
            };

            this.reloadPossible = function(){
                if(self.reloadWhenPossible){
                    window.location.reload();
                }
            };

            this.keepAlive = function () {
                setInterval(function () {
                    var response = http.get(urlSettings.baseUrl + "/socket/isAlive", {timeout: 5000}).then(
                        function (response) {
                            if (self.connectionLost) {
                                setTimeout(self.startConnect, 5000);

                                swal({
                                    type: 'success',
                                    title: "Verbindung hergestellt",
                                    text: "Das Eingabegerät hat die Verbindung zum Kassenserver wieder hergestellt.",
                                    timer: 3000
                                });
                            }
                            self.connectionLost = false;
                        },
                        function (response) {
                            if (response.status <= 0) {
                                if (!self.connectionLost) {
                                    swal({
                                        type: 'error',
                                        title: "Verbindung verloren",
                                        text: "Das Eingabegerät hat die Verbindung zum Kassenserver verloren.",
                                        timer: 3000
                                    });
                                }
                                self.connectionLost = true;
                                self.disconnect();
                            }
                        }
                    );

                }, 10000);
            };

            this.disconnect = function () {
                self.connected = false;
                self.connection = undefined;
                stomp.disconnect(function () {
                });
            };

            this.subscribeBroadcast = function (url, callback) {
                var actualUrl = '/topic/' + url;
                if (!self.connected) {
                    self.subscriptions.push({url: actualUrl, callback: callback});
                } else {
                    stomp.subscribe(url, callback);
                }
            };

            this.subscribeDevice = function (url, callback) {
                var actualUrl = '/device/' + self.readCookie("DeviceToken") + '/' + url;
                if (!self.connected) {
                    self.subscriptions.push({url: actualUrl, callback: callback});
                } else {
                    stomp.subscribe(url, callback);
                }
            };

            this.subscribeUser = function (url, callback) {
                // muss implementiert werden!!!
            };
        }]
});