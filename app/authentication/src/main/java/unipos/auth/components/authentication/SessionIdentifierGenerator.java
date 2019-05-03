package unipos.auth.components.authentication;

/**
 * @author Dominik
 */

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Creating a SessionIdentifierGenerator as a Singleton, because using a SecureRandom Instance is
 * very expensive in creation
 */
public class SessionIdentifierGenerator {

    private SecureRandom random = new SecureRandom();

    public SessionIdentifierGenerator() {}

    /**
     * Generate a new Token
     * @return The new generated Token as a String
     */
    public String nextSessionId() {
        return new BigInteger(130,random).toString(32);
    }

}
