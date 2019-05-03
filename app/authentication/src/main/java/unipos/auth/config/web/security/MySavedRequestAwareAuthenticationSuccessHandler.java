package unipos.auth.config.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import unipos.auth.components.authentication.SessionIdentifierGenerator;
import unipos.auth.components.authentication.Token;
import unipos.auth.components.authentication.TokenManagement;
import unipos.auth.components.user.UserRepository;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinAuthenticationToken;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinService;
import unipos.auth.components.user.usernamePassword.UsernamePasswordService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Dominik.
 * This class gets called after the user gets Authenticated by Spring Security the {@method onAuthenticationSuccess} method gets directly called after the
 * successful authentication.
 */
public class MySavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    TokenManagement tokenManagement = TokenManagement.getInstance();

    @Autowired
    SessionIdentifierGenerator sessionIdentifierGenerator;

    @Autowired
    MitarbeiteridPinService mitarbeiteridPinService;

    @Autowired
    UsernamePasswordService usernamePasswordService;

    @Autowired
    UserRepository userRepository;

    private RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * This method gets called, after the Authentication was successfull. It overrides the base class, so that we get not redirected to any page,
     * because that would not be very clever in a REST Authentication. It also clears the cache of a potential previous Session
     * After that, it generates a new AuthToken (COOKIE)..
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            clearAuthenticationAttributes(request);

            generateAuthenticationToken(response, authentication);
            return;
        }
        String targetUrlParam = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() ||
                (targetUrlParam != null &&
                        StringUtils.hasText(request.getParameter(targetUrlParam)))) {
            requestCache.removeRequest(request, response);
            clearAuthenticationAttributes(request);

            generateAuthenticationToken(response, authentication);
            return;
        }

        clearAuthenticationAttributes(request);

        generateAuthenticationToken(response, authentication);
    }

    /**
     * Basically {@instance tokenManagement} stores the tokens as a KEY:VALUE Pair, whereas the Key
     * is the JSESSIONID and the value is a Token Instance.
     * {@method generateAuthenticationToken} creates a new AuthToken (COOKIE) and associates it with the
     * JSESSIONID that Spring generates for (KEY:VALUE) the User. If the user (same JSESSIONID) logs in again,
     * he gets a new Token (update of the token Instance)
     *
     * @param response Is the response to put the cookie on
     */
    private void generateAuthenticationToken(HttpServletResponse response, Authentication authentication) {

        Authentication asdf = SecurityContextHolder.getContext().getAuthentication();

        //Generate the TokenString and the Token Instance
        String tokenString = sessionIdentifierGenerator.nextSessionId();

        Token token = new Token(tokenString);

        if(tokenManagement.isNew(token)) {
            token.setUser(userRepository.findByUserId((Long) asdf.getPrincipal()));

            //Save it
            tokenManagement.addToken(token);

        } else {
            //Try generating new Tokens as long as no new token is generated
            tokenManagement.removeToken(getSession());

            token.setUser(userRepository.findByUserId((Long) asdf.getPrincipal()));

            tokenManagement.addToken(token);
        }

        //Add the AuthToken Cookie to the response
        Cookie cookie = new Cookie("AuthToken", token.getToken());
        cookie.setPath("/");
        cookie.setMaxAge(315360000);
        response.addCookie(cookie);


    }

    /**
     * Request the Cookie of the requesting User
     * @return the SessionID
     */
    private String getSession() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

}
