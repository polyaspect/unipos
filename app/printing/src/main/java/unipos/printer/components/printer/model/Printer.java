package unipos.printer.components.printer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jssc.SerialPortException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.printer.model.ThermalPrinterLine;
import unipos.common.remote.printer.model.ThermalPrinterLineFormat;
import unipos.printer.components.shared.PrinterEscapeCodes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * Created by dominik on 09.09.15.
 */

@Document(collection = "printers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "connectionType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NetworkPrinter.class, name = "NETWORK"),
        @JsonSubTypes.Type(value = SerialPrinter.class, name = "SERIAL"),
        @JsonSubTypes.Type(value = UsbPrinter.class, name = "USB"),
})
public abstract class Printer {

    @Id
    protected String id;
    protected Long printerId; //Auto IncrementingId
    protected String name;
    protected String typeName;
    @JsonIgnore
    protected String logoPath;
    protected boolean defaultPrinter;
    protected int charCount;
    protected List<ThermalPrinterLine> formats = new ArrayList<>();
    protected List<String> stores = new ArrayList<>();
    protected String guid;


    public void sendMessage(String message, HorizontalAlignment alignment, int fontWidthScalar) throws IOException, SerialPortException {

        List<String> lines = new ArrayList<>();

        int correctCharCount = (int) Math.round((double) charCount / (double) fontWidthScalar);

        int pointer = 0;
        while (pointer < message.length()) {
            lines.add(message.substring(pointer, Math.min(pointer + correctCharCount, message.length())));
            pointer += correctCharCount;
        }

        switch (alignment) {
            case LEFT:
                for (String line : lines) {
                    sendMessage(line);
                }
                break;
            case CENTER:
                for (int i = 0; i < lines.size(); i++) {
                    if (i == lines.size() - 1) {
                        String string = "";
                        int paddingLeft = (int) Math.round((double) (charCount - lines.get(i).length() * fontWidthScalar) / ((double) 2 * (double) fontWidthScalar));
                        int paddingRight = correctCharCount - paddingLeft - lines.get(i).length();
                        for (int j = 0; j < paddingLeft; j++) {
                            string += " ";
                        }
                        string += lines.get(i);
                        for (int j = 0; j < paddingRight; j++) {
                            string += " ";
                        }
                        sendMessage(string);
                    } else {
                        sendMessage(lines.get(i));
                    }
                }
                break;
            case RIGHT:
                for (int i = 0; i < lines.size(); i++) {
                    if (i == lines.size() - 1) {
                        String string = "";
                        int paddingLeft = correctCharCount - lines.get(i).length() * fontWidthScalar;
                        for (int j = 0; j < paddingLeft; j++) {
                            string += " ";
                        }
                        string += lines.get(i);
                        sendMessage(string);
                    } else {
                        sendMessage(lines.get(i));
                    }
                }
                break;
        }
    }

    public void sendMessage(String message) throws IOException, SerialPortException {
        writeByteObjectOutputStream(message.getBytes("IBM437"));
    }

    public void sendMessage(byte[] message) throws IOException, SerialPortException {
        writeByteObjectOutputStream(message);
    }


    public void cutPaper() throws IOException, SerialPortException {
        writeByteObjectOutputStream((Character.toString((char) 27) + Character.toString((char) 105)).getBytes());
    }

    public void initPrinter() throws IOException, SerialPortException {
        writeByteObjectOutputStream((Character.toString((char) 27) + Character.toString((char) 64)).getBytes());
    }

