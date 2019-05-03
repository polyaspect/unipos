//package unipos.auth.components.authentication;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import unipos.auth.config.MongoConfiguration;
//import unipos.auth.config.web.SecurityConfiguration;
//import unipos.auth.config.web.WebConfiguration;
//
//import static org.hamcrest.core.Is.is;
//import static org.junit.Assert.*;
//
//public class TokenManagementTest {
//
//    TokenManagement tokenManagement;
//
//    @Before
//    public void setUp() throws Exception {
//        tokenManagement = TokenManagement.getInstance();
//
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        tokenManagement.removeAllTokens();
//    }
//
//    @Test
//    public void testAddToken() throws Exception {
//        Token token = new Token("testSessionToken");
//        tokenManagement.addToken(token);
//
//        assertThat(tokenManagement.getTokens().size(), is(1));
//
//        Token token2 = new Token("MeinSessionToken");
//        tokenManagement.addToken(token2);
//
//        assertThat(tokenManagement.getTokens().size(), is(2));
//    }
//
//    @Test
//    public void testRemoveToken() throws Exception {
//        assertThat(tokenManagement.getTokens().size(), is(0));
//
//        Token token = new Token("testSessionToken");
//        tokenManagement.addToken(token);
//
//        assertThat(tokenManagement.getTokens().size(), is(1));
//
//        tokenManagement.removeToken("JSESSIONID");
//        assertThat(tokenManagement.getTokens().size(), is(0));
//
//        Token token2 = new Token("testSessionToken2");
//        tokenManagement.addToken("JSESSIONID2",token2);
//
//        assertThat(tokenManagement.getTokens().size(), is(1));
//
//        tokenManagement.removeToken("JSESSIONID2", token2);
//        assertThat(tokenManagement.getTokens().size(), is(0));
//    }
//
//    @Test
//    public void testIsNew() throws Exception {
//        assertThat(tokenManagement.getTokens().size(), is(0));
//
//        Token token = new Token("testSessionToken");
//
//        assertThat(tokenManagement.isNew(token), is(true));
//        assertThat(tokenManagement.isNew("JSESSIONID"), is(true));
//
//        tokenManagement.addToken("JSESSIONID",token);
//
//        assertThat(tokenManagement.getTokens().size(), is(1));
//
//        assertThat(tokenManagement.isNew(token), is(false));
//        assertThat(tokenManagement.isNew("JSESSIONID"), is(false));
//    }
//
//    @Test
//    public void testRemoveAllTokens() throws Exception {
//        Token token = new Token("testSessionToken");
//        Token token2 = new Token("testSessionToken2");
//
//        tokenManagement.addToken("JSESSIONID", token);
//        tokenManagement.addToken("JSESSIONID2", token2);
//
//        assertThat(tokenManagement.getTokens().size(), is(2));
//
//        tokenManagement.removeAllTokens();
//
//        assertThat(tokenManagement.getTokens().size(), is(0));
//    }
//}