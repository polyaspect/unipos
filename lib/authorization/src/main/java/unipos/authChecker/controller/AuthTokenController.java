package unipos.authChecker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import unipos.authChecker.domain.AuthToken;
import unipos.authChecker.domain.AuthTokenManager;
import unipos.authChecker.domain.Permission;

import java.util.Arrays;

/**
 * Created by Dominik on 25.06.2015.
 */
@RestController
@RequestMapping("/authToken")
public class AuthTokenController {

    @Autowired
    AuthTokenManager tokenManager;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addToken(@RequestBody AuthToken token) {
        tokenManager.add(token);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void revokeToken(@RequestParam String token) {
        tokenManager.remove(token);
    }
}
