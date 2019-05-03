package unipos.printer.components.printer.model;

import unipos.common.remote.printer.model.ThermalPrinterLineFormat;

/**
 * Created by Thomas on 17.01.2016.
 */
public class EscapeCodes {
    public static byte[] getEscapeCodeForLineFormatBefore(ThermalPrinterLineFormat thermalPrinterLineFormat) {
        if (thermalPrinterLineFormat == ThermalPrinterLineFormat.DOUBLE_HEIGHT) {
            return new byte[]{(byte) 27, (byte) 33, (byte) 16};
        } else if (thermalPrinterLineFormat == ThermalPrinterLineFormat.DOUBLE_WIDTH) {
            return new byte[]{(byte) 27, (byte) 33, (byte) 32};
        }
        return new byte[0];
    }

    public static byte[] getEscapeCodeForLineFormatAfter(ThermalPrinterLineFormat thermalPrinterLineFormat) {
        if (thermalPrinterLineFormat == ThermalPrinterLineFormat.DOUBLE_HEIGHT) {
            return new byte[]{(byte) 27, (byte) 33, (byte) 0};
        } else if (thermalPrinterLineFormat == ThermalPrinterLineFormat.DOUBLE_WIDTH) {
            return new byte[]{(byte) 27, (byte) 33, (byte) 0};
        }
        return new byte[0];
    }
}