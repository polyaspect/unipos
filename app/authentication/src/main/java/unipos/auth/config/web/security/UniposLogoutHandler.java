package unipos.auth.config.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import unipos.auth.components.authentication.TokenManagement;
import unipos.common.container.RequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Dominik on 18.06.2015.
 */
public class UniposLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    TokenManagement tokenManagement = TokenManagement.getInstance();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String sessionId = request.getRequestedSessionId();

        if (sessionId == null) {
            response.setStatus(418);
        }

        //Check if the SessionID is existent in the TokenStorage Cache.
        //If so, delete the authToken and remove it
        if (!tokenManagement.isNew(RequestHandler.getAuthToken(request))) {
            tokenManagement.removeToken(sessionId);
            response.setStatus(200);
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("AuthToken")) {
                    cookie.setMaxAge(0);
                    cookie.setValue(null);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }

        super.onLogoutSuccess(request, response, authentication);
    }
}
