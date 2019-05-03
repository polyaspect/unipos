package unipos.printer.components.printer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jssc.SerialPortException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Transient;
import unipos.printer.components.printer.ConnectionType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Dominik on 26.09.2015.
 */

@Data
@Builder
@Slf4j
public class NetworkPrinter extends Printer implements Flushable {

    protected String ipAddress;
    protected int port;
    protected ConnectionType connectionType = ConnectionType.NETWORK;
    @Transient
    @JsonIgnore
    protected Socket socket;

    public NetworkPrinter() {}

    public NetworkPrinter(String ipAddress, int port, ConnectionType connectionType, Socket socket) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.connectionType = connectionType;
        this.socket = socket;
    }

    @Override
    public void openConnection() throws IOException {
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ipAddress, port), 5000);
        } catch (SocketTimeoutException e) {
            log.error("Error while connecting to the Socket. Timeout occured. Are the params correct?: ipAddress=" + ipAddress + ",port=" + port);
            throw e;
        }
    }

    @Override
    public void closeConnection() throws IOException {
        socket.getOutputStream().flush();
        socket.getOutputStream().close();
        socket.close();
    }

    @Override
    protected void writeByteObjectOutputStream(byte... bytes) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(bytes, 0, bytes.length);
        baos.close();
        byte[] array = baos.toByteArray();
        outputStream.write(array);
        outputStream.flush();
    }


    @Override
    public void flush() throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.flush();
    }
}
