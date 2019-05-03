package unipos.authChecker.interceptors;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;
import unipos.authChecker.domain.AuthTokenManager;
import unipos.authChecker.domain.Permission;
import unipos.common.remote.auth.model.Right;
import unipos.common.remote.core.CoreRemoteInterface;
import unipos.common.remote.core.model.ModuleStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by domin on 26.05.2016.
 */
@Component
public class AuthenticationManager {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CoreRemoteInterface coreRemoteInterface;
    @Autowired
    Environment environment;

    private List<Right> availablePermissions;

    @Autowired
    AuthTokenManager tokenManager;

    public boolean checkPermissionsWithErrorResponse(HttpServletRequest request, HttpServletResponse response, String... permissions) throws RuntimeException{
        Cookie cookie = WebUtils.getCookie(request, "AuthToken");

        if(cookie == null) {
            response.setStatus(401);
            return false;
        }

        boolean isAuthorized = tokenManager.isAuthorized(cookie.getValue(), permissions);

        if(isAuthorized) {
            return true;
        } else {
            String error = "";
            for(String perm : permissions){
                error += getHumanReadableNameForPermission(perm) + ", ";
            }
            error = error.substring(0, error.length() - 2);

            try{
                response.setStatus(401);
                response.setContentType("text/html");
                response.getWriter().write(error);
                response.getWriter().flush();
                response.getWriter().close();
            }catch(IOException e){
                throw new RuntimeException(e);
            }
            return false;
        }
    }

    public void propagateAvailablePermissions() throws JsonMappingException {
        while (!coreRemoteInterface.getModuleStatus("core").equals(ModuleStatus.RUNNING)) {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        InputStream permissionsIS = this.getClass().getResourceAsStream("/permissions.json");
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
                List<Right> rights = Arrays.asList(objectMapper.readValue(json, Right[].class));
                availablePermissions = rights;

                coreRemoteInterface.addPermissions(rights);
            } catch (Exception ex) {
                throw new JsonMappingException("Unable to parse the Permissions JSON File!!!");
            }
        }

    }

    private String getHumanReadableNameForPermission(String permissionKey){
        String result = "ERROR_PERMISSION_NOT_FOUND";
        for(Right permission : availablePermissions){
            if(permission.getName().equals(permissionKey)){
                result = permission.getPartition() + " - " + permission.getViewName();
            }
        }
        return result;
    };
}
