package unipos.auth.components.user.usernamePassword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinService;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * Created by Dominik on 03.06.2015.
 */

//ToDo: Review

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UsernamePasswordService usernamePasswordService;
    @Autowired
    LogRemoteInterface logRemoteInterface;

    ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)authentication;
        String username = String.valueOf(auth.getPrincipal());
        String password = String.valueOf(auth.getCredentials());

        UserDetails user = usernamePasswordService.loadUserByUsername(username);
        UsernamePassword usernamePassword = usernamePasswordService.findByUsername(username);

        String passwordHash = passwordEncoder.encodePassword(password, usernamePassword.getPasswordSalt());

        if(user != null && (user.getUsername().equals(username) && user.getPassword().equals(passwordHash))) {
            if(usernamePassword.isActive()) {
                return new UsernamePasswordAuthenticationToken(usernamePassword.getUser().getUserId(), usernamePassword.getPasswordHash(), user.getAuthorities());
            } else {
                throw new BadCredentialsException("User not activated");
            }
        } else {
            LogDto logDto = LogDto.builder()
                    .level(LogDto.Level.INFO)
                    .message("User tried to login with with the username '" + user.getUsername() +"' and failed")
                    .source(this.getClass().getName()+"#"+"authenticate")
                    .dateTime(LocalDateTime.now())
                    .build();
            logDto.addParameter("type", "InvalidCredentials");
            logDto.addParameter("username", user.getUsername());

            logRemoteInterface.log(logDto);
            throw new BadCredentialsException("User not found");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if(authentication == UsernamePasswordAuthenticationToken.class) {
            return true;
        } else {
            return false;
        }
//        return true;
    }
}
