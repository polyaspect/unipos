package unipos.common.container;

import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Dominik on 10.01.2016.
 */
public class RequestHandlerTest {

    @Test
    public void testGetCookeValue1() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[] {new Cookie("Test", "ASDF"),new Cookie("lol", "lol")});

        assertThat(RequestHandler.getCookieValue(request, "Test"),is("ASDF"));
        assertThat(RequestHandler.getCookieValue(request, "lol"),is("lol"));
    }

    @Test
    public void testGetCookeValue2() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[] {new Cookie("Test", "ASDF")});

        assertThat(RequestHandler.getCookieValue(request, "LOL"),is(nullValue()));
    }

    @Test
    public void testGetCookeValue3() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        assertThat(RequestHandler.getCookieValue(request, "LOL"),is(nullValue()));
    }
}