package unipos.report.components.shared;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Thomas on 05.01.2016.
 */
public class WriteToResponseStream {

    public static void writePdfToResponseStream(HttpServletResponse response, File pdf, String fileName) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(pdf.getAbsolutePath());


        response.setHeader("Content-Disposition", "inline;filename=" + fileName);
        response.setContentType("application/pdf");

        OutputStream os = response.getOutputStream();
        IOUtils.copy(fileInputStream, os);
        fileInputStream.close();
        os.flush();
        os.close();

        pdf.delete();
    }
}
