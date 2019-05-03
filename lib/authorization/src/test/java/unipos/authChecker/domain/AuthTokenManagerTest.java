package unipos.authChecker.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import unipos.authChecker.config.Configuration;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Dominik on 24.06.2015.
 */
public class AuthTokenManagerTest {

    AuthTokenManager tokenManager;
    AuthToken authToken;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        tokenManager = new AuthTokenManager();


        //Create some Permissions
        Permission permission1 = new Permission("pos.entity.delete", true);
        Permission permission2 = new Permission("pos.entity.remove", true);
        Permission permission3 = new Permission("pos.entity.edit", true);

        //Create a valid AuthToken
        authToken = new AuthToken("3AKENDNFZ5FJFNTGJ5DDZG", Arrays.asList(permission1,permission2,permission3));
    }

    @After
    public void tearDown() throws Exception {

        //Explizit delete the Instances
        tokenManager = null;
        authToken = null;

    }

    @Test
    public void testAdd() throws Exception {
        tokenManager.add(authToken);

        assertThat(tokenManager.tokens.size(), is(1));
        assertThat(tokenManager.tokens.get(0).getAuthToken(), is("3AKENDNFZ5FJFNTGJ5DDZG"));
        assertThat(tokenManager.tokens.get(0).getPermissions().size(), is(3));
    }

    @Test
    public void testRemoveByInstance() throws Exception {
        tokenManager.add(authToken);
        assertThat(tokenManager.tokens.size(), is(1));

        tokenManager.remove(authToken);
        assertThat(tokenManager.tokens.size(), is(0));
    }

    @Test
    public void testRemoveByTokenstring() throws Exception {
        tokenManager.add(authToken);
        assertThat(tokenManager.tokens.size(), is(1));

        tokenManager.remove("3AKENDNFZ5FJFNTGJ5DDZG");
        assertThat(tokenManager.tokens.size(), is(0));
    }

    @Test
    public void testGetAllTokens() throws Exception {
        tokenManager.add(authToken);
        assertThat(tokenManager.tokens.size(), is(1));

        assertThat(tokenManager.getAllTokens().size(), is(1));
        tokenManager.remove(authToken);
        assertThat(tokenManager.getAllTokens().size(), is(0));
    }

    @Test
    public void testRemoveAll() throws Exception {
        tokenManager.add(authToken);
        assertThat(tokenManager.tokens.size(), is(1));

        int count = tokenManager.removeAll();
        assertThat(count, is(1));
        assertThat(tokenManager.tokens.size(), is(0));
    }
}