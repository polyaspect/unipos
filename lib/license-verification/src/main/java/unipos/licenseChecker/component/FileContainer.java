package unipos.licenseChecker.component;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by domin on 13.02.2016.
 */
public class FileContainer {

    public static File getPublicKeyFile() {
        File file = new File(System.getProperty("catalina.home") + "/webapps/public.key");
        if (!file.exists()) {
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
        File directory = new File(System.getProperty("catalina.home") + "/licenses/");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return Arrays.asList(directory.listFiles());
    }

    public static File getSingleLicenseFile() {
        File directory = new File(System.getProperty("catalina.home") + "/licenses/");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        List<File> files = Arrays.asList(directory.listFiles());
        if (files.size() == 1) {
            return files.get(0);
        }
        return null;
    }

    public static File getLicenseDirectory() {
        File directory = new File(System.getProperty("catalina.home") + "/licenses/");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return directory;
    }

    public static File getWarTempDirectory() {
        File directory = new File(System.getProperty("catalina.home") + "/tempWar/");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return directory;
    }

    public static File createNewTempWarFile(String moduleName, String version) {
        File tempWarDirectory = getWarTempDirectory();

        File tempWarFile = new File(tempWarDirectory.getAbsolutePath() + "/tempWar_" + moduleName + "v" + version);
        if (!tempWarFile.exists()) {
            try {
                tempWarFile.createNewFile();
            } catch (IOException e) {

            }
        }
        return tempWarFile;
    }

    public static void deleteTempWarFiles() {
        File tempWarDirectory = getWarTempDirectory();
        Arrays.stream(tempWarDirectory.listFiles()).filter(File::isFile).forEach(File::delete);
    }

    public static File createNewDeployableWarFile(String moduleName) {
        File deplyableWarDirecotry = getDeployableWarDirectory();

        File tempWarFile = new File(deplyableWarDirecotry.getAbsolutePath() + "/" + moduleName + ".war");
        if (!tempWarFile.exists()) {
            try {
                tempWarFile.createNewFile();
            } catch (IOException e) {

            }
        }
        return tempWarFile;
    }

    public static File getDeployableWarDirectory() {
        File directory = new File(System.getProperty("catalina.home") + "/tempWar/deployableWars/");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return directory;
    }

    public static File createNewLicenseFile() {
        File licenseDirectory = getLicenseDirectory();

        int licenseCount = Arrays.stream(licenseDirectory.listFiles()).mapToInt(f -> Integer.valueOf(f.getName().split("unipos.license.")[1])).max().orElse(0);
        licenseCount += 1;
//        int licenseCount = licenseDirectory.listFiles().length + 1;
        File license = new File(getLicenseDirectory().getAbsolutePath() + "/unipos.license." + String.valueOf(licenseCount));

        if (!license.exists()) {
            try {
                license.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return license;
    }

    public static File getWebappsDirectory() {
        File directory = new File(System.getProperty("catalina.home") + "/webapps");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return directory;
    }

    public static boolean isWarDeployed(String moduleName) {
        File deployedWarDirectory = getWebappsDirectory();

        moduleName = moduleName.replace("unipos-", "");

        if (!deployedWarDirectory.exists() || !deployedWarDirectory.isDirectory() || deployedWarDirectory.listFiles().length == 0) {
            return false;
        }

        final String finalModuleName = moduleName;
        return Arrays.stream(deployedWarDirectory.listFiles()).anyMatch(file -> {
                    String baseName = FilenameUtils.getBaseName(file.getAbsolutePath()).replace("unipos-", "");
                    String extension = FilenameUtils.getExtension(file.getAbsolutePath());

                    return (baseName.equals(finalModuleName) && extension.equals("war") && file.isFile()) || baseName.equals(finalModuleName) && file.isDirectory();
                }
        );


    }

    public static File getLibraryFolder() {
        File directory = new File(System.getProperty("catalina.home") + "/lib");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return directory;
    }

    public static File getUpdaterJar() throws IOException {
        File libDirectory = getLibraryFolder();

        File updater = new File(libDirectory.getAbsolutePath(), "updater.jar");
        if(updater.exists()) {
            return updater;
        }
        updater.createNewFile();
        return updater;
    }
}
