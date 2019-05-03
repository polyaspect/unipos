package unipos.licenseChecker.computerId;

/**
 * Created by domin on 05.03.2016.
 */
public class OsDetector {

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 );
    }

    public static boolean isSolaris() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("sunos") >= 0);
    }
}
