package unipos.signature.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import unipos.common.config.SpringConfiguration;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.integritySafeGuard.smartcard.SmartCardHandler;
import unipos.signature.components.dep.DepService;
import unipos.signature.components.signature.SignatureService;
import unipos.signature.components.signatureOption.SignatureOptionService;
import unipos.signature.components.umsatzZaehler.UmsatzZaehlerService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author ggradnig
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"unipos.signature.components"}, excludeFilters = {
        @ComponentScan.Filter(type= FilterType.ANNOTATION, value=Service.class)})
@Import(SpringConfiguration.class)
public class WebTestConfiguration extends WebMvcConfigurationSupport {

    @Bean
    public AuthRemoteInterface authRemoteInterface(){
        return mock(AuthRemoteInterface.class);
    }

    @Bean
    public SocketRemoteInterface socketRemoteInterface(){
        return mock(SocketRemoteInterface.class);
    }

    @Bean
    public PosRemoteInterface posRemoteInterface(){
        return mock(PosRemoteInterface.class);
    }

    @Bean
    public DataRemoteInterface dateRemoteInterface() {
        return mock(DataRemoteInterface.class);
    }

    @Bean
    public LogRemoteInterface logRemoteInterface() {
        LogRemoteInterface logRemoteInterface = mock(LogRemoteInterface.class);
        when(logRemoteInterface.log(any())).thenReturn(1);
        return mock(LogRemoteInterface.class);
    }

    //Mocks for Rest Testing
    @Bean
    public UmsatzZaehlerService umsatzZaehlerService() {
        return mock(UmsatzZaehlerService.class);
    }

    @Bean
    public SignatureOptionService signatureOptionService() {
        return mock(SignatureOptionService.class);
    }

    @Bean
    public SignatureService signatureService() {
        return mock(SignatureService.class);
    }

    @Bean
    public DepService depService() {
        return mock(DepService.class);
    }

    @Bean
    public SmartCardHandler smartCardHandler() {
        return mock(SmartCardHandler.class);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
