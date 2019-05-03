package unipos.common.container;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dominik on 10.01.2016.
 */
public class RequestHandler {

    /**
     * Tries to get the Cookie Value of a Request.
     * @param request the Request you try to use
     * @param cookieName the name of the Cookie. Is case sensitive
     * @return the CookieValue. If the cookie is not available null
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        List<Cookie> cookies = new ArrayList<>();
        if(request.getCookies() == null) {
            return null;
        }
        Collections.addAll(cookies, request.getCookies());
        if(cookies.stream().filter(c -> c.getName().equals(cookieName)).findFirst().isPresent())
            return cookies.stream().filter(c -> c.getName().equals(cookieName)).findFirst().get().getValue();

        return null;
    }

    /**
     * Deletes a cookie of a given request
     * @param request the Request you want to delete a cookie from
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        if(request.getCookies() == null) {
            return;
        }
        Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(cookieName)).findFirst().ifPresent(cookie -> {
            cookie.setMaxAge(0);
            cookie.setValue("");
            cookie.setPath("/");
            response.addCookie(cookie);
        });
    }

    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setMaxAge(315360000);
        response.addCookie(cookie);
    }

    public static String getAuthToken(HttpServletRequest request) throws IllegalArgumentException {
        String result = getCookieValue(request, "AuthToken");

        if(result == null || result.isEmpty()) {
            throw new IllegalArgumentException("Unable to retrieve the AuthToken from the given Request");
        }

        return result;
    }

    public static String getDeviceToken(HttpServletRequest request) throws IllegalArgumentException {
        String result = getCookieValue(request, "DeviceToken");

        if(result == null || result.isEmpty()) {
            throw new IllegalArgumentException("Unable to retrieve the DeviceToken from the given Request");
        }

        return result;
    }
}
