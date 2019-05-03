package unipos.licenseChecker.computerId;

import java.net.SocketException;

/**
 * Created by domin on 05.03.2016.
 */
public class ComputerIdCalculator {

    public static String calcComputerId() {
//        return "default";
//        if(OsDetector.isWindows()) {
//            return Hardware4Win.getSerialNumber();
//        }
        if(OsDetector.isUnix()) {
            try {
                return Hardware4Nix.getSerialNumber();
            } catch (SocketException ex) {
                return "default";
            }
        }
        return "default";
    }
}
