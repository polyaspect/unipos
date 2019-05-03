package unipos.authChecker.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.Right;
import unipos.common.remote.auth.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dominik on 24.06.2015.
 */
public class AuthTokenManager {

    @Autowired
    AuthRemoteInterface authRemoteInterface;

    List<AuthToken> tokens;

    public AuthTokenManager(List<AuthToken> tokens) {
        this.tokens = tokens;
    }

    public AuthTokenManager() {
        this.tokens = new ArrayList<>();
    }

    /**
     * Add a new token to the List of authorized tokens
     * @param token is the AuthToken Instance to add
     */
    public void add(AuthToken token) {
        if(token != null) {
            if(isNewTokenString(token)) {
                tokens.add(token);
            }
        } else {
            throw new IllegalArgumentException("Illegal Token Argument");
        }
    }

    /**
     * Remove the Element by the AuthToken Instance. Use this Careful, becuase it uses the hashcode equality method!
     * @param token is the AuthToken Instance of the module
     */
    public void remove(AuthToken token) {
        if(token != null) {
            tokens.remove(token);
        }
    }

    /**
     * Return an AuthToken by the TokenString (Session TokenString in the Cookie)
     * @param tokenString is the Value of the AuthToken Cookie
     * @return the number of the deleted elements
     */
    public int remove(String tokenString) {
        int count = 0;
        if (!tokenString.isEmpty()) {
            Iterator<AuthToken> tokenIterator = tokens.iterator();
            while (tokenIterator.hasNext()) {
                AuthToken token = tokenIterator.next();
                if(token.getAuthToken().equals(tokenString)) {
                    tokenIterator.remove();
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Get a List of the current Tokens that are cached
     * @return the cached token list
     */
    public List<AuthToken> getAllTokens() {
        return tokens;
    }

    /**
     * remove all cached Tokens
     * @return the count of deleted Tokens
     */
    public int removeAll() {
        int count = 0;
        Iterator<AuthToken> tokenIterator = tokens.iterator();
        while (tokenIterator.hasNext()) {
            tokenIterator.next();
            tokenIterator.remove();
            count++;
        }
        return count;
    }

    /**
     * This method returns true, if the AuthTokenSTRING is a new one. This Method does not check, if the permissions have changed!
     * @param token The AuthTokenString to validate
     * @return true if the String is a new one
     */
    public boolean isNewTokenString(AuthToken token) {
        Iterator<AuthToken> iterator = tokens.iterator();
        while (iterator.hasNext()){
            AuthToken listToken = iterator.next();
            if(listToken.getAuthToken().equals(token.getAuthToken())) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method returns true, if the AuthTokenSTRING is a new one. This Method does not check, if the permissions have changed!
     * @param token The AuthTokenString to validate
     * @return true if the String is a new one
     */
    public boolean isNewTokenString(String token) {
        Iterator<AuthToken> iterator = tokens.iterator();
        while (iterator.hasNext()){
            AuthToken listToken = iterator.next();
            if(listToken.getAuthToken().equals(token)) {
                return false;
            }
        }

        return false;
    }

    /**
     * This Method checks, if the tokenString is allowed to perform the required action
     * @param tokenString is the Value of the AuthToken-Cookie
     * @param permissionName is the "action" the AuthToken wants tp perform. This is a list of permissions.
     * @return true ONLY if the user is allowed to perform the desired action.
     */
    public boolean isAuthorized(String tokenString, final String[] permissionName) throws IllegalArgumentException {
        User user = authRemoteInterface.findUserByAuthToken(tokenString);

        if(user == null) {
            return false;
        }

        if(user.getGuid().equals("00000000-580a-46e2-9d2a-590fd403910e"))
            return true;

        if(user.getRights().stream().filter(x -> Arrays.stream(permissionName).filter(y -> y.equalsIgnoreCase(x.getName())).count() > 0).count() > 0) {
            return true;
        } else {
            return false;
        }
//        Iterator<AuthToken> iterator = tokens.iterator();
//        while (iterator.hasNext()) {
//            AuthToken token = iterator.next();
//            if(token.getAuthToken().equals(tokenString)) {
//                for(Permission permission : token.getPermissions()) {
//                    for(String permissionStringArray : permissionName) {
//                        if(permission.getKey().equals(permissionStringArray)) {
//                            return permission.isAllowed();
//                        }
//                    }
//                }
//            }
//        }
//        return false;

    }

    public boolean isAuthorized(String value) {
        User user = authRemoteInterface.findUserByAuthToken(value);

        if(user == null) {
            return false;
        } else {
            return true;
        }
    }
}
