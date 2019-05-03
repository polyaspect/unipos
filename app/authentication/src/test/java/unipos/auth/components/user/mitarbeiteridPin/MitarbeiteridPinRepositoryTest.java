package unipos.auth.components.user.mitarbeiteridPin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import unipos.auth.components.user.usernamePassword.UsernamePasswordFixture;
import unipos.auth.components.user.usernamePassword.UsernamePasswordRepository;
import unipos.auth.test.config.MongoTestConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by Dominik on 02.06.2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MongoTestConfiguration.class})
public class MitarbeiteridPinRepositoryTest {

    @Autowired
    MitarbeiteridPinRepository mitarbeiteridPinRepository;
    @Autowired
    MitarbeiteridPinFixture mitarbeiteridPinFixture;


    @Before
    public void setUp() throws Exception {
        mitarbeiteridPinFixture.setUp();
    }

    @After
    public void tearDown() throws Exception {
        mitarbeiteridPinFixture.tearDown();

    }

    @Test
    public void testFindByMitarbeiteridAndPin() throws Exception {
        assertThat(mitarbeiteridPinRepository.findByMitarbeiteridAndPin(1234,4321), notNullValue());
        assertThat(mitarbeiteridPinRepository.findByMitarbeiteridAndPin(1234,4321).getMitarbeiterid(), is(1234));
        assertThat(mitarbeiteridPinRepository.findByMitarbeiteridAndPin(1234,4321).getPin(), is(4321));
    }

    @Test
    public void testFindByMitarbeiterid() throws Exception {
        assertThat(mitarbeiteridPinRepository.findByMitarbeiterid(1234), notNullValue());
        assertThat(mitarbeiteridPinRepository.findByMitarbeiterid(1234).getMitarbeiterid(), is(1234));
        assertThat(mitarbeiteridPinRepository.findByMitarbeiterid(1234).getPin(), is(4321));
    }

    @Test
    public void testUserReference() throws Exception {
        assertThat(mitarbeiteridPinRepository.findByMitarbeiterid(1234).getUser(), notNullValue());
    }

    @Test
    public void testFindByUser() throws Exception {
        assertThat(mitarbeiteridPinRepository.findByUser(mitarbeiteridPinFixture.dominik).size(), is(1));

        MitarbeiteridPin mitarbeiteridPin = new MitarbeiteridPin(666, 666, mitarbeiteridPinFixture.dominik);
        mitarbeiteridPinRepository.save(mitarbeiteridPin);

        assertThat(mitarbeiteridPinRepository.findByUser(mitarbeiteridPinFixture.dominik).size(), is(2));
    }
}