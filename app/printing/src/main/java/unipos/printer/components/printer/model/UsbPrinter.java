package unipos.printer.components.printer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jssc.SerialPortException;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import unipos.printer.components.printer.ConnectionType;

import javax.imageio.ImageIO;
import javax.usb.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dominik on 27.09.2015.
 */

@Builder
@Data
public class UsbPrinter extends Printer {

    private short vendorId;
    private short productId;
    private ConnectionType connectionType = ConnectionType.USB;
    @Transient
    @JsonIgnore
    UsbDevice usbDevice;

    public UsbPrinter() {}

    public UsbPrinter(short vendorId, short productId, ConnectionType connectionType, UsbDevice usbDevice) {
        this.vendorId = vendorId;
        this.productId = productId;
        this.connectionType = connectionType;
        this.usbDevice = usbDevice;
    }

    @Override
    public void openConnection() throws IOException {
        try {
            usbDevice = findDevice((short)Integer.parseInt(Integer.toHexString(vendorId & 0xffff), 16), (short)Integer.parseInt(Integer.toHexString(productId & 0xffff),16));
        } catch (UsbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() throws IOException {

    }

    @Override
    public void initLogo() throws IOException, SerialPortException {
        initPrinter();

        UsbConfiguration configuration = usbDevice.getActiveUsbConfiguration();
        UsbInterface iface = configuration.getUsbInterface((byte) 0);
        try {
            iface.claim(usbInterface -> true);
        } catch (UsbException e) {
            e.printStackTrace();
        }
        try {
            UsbEndpoint endpoint = iface.getUsbEndpoint((byte)0x02);
            UsbPipe pipe = endpoint.getUsbPipe();
            pipe.open();
            try
            {

            int white = Color.WHITE.getRGB();


            //If there's no logoImage available stop trying to initialize it
            if(logoPath == null || logoPath.isEmpty() || !new File(logoPath).exists()) {
                logoPath = this.getClass().getResource("/unipos.bmp").getFile();
            }
            File imageFile = new File(getLogoPath());
            BufferedImage img = null;
            try {
                img = ImageIO.read(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            pipe.syncSubmit(new byte[] {(byte) 28, (byte) 113, (byte) 1});

            byte xL = (byte) Math.floor((double) img.getWidth() / 8.0);
            byte xH = 0;
            byte yL = (byte) Math.floor((double) img.getHeight() / 8.0);
            byte yH = 0;

            int width = xL * 8;
            int height = yL * 8;

            pipe.syncSubmit(new byte[] {xL, xH, yL, yH}); //FS q (2/2)

            byte pixel = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y += 8) {
                    pixel = 0;

                    for (int offset = 0; offset < 8; offset++) {
                        if (img.getRGB(x, y + 7 - offset) != white) {
                            pixel |= (byte) Math.pow(2, offset);
                        }
                    }
                    pipe.syncSubmit(new byte[] {(byte) pixel});
                }
            }
            }
            finally
            {
                pipe.close();
            }
        } catch (UsbException e) {
            e.printStackTrace();
        } finally
        {
            try {
                iface.release();
            } catch (UsbException e) {
                e.printStackTrace();
            }
        }

        System.out.print("Printing Done");
    }

    @Override
    protected void writeByteObjectOutputStream(byte... bytes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(bytes, 0, bytes.length);
        baos.close();
        byte[] array = baos.toByteArray();

        UsbConfiguration configuration = usbDevice.getActiveUsbConfiguration();
        UsbInterface iface = configuration.getUsbInterface((byte) 0);
        try {
            iface.claim(usbInterface -> true);
        } catch (UsbException e) {
            e.printStackTrace();
        }
        try
        {
            UsbEndpoint endpoint = iface.getUsbEndpoint((byte)0x02);
            UsbPipe pipe = endpoint.getUsbPipe();
            pipe.open();
            try
            {
                int sent = pipe.syncSubmit(bytes);
            }
            finally
            {
                pipe.close();
            }
        } catch (UsbException e) {
            e.printStackTrace();
        } finally
        {
            try {
                iface.release();
            } catch (UsbException e) {
                e.printStackTrace();
            }
        }
    }

    //Private Methods

    private UsbDevice findDevice(short vendorId, short productId) throws UsbException {
        UsbServices services = UsbHostManager.getUsbServices();
        UsbHub rootHub = services.getRootUsbHub();

        for (UsbDevice device : (List<UsbDevice>) rootHub.getAttachedUsbDevices())
        {
            UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
            if (desc.idVendor() == vendorId && desc.idProduct() == productId) return device;
            if (device.isUsbHub())
            {
                device = findDevice((UsbHub) device, vendorId, productId);
                if (device != null) return device;
            }
        }
        return null;
    }

    private UsbDevice findDevice(UsbHub hub, short vendorId, short productId)
    {
        for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices())
        {
            UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
            if (desc.idVendor() == vendorId && desc.idProduct() == productId) return device;
            if (device.isUsbHub())
            {
                device = findDevice((UsbHub) device, vendorId, productId);
                if (device != null) return device;
            }
        }
        return null;
    }
}
