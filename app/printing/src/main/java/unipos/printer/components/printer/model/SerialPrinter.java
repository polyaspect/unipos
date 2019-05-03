package unipos.printer.components.printer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import unipos.printer.components.printer.ConnectionType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Dominik on 26.09.2015.
 */
@Data
@Builder
public class SerialPrinter extends Printer {
    protected ConnectionType connectionType = ConnectionType.SERIAL;
    protected String comPort;
    @Transient
    @JsonIgnore
    protected SerialPort serialPort;

    public SerialPrinter() {}

    public SerialPrinter(ConnectionType connectionType, String comPort, SerialPort serialPort) {
        this.connectionType = connectionType;
        this.comPort = comPort;
        this.serialPort = serialPort;
    }

    @Override
    public void openConnection() throws IOException, SerialPortException {
        String[] portNames = SerialPortList.getPortNames();
        String portName = "";
        for(int i = 0; i < portNames.length; i++){
            if(comPort.equalsIgnoreCase(portNames[i]))
                portName = portNames[i];
        }
        serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_19200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);

        }
        catch (SerialPortException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void closeConnection() throws IOException {
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void writeByteObjectOutputStream(byte... bytes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(bytes, 0, bytes.length);
        baos.close();
        byte[] array = baos.toByteArray();
        try {
            serialPort.writeBytes(array);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

}
