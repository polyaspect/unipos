package unipos.common.remote.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import unipos.common.container.UrlContainer;
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

@Service
public class AuthRemoteInterfaceImpl implements AuthRemoteInterface {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Token getAuthTokenByRequest(HttpServletRequest request) {
        Cookie authTokenCookie = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equalsIgnoreCase("authToken")) {
                authTokenCookie = cookie;
                break;
            }
        }
        Token token = restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.AUTH_GET_USERNAME_BY_AUTH_TOKEN + "/" + authTokenCookie.getValue(), Token.class);
        return token;
    }

    @Override
    public User findUserByAuthToken(String authTokenString) {
        Token token = restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.AUTH_GET_USERNAME_BY_AUTH_TOKEN + "/" + authTokenString, Token.class);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    @Override
    public Long getUserIdByAuthToken(String authToken) {
        Long userId = restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.AUTH_GET_USERID_BY_AUTH_TOKEN + "?authTokenString=" + authToken, Long.class);
        return userId;
    }

    @Override
    public User getUserByGuid(String userGuid) {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.AUTH_GET_USER_BY_GUID + "?userGuid=" + userGuid, User.class);
    }

    @Override
    public User getUserByUserId(Long userId) {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.AUTH_GET_USER_BY_USER_ID, User.class, userId);
    }


    @Override
    public String getCurrentVersion() {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.AUTH_CURRENT_VERSION, String.class);
    }
    @Override
    public User getUserByUsername(String username) {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.AUTH_GET_USER_BY_USERNAME + "?username=" + username, User.class);
    }
    @Override
    public User checkAccountCredentials(String username, String password) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
        mvm.add("username", username);
        mvm.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(mvm, headers);
        ResponseEntity<User> response = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.AUTH_CHECK_USERNAME_PASSWORD, HttpMethod.GET, request, User.class, username, password);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }

    @Override
    public Cookie accountLogin(String username, String password) throws UserPrincipalNotFoundException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(UrlContainer.BASEURL + UrlContainer.AUTH_ACCOUNT_LOGIN);
        builder.queryParam("username", username);
        builder.queryParam("password", password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.POST,
                requestEntity,
                String.class);
        List<String> headers = response.getHeaders().get("Set-Cookie");

        Cookie cookie = new Cookie("AuthToken", headers.stream().filter(s -> s.startsWith("AuthToken")).findFirst().orElseThrow(() -> new UserPrincipalNotFoundException("No User found for the given username and password")).replace("AuthToken=", "").split(";")[0]);
        cookie.setPath("/");
        cookie.setMaxAge(315360000);
        return cookie;
    }

    @Override
    public void addUsernamePasswordAuthMethod(UsernamePassword usernamePassword) {
        restTemplate.postForEntity(UrlContainer.BASEURL + UrlContainer.AUTH_ADD_USERNAME_PASSWORD_AUTH_METHOD, usernamePassword, Void.class);
    }

    public void addRights(List<Right> rightList) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        HttpEntity<List<Right>> request = new HttpEntity<>(rightList, headers);

        ResponseEntity responseEntity = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.AUTH_ADD_RIGHTS, HttpMethod.POST, request, Void.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RestClientException("An error occured during the RightPropagation!");
        }
    }
}
