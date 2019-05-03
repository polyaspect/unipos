package unipos.printer.components.shared.uniposInterpreter;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dominik on 27.09.2015.
 */
public interface Interpreter {

    String interpretXml(InputStream inputStream) throws IOException, SAXException;
}
