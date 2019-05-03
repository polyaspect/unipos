package unipos.printer.components.shared.uniposInterpreter;

import com.google.common.primitives.Bytes;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import unipos.printer.components.shared.PrinterEscapeCodes;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dominik on 23.09.15.
 */
public class Interpreter80mm implements Interpreter {

    Document document;
    List<Integer> breakCount = new ArrayList<>(); //This variable is used to count the lines after the last cut signal
    List<Integer> breakPosition = new ArrayList<>(); //Marks the line at which the cut comes from

    private Interpreter80mm() {
    }

    private static class Holder {
        static final Interpreter80mm INSTANCE = new Interpreter80mm();
    }

    public static Interpreter80mm getInstance() {
        return Holder.INSTANCE;
    }

    public String interpretXml(InputStream inputStream) throws IOException, SAXException {
        breakCount = new ArrayList<>();
        breakPosition = new ArrayList<>();
        initDocument(inputStream);

        String result = "";
        int rowPointer = Integer.parseInt(getAttributeValue(document.getFirstChild().getFirstChild(), "row")); //I init this param with the value of the first row

        NodeList nodeList = document.getFirstChild().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if(node.hasChildNodes() && interpretRow(node).replace('\n', ' ').trim().equalsIgnoreCase("qr-code")) {
                result += getQrSequence(interpretRow(nodeList.item(i+1)).replace('\n', ' ').trim());
                node.getParentNode().removeChild(node);
                rowPointer++;
            } else {

                //Check if there is a space between the rows
                int spaceNewLines = Integer.parseInt(getAttributeValue(node, "row")) - rowPointer;
                for (int a = 0; a < spaceNewLines; a++) {
                    result += "\n";
                    rowPointer++;
                }
            }

            result += interpretRow(node);
            if(breakPosition.stream().filter(x -> x != 0).count() > 0) {
                breakCount = breakCount.stream().map(x -> x+1).collect(Collectors.toList());
            }
            if(breakCount.stream().filter(x -> x == 2).count() > 0) {
//                result += (Character.toString((char) 29) + Character.toString((char) 86) + Character.toString((char) 1));
                result  += (Character.toString((char) 27) + Character.toString((char) 105));
                breakCount = breakCount.stream().filter(x -> x < 2).collect(Collectors.toList());
            }
            rowPointer++;
        }

        result += "\n\n\n\n\n\n\n";

