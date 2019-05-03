package unipos.signature.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import unipos.integritySafeGuard.SmartCardUtils;
import unipos.integritySafeGuard.config.AppConfig;
import unipos.integritySafeGuard.domain.RksSuite;
import unipos.integritySafeGuard.smartcard.SmartCardHandler;
import unipos.signature.components.signatureOption.SignatureHolder;
import unipos.signature.components.signatureOption.SignatureOption;

/**
 * Created by domin on 04.11.2016.
 */
@Configuration
@Import(AppConfig.class)
public class SignatureConfig {

    @Bean
    public SignatureHolder signatureHolder() {
        SignatureHolder signatureHolder = new SignatureHolder();

        SignatureOption signatureOption = SignatureOption.builder()
                .turnOverCounterLengthInBytes(5)
                .signatureDeviceAvailable(true)
                .kassaId("1")
                .crtSerialNo("MeineRKSSerialNo")
                .rksSuite(RksSuite.R1_AT2)
                .secretKey(SmartCardUtils.getSecretKeyByCustomPw("MeinPW"))
                .secretKeyPlainText("MeinPW")
                .storeGuid("MeinStore")
                .build();

        signatureHolder.signatureOptionList.add(signatureOption);
        return signatureHolder;

//        return new SignatureHolder();
    }

    @Bean
    public SmartCardHandler smartCardHandler() {
        return new SmartCardHandler();
    }
}
