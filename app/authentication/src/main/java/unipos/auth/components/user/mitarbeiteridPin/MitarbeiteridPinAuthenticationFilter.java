package unipos.auth.components.user.mitarbeiteridPin;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Dominik on 03.06.2015.
 */

public class MitarbeiteridPinAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_MITARBEITERNR_KEY = "mitarbeiternr";
    public static final String SPRING_SECURITY_FORM_PIN_KEY = "pin";

    private String mitarbeiteridParameter = SPRING_SECURITY_FORM_MITARBEITERNR_KEY;
    private String pinParameter = SPRING_SECURITY_FORM_PIN_KEY;
    private boolean postOnly = true;

    // ~ Constructors
    // ===================================================================================================

    public MitarbeiteridPinAuthenticationFilter() {
        super(new AntPathRequestMatcher("/auth/pin_login", "POST"));
    }

    // ~ Methods
    // ========================================================================================================

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        int mitarbeiterid = -1;
        int pin = -1;

        try {
            mitarbeiterid = obtainMitarbeiterid(request);
            pin = obtainPin(request);
        } catch (NumberFormatException e) {
            throw new BadCredentialsException("Mitarbeiter Nr. oder Pin waren keine Zahlen");
        }

        MitarbeiteridPinAuthenticationToken authRequest = new MitarbeiteridPinAuthenticationToken(mitarbeiterid, pin);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Enables subclasses to override the composition of the password, such as by
     * including additional values and a separator.
     * <p>
     * This might be used for example if a postcode/zipcode was required in addition to
     * the password. A delimiter such as a pipe (|) should be used to separate the
     * password and extended value(s). The <code>AuthenticationDao</code> will need to
     * generate the expected password in a corresponding manner.
     * </p>
     *
     * @param request so that request attributes can be retrieved
     *
     * @return the password that will be presented in the <code>Authentication</code>
     * request token to the <code>AuthenticationManager</code>
     */
    protected int obtainPin(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter(pinParameter));
    }

    /**
     * Enables subclasses to override the composition of the username, such as by
     * including additional values and a separator.
     *
     * @param request so that request attributes can be retrieved
     *
     * @return the username that will be presented in the <code>Authentication</code>
     * request token to the <code>AuthenticationManager</code>
     */
    protected int obtainMitarbeiterid(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter(mitarbeiteridParameter));
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication
     * request's details property.
     *
     * @param request that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details
     * set
     */
    protected void setDetails(HttpServletRequest request,
                              UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     * Sets the parameter name which will be used to obtain the username from the login
     * request.
     *
     * @param mitarbeiteridParameter the parameter name. Defaults to "username".
     */
    public void setMitarbeiteridParameter(String mitarbeiteridParameter) {
        Assert.hasText(mitarbeiteridParameter, "Username parameter must not be empty or null");
        this.mitarbeiteridParameter = mitarbeiteridParameter;
    }

    /**
     * Sets the parameter name which will be used to obtain the password from the login
     * request..
     *
     * @param pinParameter the parameter name. Defaults to "password".
     */
    public void setPinParameter(String pinParameter) {
        Assert.hasText(pinParameter, "Password parameter must not be empty or null");
        this.pinParameter = pinParameter;
    }

    /**
     * Defines whether only HTTP POST requests will be allowed by this filter. If set to
     * true, and an authentication request is received which is not a POST request, an
     * exception will be raised immediately and authentication will not be attempted. The
     * <tt>unsuccessfulAuthentication()</tt> method will be called as if handling a failed
     * authentication.
     * <p>
     * Defaults to <tt>true</tt> but may be overridden by subclasses.
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMitarbeiteridParameter() {
        return mitarbeiteridParameter;
    }

    public final String getPinParameter() {
        return pinParameter;
    }

}
