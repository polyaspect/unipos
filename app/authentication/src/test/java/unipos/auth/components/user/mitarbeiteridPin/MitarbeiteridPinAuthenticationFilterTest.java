package unipos.auth.components.user.mitarbeiteridPin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Dominik on 04.06.2015.
 */
public class MitarbeiteridPinAuthenticationFilterTest {

    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;
    @Mock AuthenticationManager authenticationManager;

    @InjectMocks
    MitarbeiteridPinAuthenticationFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new MitarbeiteridPinAuthenticationFilter();
        MockitoAnnotations.initMocks(this);

        Whitebox.setInternalState(filter, "authenticationManager", authenticationManager);
        when(request.getMethod()).thenReturn("POST");
    }

    @Test
    public void testAttemptAuthenticationSithValidCredentials() throws Exception {
        setMitarbeiternrAndPassword("1234","4321");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(new MitarbeiteridPinAuthenticationToken("1234", "4321"));

        assertThat(filter.attemptAuthentication(request, response).getPrincipal(), is("1234"));
        assertThat(filter.attemptAuthentication(request, response).getCredentials(), is("4321"));
    }

    @Test(expected = BadCredentialsException.class)
    public void testAttemptAuthenticationWithMitarbeiterNrAsString() throws Exception {
        setMitarbeiternrAndPassword("asdf", "4321");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(new MitarbeiteridPinAuthenticationToken("1234", "4321"));
    }

    @Test(expected = BadCredentialsException.class)
    public void testAttemptAuthenticationWithPinAsString() throws Exception {
        setMitarbeiternrAndPassword("1234", "asdf");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(new MitarbeiteridPinAuthenticationToken("1234", "4321"));

        filter.attemptAuthentication(request,response);
    }

    @Test(expected = BadCredentialsException.class)
    public void testAttemptAuthenticationWithMitarbeiterNrAndPinAsString() throws Exception {
        setMitarbeiternrAndPassword("asdf", "asdf");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(new MitarbeiteridPinAuthenticationToken("1234", "4321"));

        filter.attemptAuthentication(request,response);
    }

    @Test(expected = AuthenticationServiceException.class)
    public void testAttemptAuthenticationWithGet() throws Exception {
        when(request.getMethod()).thenReturn("GET");

        filter.attemptAuthentication(request,response);
    }

    @Test
    public void testSetPostOnly() throws Exception {
        filter.setPostOnly(false);

        assertThat(Whitebox.getInternalState(filter, "postOnly"), is(false));
    }

    @Test
    public void testSetMitarbeitertidParameter() throws Exception {
        filter.setMitarbeiteridParameter("asdf");

        assertThat(filter.getMitarbeiteridParameter(), is("asdf"));
    }

    @Test
    public void testsetPinParameter() throws Exception {
        filter.setPinParameter("qwer");

        assertThat(filter.getPinParameter(), is("qwer"));
    }

    private void setMitarbeiternrAndPassword(String mitarbeiternr, String pin) {
        when(request.getParameter(MitarbeiteridPinAuthenticationFilter.SPRING_SECURITY_FORM_MITARBEITERNR_KEY)).thenReturn(mitarbeiternr);
        when(request.getParameter(MitarbeiteridPinAuthenticationFilter.SPRING_SECURITY_FORM_PIN_KEY)).thenReturn(pin);

        filter.attemptAuthentication(request, response);
    }
}