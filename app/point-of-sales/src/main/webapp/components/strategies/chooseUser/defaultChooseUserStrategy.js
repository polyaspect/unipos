//# sourceURL=pos.defaultChooseUserStrategy.js
define([
    'angular'
], function (angular) {
    return {
        chooseUser : function(injector, user){
            injector.get("pos.chooseUserService").chooseUser(user);
        }
    };
});