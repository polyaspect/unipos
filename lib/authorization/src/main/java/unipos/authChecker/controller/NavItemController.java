package unipos.authChecker.controller;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import unipos.authChecker.domain.AuthTokenManager;
import unipos.authChecker.domain.NavigationMenuItem;
import unipos.authChecker.domain.NavigationSubMenuItem;
import unipos.authChecker.interceptors.AuthenticationManager;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.Right;
import unipos.common.remote.auth.model.User;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dominik on 25.06.2015.
 */
@RestController
@RequestMapping("/navItem")
public class NavItemController {
    @Autowired
    private AuthTokenManager authTokenManager;

    @Autowired
    private AuthRemoteInterface authRemoteInterface;

    @RequestMapping(value = "/{authToken}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public NavigationMenuItem nav(@PathVariable("authToken") String authToken) throws Exception{
        InputStream permissionsIS = this.getClass().getResourceAsStream("/navigation.json");
        NavigationMenuItem menuItem = null;

        if(permissionsIS != null) {
            String json = null;
            try {
                json = IOUtils.toString(permissionsIS, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            IOUtils.closeQuietly(permissionsIS);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                menuItem = objectMapper.readValue(json, NavigationMenuItem.class);

            } catch (Exception ex) {
                throw new JsonMappingException("Unable to parse the navigation file");
            }
        }

        boolean hasModuleAccess = false;

        User user = authRemoteInterface.findUserByAuthToken(authToken);
        List<Right> rights =  user.getRights();
        for(Right right : rights){
            if(right.getName().equals(menuItem.getRight())){
                hasModuleAccess = true;
            }
        }

        List<NavigationSubMenuItem> newSubMenuItems = new ArrayList<>();
        if(hasModuleAccess){
            NavigationSubMenuItem[] originalSubMenuItems = menuItem.getSubItems().clone();

            for(NavigationSubMenuItem subMenuItem : originalSubMenuItems){
                for(Right right : rights){
                    if(right.getName().equals(subMenuItem.getRight())){
                        newSubMenuItems.add(subMenuItem);
                    }
                }
            }
        }else{
            return null;
        }

        menuItem.setSubItems(newSubMenuItems.toArray(new NavigationSubMenuItem[newSubMenuItems.size()]));
        return menuItem;
    }

}
