package unipos.auth.components.role;

import java.util.List;

/**
 * Created by Dominik on 28.05.2015.
 */
public interface RoleService {
    void createRole(Role role);
    List<Role> findAllRoles();
}