    public void initLogo() throws IOException, SerialPortException {
        int white = Color.WHITE.getRGB();


        initPrinter();

        //If there's no Logo available stop trying to initialize it on the printer
        if (getLogoPath() == null || getLogoPath().isEmpty() || !new File(logoPath).exists()) {
            logoPath = this.getClass().getResource("/unipos.bmp").getFile();
        }
        File imageFile = new File(getLogoPath());
        BufferedImage img = null;
        try {
            img = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeByteObjectOutputStream((byte) 28, (byte) 113, (byte) 1);

        byte xL = (byte) Math.floor((double) img.getWidth() / 8.0);
        byte xH = 0;
        byte yL = (byte) Math.floor((double) img.getHeight() / 8.0);
        byte yH = 0;

        int width = xL * 8;
        int height = yL * 8;

        writeByteObjectOutputStream(xL, xH, yL, yH); //FS q (2/2)

        byte pixel = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y += 8) {
                pixel = 0;

                for (int offset = 0; offset < 8; offset++) {
                    if (img.getRGB(x, y + 7 - offset) != white) {
                        pixel |= (byte) Math.pow(2, offset);
                    }
                }
                writeByteObjectOutputStream((byte) pixel);
            }
        }

//        writeByteObjectOutputStream(objectOutputStream, (byte) 28, (byte) 112, (byte) 1, (byte) 0); //Print the image

//        writeByteObjectOutputStream(objectOutputStream, (byte) 28, (byte) 112, (byte) 002, (byte) 000);
//        writeByteObjectOutputStream(objectOutputStream, (byte) 29, (byte) 86, (byte) 66, (byte) 64);
        System.out.print("Printing Done");
    }

    public void initLogo(String picturePath) throws IOException, SerialPortException {
        int white = Color.WHITE.getRGB();


        initPrinter();
        File imageFile = new File(picturePath);
        BufferedImage img = null;
        try {
            img = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeByteObjectOutputStream((byte) 28, (byte) 113, (byte) 1);

        byte xL = (byte) Math.floor((double) img.getWidth() / 8.0);
        byte xH = 0;
        byte yL = (byte) Math.floor((double) img.getHeight() / 8.0);
        byte yH = 0;

        int width = xL * 8;
        int height = yL * 8;

        writeByteObjectOutputStream(xL, xH, yL, yH); //FS q (2/2)

        byte pixel = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y += 8) {
                pixel = 0;

                for (int offset = 0; offset < 8; offset++) {
                    if (img.getRGB(x, y + 7 - offset) != white) {
                        pixel |= (byte) Math.pow(2, offset);
                    }
                }
                writeByteObjectOutputStream((byte) pixel);
            }
        }

//        writeByteObjectOutputStream(objectOutputStream, (byte) 28, (byte) 112, (byte) 1, (byte) 0); //Print the image

//        writeByteObjectOutputStream(objectOutputStream, (byte) 28, (byte) 112, (byte) 002, (byte) 000);
//        writeByteObjectOutputStream(objectOutputStream, (byte) 29, (byte) 86, (byte) 66, (byte) 64);
        System.out.print("Printing Done");
    }

    public void initImage(String picturePath, int position) throws IOException, SerialPortException {
        int white = Color.WHITE.getRGB();


        initPrinter();
        File imageFile = new File(picturePath);
        BufferedImage img = null;
        try {
            img = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeByteObjectOutputStream((byte) 28, (byte) 113, (byte) position);

        byte xL = (byte) Math.floor((double) img.getWidth() / 8.0);
        byte xH = 0;
        byte yL = (byte) Math.floor((double) img.getHeight() / 8.0);
        byte yH = 0;

        int width = xL * 8;
        int height = yL * 8;

        writeByteObjectOutputStream(xL, xH, yL, yH); //FS q (2/2)

        byte pixel = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y += 8) {
                pixel = 0;

                for (int offset = 0; offset < 8; offset++) {
                    if (img.getRGB(x, y + 7 - offset) != white) {
                        pixel |= (byte) Math.pow(2, offset);
                    }
                }
                writeByteObjectOutputStream((byte) pixel);
            }
        }

//        writeByteObjectOutputStream(objectOutputStream, (byte) 28, (byte) 112, (byte) 1, (byte) 0); //Print the image

