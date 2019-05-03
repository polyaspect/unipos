package unipos.integritySafeGuard.apdu

import org.codehaus.jackson.util.ByteArrayBuilder
import unipos.integritySafeGuard.SmartCardUtils

import javax.smartcardio.CommandAPDU
import java.nio.ByteBuffer

/**
 * Created by domin on 28.12.2016.
 */
class ApduProvider {

    public static CommandAPDU getVerify(String pin) {
        def ba = SmartCardUtils.getFormat2PIN(pin)

        byte[] data = [0x00, 0x20, 0x00, 0x81, 0x08, ba[0], ba[1], ba[2], ba[3], ba[4], ba[5], ba[6], ba[7]] as byte[];
        CommandAPDU command = new CommandAPDU(data);

        new CommandAPDU(data)
    }

    public static CommandAPDU getSelectMasterFile() {
        return new CommandAPDU(0x00,0xA4,0x00,0x0C, [0x3F, 0x00] as byte[])
    }

    public static CommandAPDU getSelectDfSigApp() {
        return new CommandAPDU(0x00,0xA4,0x00,0x0C, [0xDF, 0x01] as byte[])
    }

    public static CommandAPDU getSelectEfCChDs() {
        return new CommandAPDU(0x00,0xA4,0x00,0x0C, [0xc0, 0x00] as byte[])
    }

    public static CommandAPDU getSignatureApdu(byte[] data) {
        new CommandAPDU(0x00 as byte, 0x2A as byte, 0x9E as byte, 0x9A as byte, data, 64);
    }
}
