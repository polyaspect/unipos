package unipos.auth.components.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Dominik on 03.01.2016.
 */
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Role> findAllRoles() {
        return roleService.findAllRoles();
    }
}
