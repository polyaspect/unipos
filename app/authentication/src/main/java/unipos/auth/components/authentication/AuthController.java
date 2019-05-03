package unipos.auth.components.authentication;

import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.auth.components.user.User;
import unipos.auth.components.user.UserService;
import unipos.common.container.RequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
// Created by Dominik on 30.05.2015.


@RestController

@Api(value = "/auth")
@RequestMapping(value = "/auth")
public class AuthController {

    TokenManagement tokenManagement = TokenManagement.getInstance();
    @Autowired
    UserService userService;
    /*@RequestMapping(value = "/pin_login", method = RequestMethod.POST)
    @ApiOperation(value="Login into the System")
    @ApiResponses(
            {@ApiResponse(code = 401, message = "Authentication Failed: Bad credentials"),
                    @ApiResponse(code = 200, message = "{SESSION-TOKEN}")}
    )
    public void pin_login(@PathVariable("mitarbeiterNr") String mitarbeiternr, @PathVariable("pin") String pin)
    {
        int i = 0;
    }*/

    /**
     * This Method converts the assigned AuthToken to an Username.
     *
     * @return the Username of the assigned AuthToken
     */
    @RequestMapping(value = "/getUserByAuthToken/{authTokenString}", method = RequestMethod.GET)
    public User getUserByAuthToken(@PathVariable("authTokenString") String authTokenString, HttpServletResponse response) {
        Token authToken = null;

        authToken = tokenManagement.getTokenByTokenString(authTokenString);

        if (authToken == null) {
            return null;
        }
        return authToken.getUser();
    }

    @RequestMapping(value = "/getUsernameByAuthToken/{authTokenString}", method = RequestMethod.GET)
    public Token getUsernameByAuthToken(@PathVariable("authTokenString") String authTokenString, HttpServletResponse response) {
        Token authToken = null;

        authToken = tokenManagement.getTokenByTokenString(authTokenString);

        if (authToken == null) {
            return null;
        }
        return authToken;
    }

    @RequestMapping(value = "/getUserIdByAuthToken", method = RequestMethod.GET)
    public Long getPrincipalValueByAuthToken(@RequestParam String authTokenString) {
        Token authToken = null;

        authToken = tokenManagement.getTokenByTokenString(authTokenString);
        if (authToken == null || authToken.getUser() == null) {
            return -1L;
        }

        if (authToken.getUser() != null && authToken.getUser().getUserId() != null) {
            return authToken.getUser().getUserId();
        } else {
            return -1L;
        }
    }

    @RequestMapping(value = "/getUserId", method = RequestMethod.GET)
    public Long getPrincipalValueByAuthToken(HttpServletRequest request) {
        String authTokenString;
        try {
            authTokenString = RequestHandler.getAuthToken(request);
        } catch (IllegalArgumentException e) {
            return -1L;
        }

        Token authToken = null;

        authToken = tokenManagement.getTokenByTokenString(authTokenString);
        if (authToken == null || authToken.getUser() == null) {
            return -1L;
        }
        return authToken.getUser().getUserId();
    }

    @RequestMapping(value = "/checkAuthToken", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void checkDeviceToken(HttpServletRequest request, HttpServletResponse response) {
        String authToken = RequestHandler.getCookieValue(request, "AuthToken");
        if (tokenManagement.getTokenByTokenString(authToken) == null) {
            RequestHandler.deleteCookie(request, response, "AuthToken");
        }
    }

    @RequestMapping(value = "/getCompanyGuidByAuthToken", method = RequestMethod.GET)
    public String getCompanyGuidByAuthToken(HttpServletRequest request) {
        String authToken = RequestHandler.getCookieValue(request, "AuthToken");
        Token token = tokenManagement.getTokenByTokenString(authToken);

        if (token == null || token.getUser() == null) {
            return null;
        } else {
            return token.getUser().getCompanyGuid();
        }
    }

    @RequestMapping(value = "/getUserByUserGuid", method = RequestMethod.GET)
    public User getUserByUserGuid(@RequestParam("userGuid") String guid) {
        return userService.findUserByGuid(guid);
    }

    @RequestMapping(value = "/getUserByUsername", method = RequestMethod.GET)
    public User getUser(@RequestParam("username") String username) {
        return userService.findUserByUsername(username);
    }

    @RequestMapping(value = "/getByUserId/{userId}", method = RequestMethod.GET)
    public User getUserByUserId(@PathVariable("userId") Long userId) {
        return userService.findUserByUserId(userId);
    }

    @RequestMapping(value = "/isLoggedIn", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response) {
        String authToken = RequestHandler.getCookieValue(request, "AuthToken");
        if (tokenManagement.getTokenByTokenString(authToken) == null) {
            RequestHandler.deleteCookie(request, response, "AuthToken");
            return false;
        }
        else{
            return true;
        }
    }

    /*@RequestMapping(value = "/account_login", method = RequestMethod.POST)
    @ApiOperation(value="Login into the System")
    @ApiResponses(
            {@ApiResponse(code = 401, message = "Authentication Failed: Bad credentials"),
                    @ApiResponse(code = 200, message = "{SESSION-TOKEN}")}
    )
    public void pin_account(@PathVariable("username") String username, @PathVariable("password") String password)
    {
        int i = 0;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ApiOperation(value = "Logout the user of the system")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful logout of the System"),
            @ApiResponse(code = 418, message = "Logout not Successfull (Because of any reason")
    })
    public void logout(String sessionToken) {
        int i = 0;
    }*/
}
