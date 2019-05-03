package unipos.licenseChecker.component;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.common.remote.core.CoreRemoteInterface;
import unipos.licenseChecker.testConfig.TestLicenseCheckerConfig;

import java.io.File;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Dominik on 06.02.2016.
 */

@ContextConfiguration(classes = TestLicenseCheckerConfig.class)
public class LicenseCheckerTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    LicenseChecker licenseChecker;
    @Autowired
    CoreRemoteInterface coreRemoteInterface;

    @Test
    public void testLicenseChecker() throws Exception {
    }
}