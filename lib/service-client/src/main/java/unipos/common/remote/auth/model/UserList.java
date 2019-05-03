package unipos.common.remote.auth.model;

import java.util.ArrayList;

/**
 * Created by ggradnig on 18.01.2017.
 */
public class UserList extends ArrayList<User> {
    public boolean containsUserGuid(String guid){
        for(User user : this){
            if(user.getGuid().equals(guid))
                return true;
        }
        return false;
    }
}
