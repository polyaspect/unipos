//# sourceURL=pos.reassignOrderChooseUserStrategy.js
define([
    'angular'
], function (angular) {
    return {
        chooseUser : function(injector, user){
            injector.get("pos.reassignOrderService").chooseUser(user);
        }
    };
});