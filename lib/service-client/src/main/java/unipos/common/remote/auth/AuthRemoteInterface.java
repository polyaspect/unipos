package unipos.common.remote.auth;

import unipos.common.remote.auth.model.Right;
import unipos.common.remote.auth.model.Token;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.auth.model.UsernamePassword;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

/**
 * Created by dominik on 28.08.15.
 */
public interface AuthRemoteInterface {

    Token getAuthTokenByRequest(HttpServletRequest request);
    User findUserByAuthToken(String authTokenString);

    /**
     * Query the UserId that is associated with a given AuthToken in the auth-module. Return -1 if no valid user was found!
     * @param authToken the AuthToken value as a String
     * @return the userId, or -1 if no valid user was found.
     */
    Long getUserIdByAuthToken(String authToken);

    User getUserByGuid(String guid);

    User getUserByUserId(Long userId);

    String getCurrentVersion();

    User getUserByUsername(String username);

    /**
     * This method checks the given Username and Password against the Database of the auth module
     * @param username the username to check
     * @param password  the appropriate password for the given username
     * @return true if the given credentials are valid, else false
     */
    User checkAccountCredentials(String username, String password);

    Cookie accountLogin(String username, String pin) throws UserPrincipalNotFoundException;

    void addUsernamePasswordAuthMethod(UsernamePassword usernamePassword);

    void addRights(List<Right> rightList);
}
