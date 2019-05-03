package unipos.common.container;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by domin on 13.02.2016.
 */
public class FileContainer {

    public static File getPublicKeyFile() {
        File file = new File(System.getProperty("catalina.home")+"/webapps/public.key");
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static List<File> getLicenseFiles() {
        File directory = new File(System.getProperty("catalina.home")+"/licenses/");
        if(!directory.exists()) {
            directory.mkdirs();
        }

        return Arrays.asList(directory.listFiles());
    }

    public static File getSingleLicenseFile() {
        File directory = new File(System.getProperty("catalina.home")+"/licenses/");
        if(!directory.exists()) {
            directory.mkdirs();
        }

        List<File> files = Arrays.asList(directory.listFiles());
        if(files.size() == 1) {
            return files.get(0);
        }
        return null;
    }
}
