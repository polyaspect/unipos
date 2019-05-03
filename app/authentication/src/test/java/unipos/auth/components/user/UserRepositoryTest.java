package unipos.auth.components.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import unipos.auth.test.config.MongoTestConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MongoTestConfiguration.class})
public class UserRepositoryTest {

    @Autowired
    UserFixture userFixture;

    @Autowired
    UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        userFixture.setUp();
    }

    @After
    public void tearDown() throws Exception {
        userFixture.tearDown();
    }

    @Test
    public void testFindByUsernameAndPassword() throws Exception {
        assertNotNull(userRepository.findByNameAndSurname("dominik", "1234"));
        assertNull(userRepository.findByNameAndSurname("wrong", "wrong"));
    }
}