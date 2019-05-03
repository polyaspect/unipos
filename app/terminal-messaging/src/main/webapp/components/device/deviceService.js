//# sourceURL=deviceService.js
/**
 * Created by dominik on 04.08.15.
 */

define(["angular"], function (angular) {
    return ["$resource", function ($resource) {

        var deviceResource = $resource('/socket/device', {}, {
            all: {
                method: 'GET',
                isArray:true,
                url: '/socket/device/all'
            },
            deleteDeviceByGuid: {
                method: 'DELETE',
                isArray:false,
                url: '/socket/device/guid'
            },
            setDevice: {
                method: 'PUT',
                isArray:false,
                url: '/socket/device/set/:deviceId'
            }
        });

        this.findAll = function () {
            var devices = deviceResource.all();
            return devices;
        };

        this.save = function(device) {
            return deviceResource.save(device);
        };

        this.checkIfResourceInstance = function(instance) {
            return (instance instanceof deviceResource);
        };

        this.update = function (device) {
            return device.$save();
        };

        this.save = function(device) {
            return deviceResource.save(device);
        };

        this.setDevice = function(deviceId){
            deviceResource.setDevice(deviceId);
        };

        this.deleteByGuid = function(guid) {
            return deviceResource.deleteDeviceByGuid({guid: guid});
        };
    }];
});