//        writeByteObjectOutputStream(objectOutputStream, (byte) 28, (byte) 112, (byte) 002, (byte) 000);
//        writeByteObjectOutputStream(objectOutputStream, (byte) 29, (byte) 86, (byte) 66, (byte) 64);
        System.out.print("Printing Done");
    }

    public void printLogo() throws IOException, SerialPortException {
        writeByteObjectOutputStream((Character.toString((char) 28) + (Character.toString((char) 112)) + (Character.toString((char) 1)) + (Character.toString((char) 0))).getBytes());
    }

    public void printImage(int position) throws IOException, SerialPortException {
        writeByteObjectOutputStream((Character.toString((char) 28) + (Character.toString((char) 112)) + (Character.toString((char) position)) + (Character.toString((char) 0))).getBytes());
    }

    public abstract void openConnection() throws IOException, SerialPortException;

    public abstract void closeConnection() throws IOException;

    protected abstract void writeByteObjectOutputStream(byte... bytes) throws IOException;

    public void sendEscapeSequence(ThermalPrinterLineFormat thermalPrinterLineFormat, boolean before) throws IOException {
        if (before) {
            writeByteObjectOutputStream(EscapeCodes.getEscapeCodeForLineFormatBefore(thermalPrinterLineFormat));
        } else {
            writeByteObjectOutputStream(EscapeCodes.getEscapeCodeForLineFormatAfter(thermalPrinterLineFormat));
        }
    }

    public void printQRCode(String message, int scale) throws IOException, SerialPortException {

        String qrCode;

        if (message != null) {
            String[] jwsHeaderPayloadSignature = message.split("\\.");

            qrCode = jwsHeaderPayloadSignature[1].replace("-", "+").replace("_", "/") + "_" + jwsHeaderPayloadSignature[2].replace("-", "+").replace("_", "/");
        } else {
            String error = "Sicherheitseinrichtung ausgefallen";
            byte[] errorBytes = error.getBytes("UTF-8");
            qrCode = Base64.getUrlEncoder().encodeToString(errorBytes);
        }
        int store_len = qrCode.length() + 3;
        byte store_pL = (byte) (store_len % 256);
        byte store_pH = (byte) (store_len / 256);

        // QR Code: Select the model
        //              Hex     1D      28      6B      04      00      31      41      n1(x32)     n2(x00) - size of model
        // set n1 [49 x31, model 1] [50 x32, model 2] [51 x33, micro qr code]
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=140
        byte[] modelQR = PrinterEscapeCodes.QR_MODEL;

        // QR Code: Set the size of module
        // Hex      1D      28      6B      03      00      31      43      n
        // n depends on the printer
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=141
        byte[] sizeQR = {(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x43, (byte) ((scale > 0) ? scale : 3)};

        //          Hex     1D      28      6B      03      00      31      45      n
        // Set n for error correction [48 x30 -> 7%] [49 x31-> 15%] [50 x32 -> 25%] [51 x33 -> 30%]
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=142
        byte[] errorQR = PrinterEscapeCodes.QR_ERROR_CORRECTION;

        // QR Code: Store the data in the symbol storage area
        // Hex      1D      28      6B      pL      pH      31      50      30      d1...dk
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=143
        //                        1D          28          6B         pL          pH  cn(49->x31) fn(80->x50) m(48->x30) d1…dk
        byte[] storeQR = {(byte) 0x1d, (byte) 0x28, (byte) 0x6b, store_pL, store_pH, (byte) 0x31, (byte) 0x50, (byte) 0x30};

        // QR Code: Print the symbol data in the symbol storage area
        // Hex      1D      28      6B      03      00      31      51      m
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=144
        byte[] printQR = PrinterEscapeCodes.QR_PRINT;

        // write() simply appends the data to the buffer
        writeByteObjectOutputStream(modelQR);
        writeByteObjectOutputStream(sizeQR);
        writeByteObjectOutputStream(errorQR);
        writeByteObjectOutputStream(storeQR);
        writeByteObjectOutputStream(qrCode.getBytes(StandardCharsets.US_ASCII));
        writeByteObjectOutputStream(PrinterEscapeCodes.HORIZONTAL_CENTER);
        writeByteObjectOutputStream(printQR);
        writeByteObjectOutputStream("\n\n\n\n\n\n\n".getBytes("IBM437"));

    }
}
