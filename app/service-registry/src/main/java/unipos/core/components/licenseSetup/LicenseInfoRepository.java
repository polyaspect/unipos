package unipos.core.components.licenseSetup;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by domin on 13.02.2016.
 */
public interface LicenseInfoRepository extends MongoRepository<LicenseInfo, String> {
    LicenseInfo findByActivationCode(String activationCode);
}
