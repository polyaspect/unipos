package unipos.auth.components.user.mitarbeiteridPin;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by Dominik on 03.06.2015.
 */
public class MitarbeiteridPinAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public MitarbeiteridPinAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public MitarbeiteridPinAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