        return result;
    }

    private String getQrSequence(String qrCode) throws IOException {

        List<Byte> sequence = new ArrayList<>();

        if(qrCode != null && !qrCode.trim().isEmpty()) {

            int store_len = qrCode.length() + 4;
            byte store_pL = (byte) (store_len % 256);
            byte store_pH = (byte) (store_len / 256);

            // QR Code: Select the model
            //              Hex     1D      28      6B      04      00      31      41      n1(x32)     n2(x00) - size of model
            // set n1 [49 x31, model 1] [50 x32, model 2] [51 x33, micro qr code]
            // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=140
//        byte[] modelQR = PrinterEscapeCodes.QR_MODEL;

            // QR Code: Set the size of module
            // Hex      1D      28      6B      03      00      31      43      n
            // n depends on the printer
            // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=141
            byte[] sizeQR = {(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x43, (byte) 4};

            //          Hex     1D      28      6B      03      00      31      45      n
            // Set n for error correction [48 x30 -> 7%] [49 x31-> 15%] [50 x32 -> 25%] [51 x33 -> 30%]
            // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=142
//        byte[] errorQR = PrinterEscapeCodes.QR_ERROR_CORRECTION;

            // QR Code: Store the data in the symbol storage area
            // Hex      1D      28      6B      pL      pH      31      50      30      d1...dk
            // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=143
            //                        1D          28          6B         pL          pH  cn(49->x31) fn(80->x50) m(48->x30) d1…dk
            byte[] storeQR = {(byte) 0x1d, (byte) 0x28, (byte) 0x6b, store_pL, store_pH, (byte) 0x31, (byte) 0x50, (byte) 0x30};

            // QR Code: Print the symbol data in the symbol storage area
            // Hex      1D      28      6B      03      00      31      51      m
            // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=144
//        byte[] printQR = PrinterEscapeCodes.QR_PRINT;

            // write() simply appends the data to the buffer
            sequence.addAll(Bytes.asList(PrinterEscapeCodes.QR_MODEL));
            sequence.add((byte) 10);
            sequence.addAll(Bytes.asList(sizeQR));
            sequence.add((byte) 10);
            sequence.addAll(Bytes.asList(PrinterEscapeCodes.QR_ERROR_CORRECTION));
            sequence.add((byte) 10);
            sequence.addAll(Bytes.asList(storeQR));
            sequence.addAll(Bytes.asList(qrCode.getBytes()));
            sequence.add((byte) 10);
            sequence.addAll(Bytes.asList(PrinterEscapeCodes.HORIZONTAL_CENTER));
            sequence.add((byte) 10);
            sequence.addAll(Bytes.asList(PrinterEscapeCodes.QR_PRINT));
//        sequence += "\n\n\n\n\n\n\n";
        } else {
            return new String(Bytes.toArray(sequence), "IBM437");
        }

        return new String(Bytes.toArray(sequence), "IBM437");
    }

    private void initDocument(InputStream inputStream) throws IOException, SAXException {
        Interpreter80mm interpreter = Holder.INSTANCE;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // root elements
        interpreter.document = docBuilder.parse(inputStream);
    }

    private String interpretRow(Node row) {
        String result = "";
        int rowPosition = 0;

        int rowNumber = Integer.parseInt(getAttributeValue(row, "row"));
        NodeList cols = row.getChildNodes();
        for (int i = 0; i < cols.getLength(); i++) {
            result += interpretCol(cols.item(i), rowPosition);
            rowPosition += Integer.parseInt(getAttributeValue(cols.item(i), "colSpan"));
        }
        if(result.contains("<@--break-->")) {
            breakPosition.add(rowNumber);
            breakCount.add(0);
            result = "\n\n\n";
        } else {
            result += "\n";
        }
        if(result.contains("nke-Bon")) {
            result += "\n";
        }
        return result;
    }

    private String interpretCol(Node item, int rowPosition) {
        String result = "";

        int chars = Integer.parseInt(getAttributeValue(item, "chars"));
        int colspan = Integer.parseInt(getAttributeValue(item, "colSpan"));
        int col = Integer.parseInt(getAttributeValue(item, "col"));
        String horAlignment = getAttributeValue(item, "horAlignment");
        int colpadding = colspan - chars;

        horAlignment = horAlignment == null ? "Left" : horAlignment;

        if (rowPosition == 0  && !horAlignment.equalsIgnoreCase("Center")) {
            if(horAlignment.equalsIgnoreCase("Right")) {
                for (int i = 0; i < col-colpadding; i++) {
                    result += " ";
                }
            } else {
                for (int i = 0; i < col; i++) {
                    result += " ";
                }
            }
        }

        if (horAlignment.equalsIgnoreCase("Right")) {
            for (int i = 0; i < colpadding; i++) {
                result += " ";
            }

            result += item.getTextContent();
        }

        if (horAlignment.equalsIgnoreCase("Center")) {
            int paddingLeft = Math.round(Float.valueOf(colpadding)/2);
            int paddingRight = colspan-paddingLeft-chars;
            for (int i = 0; i < paddingLeft; i++) {
                result += " ";
            }

            result += item.getTextContent();

            for (int i = 0; i < paddingRight; i++) {
                result += " ";
            }
        }


        if (horAlignment.equalsIgnoreCase("Left")) {
            result += item.getTextContent();

            for (int i = 0; i < colpadding; i++) {
                result += " ";
            }
        }
        return result;
    }

    private String getAttributeValue(Node node, String attributeName) {
        NamedNodeMap attributes = node.getAttributes();
        String result = "";
        try {
            result = attributes.getNamedItem(attributeName).getNodeValue();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

}
