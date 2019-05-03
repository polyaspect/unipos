package unipos.auth.components.user.mitarbeiteridPin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import unipos.auth.components.user.User;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;

import java.time.LocalDateTime;

/**
 * Created by Dominik on 03.06.2015.
 */

@Component
public class MitarbeiteridPinAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    MitarbeiteridPinService mitarbeiteridPinService;
    @Autowired
    LogRemoteInterface logRemoteInterface;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MitarbeiteridPinAuthenticationToken auth = (MitarbeiteridPinAuthenticationToken)authentication;
        String username = String.valueOf(auth.getPrincipal());
        String password = String.valueOf(auth.getCredentials());

        UserDetails user = mitarbeiteridPinService.loadUserByUsername(username);
        if(user != null && (user.getUsername().equals(username) && user.getPassword().equals(password))) {
            MitarbeiteridPin mitarbeiteridPin = mitarbeiteridPinService.findByMitarbeiterNrAndPin(Integer.parseInt(user.getUsername()), Integer.parseInt(user.getPassword()));
            if(mitarbeiteridPin.isActive()) {
                return new MitarbeiteridPinAuthenticationToken(mitarbeiteridPin.getUser().getUserId(), user.getPassword(), user.getAuthorities());
            } else {
                throw new BadCredentialsException("Mitarbeiter-Nummer not activated");
            }
        } else {
            LogDto logDto = LogDto.builder()
                    .level(LogDto.Level.INFO)
                    .message("User tried to login with the MitarbeiterId '" + user.getUsername() +"' and failed")
                    .source(this.getClass().getName()+"#"+"authenticate")
                    .dateTime(LocalDateTime.now())
                    .build();
            logDto.addParameter("type", "InvalidCredentials");
            logDto.addParameter("mitarbeiterId", user.getUsername());

            logRemoteInterface.log(logDto);
            throw new BadCredentialsException("User not found");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if(authentication == MitarbeiteridPinAuthenticationToken.class) {
            return true;
        } else {
            return false;
        }
//        return true;
    }
}
