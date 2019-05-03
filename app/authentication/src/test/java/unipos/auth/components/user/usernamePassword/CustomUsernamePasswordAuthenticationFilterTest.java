package unipos.auth.components.user.usernamePassword;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Dominik on 18.06.2015.
 */
public class CustomUsernamePasswordAuthenticationFilterTest {

    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;
    @Mock AuthenticationManager authenticationManager;

    @InjectMocks
    UsernamePasswordAuthenticationFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new UsernamePasswordAuthenticationFilter();
        MockitoAnnotations.initMocks(this);

        Whitebox.setInternalState(filter, "authenticationManager", authenticationManager);
        when(request.getMethod()).thenReturn("POST");
    }

    @Test
    public void testAttemptAuthenticationIsValid() throws Exception {
        setUsernameAndPassword("Dominik", "asdf");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(
                new UsernamePasswordAuthenticationToken("Dominik", "asdf")
        );
        assertThat(filter.attemptAuthentication(request,response).getPrincipal(), is("Dominik"));
        assertThat(filter.attemptAuthentication(request,response).getCredentials(), is("asdf"));
    }

    @Test(expected = AuthenticationServiceException.class)
    public void testAttemptAuthenticationWithGet() throws Exception {
        when(request.getMethod()).thenReturn("GET");

        filter.attemptAuthentication(request,response);
    }

    private void setUsernameAndPassword(String username, String password) {
        when(request.getParameter(CustomUsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)).thenReturn(username);
        when(request.getParameter(CustomUsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)).thenReturn(password);

        filter.attemptAuthentication(request, response);
    }
}