//# sourceURL=pos.designer.screenCollectionApiService.js
define([
    'angular'
], function (angular) {
    return ['$resource', function (resource) {
        var screenCollectionResource = resource('/design/screenCollection', {}, {
            listNames: {
                url: '/design/screenCollection/listNames/:published',
                method: 'GET',
                isArray: true
            },
            findByName: {
                url: '/design/screenCollection/:name/:published',
                method: 'GET',
                isArray: false
            },
            getCompanies: {
                url: '/data/companies',
                method: 'GET',
                isArray: true
            },
            publish: {
                url: '/design/screenCollection/publish/',
                method: 'POST',
                isArray: false
            },
            setDefault:{
                url: '/design/screenCollection/default/',
                method: 'POST',
                isArray: false
            }
        });
        this.listNames = function () {
            return screenCollectionResource.listNames({published: false});
        };
        this.save = function (data) {
            return screenCollectionResource.save(data);
        };
        this.findByName = function (name) {
            return screenCollectionResource.findByName({name: name, published: false});
        };
        this.getCompanies = function () {
            return screenCollectionResource.getCompanies();
        };
        this.publish = function (screenCollectionName) {
            return screenCollectionResource.publish(screenCollectionName);
        };
        this.setDefault = function (screenCollectionName) {
            return screenCollectionResource.setDefault(screenCollectionName);
        };
    }];
});