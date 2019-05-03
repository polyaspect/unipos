package unipos.core.components.update;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * Created by domin on 07.03.2016.
 */
public interface UpdateRepository {

    /**
     * Asks the Update Server if there's  a new Version available for my machine. It does this by sending the license-file
     * to the server. The server validates the file, and answers with a new license-file, if there's a new Version of any module available.
     * This method takes the new license-file and persists it somewhere on the tomcat-instance.
     *
     * @param updateLicenseFile
     * @return a List of the updated Module Names
     */
    Map<String, String> checkForNewLicenseFile(boolean updateLicenseFile) throws IOException, GeneralSecurityException;
}

