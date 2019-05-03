package unipos.auth.components.authentication;

/**
 * Created by Dominik on 29.05.2015.
 */

import lombok.Data;
import org.springframework.util.Assert;
import unipos.auth.components.right.Right;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Managing all the Tokens (authToken) is the Job of this class.
 * Key => JSESSIONID (of the auth-module)
 * Value => tokenInstance
 */

@Data
public class TokenManagement {

    private ArrayList<Token> tokens;

    private static class StaticHolder {
        static final TokenManagement INSTANCE = new TokenManagement();
    }

    public static TokenManagement getInstance() {
        return StaticHolder.INSTANCE;
    }

    private TokenManagement() {
        tokens = new ArrayList<>();
    }

    /**
     * Adds the @param token to the storage. If the token is already existent in the storage, it throws a new IllegalArgumentException
     * @param token Is the token you want to save in the storage
     */
    public void addToken(Token token) throws IllegalArgumentException {
        //delete all older sessions from the current User. This disables concurrent login on multiple devices!!!
//        tokens.removeIf(x -> x.getUser().getUserId().equals(token.getUser().getUserId()));

        if(tokens.stream().filter(x -> x.getToken().equals(token.getToken())).count() > 0) {
            throw new IllegalArgumentException("Key already existing");
        }

        tokens.add(token);
    }

    /**
     * Removes the token from the storage. Throws an IllagalArgumentException if the token was not found
     * @param token is the Token you want to remove from the storage
     */
    public void removeToken(Token token) throws IllegalArgumentException {
        if(tokens.stream().filter(x -> x.getToken().equals(token.getToken())).count() > 0) {
            tokens.removeIf(x -> x.getToken().equals(token.getToken()));
        } else {
            throw new IllegalArgumentException("Key not existent in Key Storage");
        }
    }

    public void removeToken(String tokenString) throws IllegalArgumentException {
        if(tokens.stream().filter(x -> x.getToken().equals(tokenString)).count() > 0) {
            tokens.removeIf(x -> x.getToken().equals(tokenString));
        } else {
            throw new IllegalArgumentException("Key not existent in Key Storage");
        }
    }

    /**
     * Checks if the token is existent in the saved list of tokens. Based
     * @param token The token you want to check
     * @return true if the token is not existent in the storage
     */
    public boolean isNew(Token token) {
        return tokens.stream().filter(x -> x.getToken().equals(token.getToken())).count() == 0;
    }

    public boolean isNew(String tokenString) {
        return tokens.stream().filter(x -> x.getToken().equals(tokenString)).count() == 0;
    }
    /**
     * Clears the sessionStorageCache
     */
    public void removeAllTokens() {
        this.tokens = new ArrayList<>();
    }

    public Token getTokenByTokenString(String tokenString) {
        return tokens.stream().filter(x -> x.getToken().equals(tokenString)).findFirst().orElse(null);
    }

    public Long findUserIdByAuthToken(String authToken) {
        Token token = getTokenByTokenString(authToken);

        if(token != null && token.getUser() != null) {
            return token.getUser().getUserId();
        } else {
            return null;
        }
    }

    public void updateTokenPermissions(List<Token> authTokens, List<Right> rightList) {
        authTokens.stream().forEach(x -> {
            Token token = getTokenByTokenString(x.getToken());
            token.getUser().setRights(rightList);
            Assert.notNull(token, "No token found for the given TokenString \"" + x.getToken() + "\"!");
        });
    }

    public List<Token> getTokensByUserId(Long userId) {
        return tokens.stream().filter(x -> x.getUser().getUserId().equals(userId)).collect(toList());
    }
}
