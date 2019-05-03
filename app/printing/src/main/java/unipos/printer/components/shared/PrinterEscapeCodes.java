package unipos.printer.components.shared;

/**
 * Created by Thomas on 21.09.2016.
 */
public class PrinterEscapeCodes {

    public static final byte[] HORIZONTAL_CENTER = new byte[]{(byte) 27, (byte) 97, (byte) 1};

    //static escape codes for QR-Code generation
    public static final byte[] QR_MODEL = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x04, (byte) 0x00, (byte) 0x31, (byte) 0x41, (byte) 0x32, (byte) 0x00};
    public static final byte[] QR_ERROR_CORRECTION = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x45, (byte) 0x31};
    public static final byte[] QR_PRINT = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x51, (byte) 0x30};

}
