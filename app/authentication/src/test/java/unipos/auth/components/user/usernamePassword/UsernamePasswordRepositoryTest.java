package unipos.auth.components.user.usernamePassword;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPin;
import unipos.auth.test.config.MongoTestConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by Dominik on 01.06.2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MongoTestConfiguration.class})
public class UsernamePasswordRepositoryTest {

    @Autowired UsernamePasswordRepository usernamePasswordRepository;
    @Autowired UsernamePasswordFixture usernamePasswordFixture;


    @Before
    public void setUp() throws Exception {
        usernamePasswordFixture.setUp();
    }

    @After
    public void tearDown() throws Exception {
        usernamePasswordFixture.tearDown();

    }

    @Test
    public void testFindByUsernameAndPassword() throws Exception {
        assertThat(usernamePasswordRepository.findByUsernameAndPassword("Dominik", "asdf"), notNullValue());
        assertThat(usernamePasswordRepository.findByUsernameAndPassword("Dominik", "asdf").getUsername(), is("Dominik"));
        assertThat(usernamePasswordRepository.findByUsernameAndPassword("Dominik","asdf").getUser(), notNullValue());
    }

    @Test
    public void testFindByUsername() throws Exception {
        assertThat(usernamePasswordRepository.findByUsername("Dominik"), notNullValue());
        assertThat(usernamePasswordRepository.findByUsername("Joyce").getUsername(), is("Joyce"));
        assertThat(usernamePasswordRepository.findByUsername("asdfasdf"), is(nullValue()));
    }

    @Test
    public void testFindByUser() throws Exception {
        assertThat(usernamePasswordRepository.findByUser(usernamePasswordFixture.dominik).size(), is(1));

        UsernamePassword usernamePassword = new UsernamePassword("LOL", "LOL", usernamePasswordFixture.dominik);
        usernamePasswordRepository.save(usernamePassword);

        assertThat(usernamePasswordRepository.findByUser(usernamePasswordFixture.dominik).size(), is(2));
    }

    @Test
    public void testUserReference() throws Exception {
        assertThat(usernamePasswordRepository.findByUsername("Dominik").getUser(), notNullValue());
    }
}