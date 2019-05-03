//# sourceURL=pos.jkButtonService.js
define([
    'angular'
], function (angular) {
    return ['$injector', function (injector) {
        this.buttonClick = function (cell) {
            if(cell.legacy != undefined && cell.legacy == false){
                try{
                    var viewController = injector.get("pos.ui.elements.button." + cell.elementType + "ViewController");
                    viewController.onClick(cell.metaData);
                    return;
                }
                catch(ex){
                    alert("ViewController für pos.ui.elements.button." + cell.elementType + "ViewController fehlt");
                    return;
                }
            }

            var service = injector.get(cell.serviceName);
            if (service != undefined) {
                service[cell.serviceAction](cell);
            }
        };
    }];
});