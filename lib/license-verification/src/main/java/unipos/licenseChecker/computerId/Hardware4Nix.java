package unipos.licenseChecker.computerId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

public class Hardware4Nix {

    private static String sn = null;

    public static final String getSerialNumber() throws SocketException {

        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        byte[] address = null;

        while (enumeration.hasMoreElements()) {
            address = enumeration.nextElement().getHardwareAddress();
            if (address != null && address.length > 0) {
                break;
            }
        }

        if (address == null) {
            return "default";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < address.length; i++) {
            sb.append(String.format("%02X%s", address[i], (i < address.length - 1) ? "-" : ""));
        }

        return sb.toString();

//        if (sn == null) {
//            readDmidecode();
//        }
//        if (sn == null) {
//            readCpuInfo();
//        }
//        if (sn == null) {
//            readLshal();
//        }
//        if (sn == null) {
//            return "default";
//        }

//        return sn;
    }

    private static BufferedReader read(String command) {

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(command.split(" "));
        } catch (IOException e) {
            return null;
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            return null;
        }

        return new BufferedReader(new InputStreamReader(is));
    }

    private static void readDmidecode() {

        String line = null;
        String marker = "Serial Number:";
        BufferedReader br = null;

        try {
            br = read("dmidecode -t system");
            while ((line = br.readLine()) != null) {
                if (line.indexOf(marker) != -1) {
                    sn = line.split(marker)[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void readCpuInfo() {

        String line = null;
        String marker = "Serial";
        BufferedReader br = null;

        try {
            br = read("cat /proc/cpuinfo");
            while ((line = br.readLine()) != null) {
                if (line.indexOf(marker) != -1) {
                    sn = line.split(marker)[1].replaceAll("\\(string\\)|(\\')", "").trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void readLshal() {

        String line = null;
        String marker = "system.hardware.serial =";
        BufferedReader br = null;

        try {
            br = read("lshal");
            while ((line = br.readLine()) != null) {
                if (line.indexOf(marker) != -1) {
                    sn = line.split(marker)[1].replaceAll("\\(string\\)|(\\')", "").trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
