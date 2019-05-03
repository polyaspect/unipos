package unipos.printer.components.printing;

import jssc.SerialPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;
import unipos.common.remote.printer.model.ThermalPrinterLine;
import unipos.common.remote.printer.model.ThermalPrinterLineFormat;
import unipos.printer.components.printer.model.EscapeCodes;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.printer.components.printer.model.HorizontalAlignment;
import unipos.printer.components.printer.model.Printer;
import unipos.printer.components.printer.PrinterService;
import unipos.printer.components.shared.uniposInterpreter.ImageScaler;
import unipos.printer.components.shared.uniposInterpreter.Interpreter;
import unipos.printer.components.shared.uniposInterpreter.Interpreter80mm;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dominik on 17.09.15.
 */

@RestController
@RequestMapping("/printing")
@Slf4j
public class PrintingController {

    @Autowired
    PrinterService printerService;
    @Autowired
    LogRemoteInterface logRemoteInterface;
    @Autowired
    DataRemoteInterface dataRemoteInterface;

    Interpreter interpreter = Interpreter80mm.getInstance();

    @RequestMapping(value = "/socket", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void printing(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException, SAXException, SerialPortException {
        Printer printer;
        try {
            printer = printerService.findDefaultPrinter(request);
        } catch (IllegalArgumentException e) {
            response.sendError(400, e.getMessage());
            return;
        } catch (DataRetrievalFailureException e) {
            response.sendError(404, e.getMessage());
            return;
        }

        try {


            InputStream result = file.getInputStream();
            byte[] toPrint = interpreter.interpretXml(result).getBytes("IBM437");
            printer.openConnection();
            printer.printLogo();
            printer.sendMessage("\n\n");

            printer.sendMessage(toPrint);

            printer.cutPaper();
            printer.closeConnection();
        } catch (Exception ex) {
            LogDto logDto = LogDto.builder()
                    .message("Error during the printing process: " + ex.getMessage())
                    .dateTime(LocalDateTime.now())
                    .source(this.getClass().getName() + "#" + "printing")
                    .level(LogDto.Level.ERROR)
                    .build();
            logDto.addParameter("ExceptionType", ex.getClass().toString());
            logDto.addParameter("ExceptionMessage", ex.getMessage());

            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));

            logDto.addParameter("ExceptionStackTrace", errors.toString());
            logRemoteInterface.log(logDto);
        }
    }

    @RequestMapping(value = "/reprintInvoice", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void reprintInvoice(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws SerialPortException, IOException, SAXException {
        Printer printer;
        try {
            printer = printerService.findDefaultPrinter(request);
        } catch (IllegalArgumentException e) {
            response.sendError(400, e.getMessage());
            return;
        } catch (DataRetrievalFailureException e) {
            response.sendError(404, e.getMessage());
            return;
        }

        try {
            printer.openConnection();

            InputStream result = file.getInputStream();
            byte[] toPrint = interpreter.interpretXml(result).getBytes("IBM437");

            printer.printLogo();
            printer.sendEscapeSequence(ThermalPrinterLineFormat.DOUBLE_HEIGHT, true);
            printer.sendEscapeSequence(ThermalPrinterLineFormat.DOUBLE_WIDTH, true);
            printer.sendMessage("----Duplikat----", HorizontalAlignment.CENTER, 2);
            printer.sendMessage("\n");
            printer.sendEscapeSequence(ThermalPrinterLineFormat.DOUBLE_HEIGHT, false);
            printer.sendEscapeSequence(ThermalPrinterLineFormat.DOUBLE_WIDTH, false);
            printer.sendMessage("\n");
            printer.sendMessage(toPrint);
            printer.cutPaper();
            printer.closeConnection();
        } catch (Exception ex) {
            LogDto logDto = LogDto.builder()
                    .message("Error during the printing process: " + ex.getMessage())
                    .dateTime(LocalDateTime.now())
                    .source(this.getClass().getName() + "#" + "printing")
                    .level(LogDto.Level.ERROR)
                    .build();
            logDto.addParameter("ExceptionType", ex.getClass().toString());
            logDto.addParameter("ExceptionMessage", ex.getMessage());

            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));

            logDto.addParameter("ExceptionStackTrace", errors.toString());
            logRemoteInterface.log(logDto);
            response.setStatus(500);
            throw ex;
        }
    }

    @RequestMapping(value = "/printRevertedInvoice", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void printRevertedInvoice(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, @RequestParam Long reversedInvoiceNumber, HttpServletResponse response) throws SerialPortException, IOException, SAXException {
        Printer printer;
        try {
            printer = printerService.findDefaultPrinter(request);
        } catch (IllegalArgumentException e) {
            response.sendError(400, e.getMessage());
            return;
        } catch (DataRetrievalFailureException e) {
            response.sendError(404, e.getMessage());
            return;
        }

        try {
            printer.openConnection();

            InputStream result = file.getInputStream();
            byte[] toPrint = interpreter.interpretXml(result).getBytes("IBM437");

            printer.printLogo();
            printer.sendEscapeSequence(ThermalPrinterLineFormat.DOUBLE_HEIGHT, true);
            printer.sendEscapeSequence(ThermalPrinterLineFormat.DOUBLE_WIDTH, true);
            printer.sendMessage("----Storno-Rechnung----", HorizontalAlignment.CENTER, 2);
            printer.sendEscapeSequence(ThermalPrinterLineFormat.DOUBLE_HEIGHT, false);
            printer.sendEscapeSequence(ThermalPrinterLineFormat.DOUBLE_WIDTH, false);
            printer.sendMessage("Originale Rechnungs-Nr: " + reversedInvoiceNumber, HorizontalAlignment.CENTER, 1);
            printer.sendMessage("\n");
            printer.sendMessage("\n");
            printer.sendMessage(toPrint);
            printer.cutPaper();
            printer.closeConnection();
        } catch (Exception ex) {
            LogDto logDto = LogDto.builder()
                    .message("Error during the printing process: " + ex.getMessage())
                    .dateTime(LocalDateTime.now())
                    .source(this.getClass().getName() + "#" + "printing")
                    .level(LogDto.Level.ERROR)
                    .build();
            logDto.addParameter("ExceptionType", ex.getClass().toString());
            logDto.addParameter("ExceptionMessage", ex.getMessage());

            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));

            logDto.addParameter("ExceptionStackTrace", errors.toString());
            logRemoteInterface.log(logDto);
            response.setStatus(500);
            throw ex;
        }
    }

    @RequestMapping(value = "/testPrinting", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void testPrinting(@RequestBody Printer printer, HttpServletResponse response) throws IOException, SerialPortException {
        try {
            printer.openConnection();
            printer.sendMessage("This is a Test-Message for the printer: \n" + printer + "\n\n\n\n\n\n\n");
            printer.cutPaper();
            printer.closeConnection();
        } catch (Exception e) {
            response.sendError(500, "Unable to with the given printer!");
            return;
        }
    }

    @RequestMapping(value = "/returnToPrintText", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String returnToPrintText(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException, SAXException, SerialPortException {

        InputStream result = file.getInputStream();
        String toPrint = interpreter.interpretXml(result);

        return toPrint;
    }

    @RequestMapping(value = "/printText", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void printText(@RequestBody List<String> lines, HttpServletRequest request, HttpServletResponse response) throws IOException, SAXException, SerialPortException {

        Printer printer;
        try {
            printer = printerService.findDefaultPrinter(request);
        } catch (IllegalArgumentException e) {
            response.sendError(400, e.getMessage());
            return;
        } catch (DataRetrievalFailureException e) {
            response.sendError(404, e.getMessage());
            return;
        }

        try {
            printer.openConnection();
            lines.stream().forEach(line -> {
                try {
                    byte[] print = (line + "\n").getBytes("IBM437");
                    printer.sendMessage(print);
                } catch (Exception e) {
                    log.error("Error during line printing");
                }
            });
            printer.sendMessage("\n\n\n\n\n\n\n");
            printer.cutPaper();
            printer.closeConnection();

        } catch (Exception ex) {
            log.error("Error during text printing");
        }
    }

    @RequestMapping(value = "/printTextEsc", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void printTextStreamWithEscapeSequence(@RequestBody(required = true) List<ThermalPrinterLine> escapeSequences, HttpServletRequest request, HttpServletResponse response) throws IOException, SAXException, SerialPortException {

        Printer printer;
        try {
            printer = printerService.findDefaultPrinter(request);
        } catch (IllegalArgumentException e) {
            response.sendError(400, e.getMessage());
            return;
        } catch (DataRetrievalFailureException e) {
            response.sendError(404, e.getMessage());
            return;
        }

        try {
            printer.openConnection();

            if (escapeSequences != null) {
//                for (int i = 0; i < lines.size(); i++) {
//                    for (EscapeSequence escapeSequence : escapeSequences) {
//                        if (escapeSequence.getLineNum() == i) {
//                            printer.sendMessage(escapeSequence.getEscSequenceBefore());
//                            printer.sendMessage(lines.get(i).getBytes("IBM437"));
//                            printer.sendMessage(escapeSequence.getEscSequenceAfter());
//                        } else {
//                            printer.sendMessage(lines.get(i).getBytes("IBM437"));
//                        }
//                    }
//                }
                escapeSequences.stream().forEach(escapeSequence -> {
                    try {
                        escapeSequence.getFormats().stream().forEach(format -> {
                            try {
                                printer.sendMessage(EscapeCodes.getEscapeCodeForLineFormatBefore(format));
                            } catch (Exception e) {
                                log.error(e.getMessage());
                            }
                        });
                        printer.sendMessage(escapeSequence.getLine());
                        escapeSequence.getFormats().stream().forEach(format -> {
                            try {
                                printer.sendMessage(EscapeCodes.getEscapeCodeForLineFormatAfter(format));
                            } catch (Exception e) {
                                log.error(e.getMessage());
                            }
                        });
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                });
                printer.sendMessage("\n\n\n\n\n\n\n");
                printer.cutPaper();
            }

            printer.closeConnection();

        } catch (Exception ex) {
            log.error("Error during text printing");
        }
    }

    @RequestMapping(value = "/uploadLogo", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("printerId") Long printerId, HttpServletResponse response, HttpServletRequest request) throws IOException {
        if (!file.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
//            File saveFile = new File(this.getClass().getResource("/").getFile() + String.format("%4d%02d%02d_%02d%02d%02d." + FilenameUtils.getExtension(file.getOriginalFilename()), now.getYear(), now.getMonth().getValue(), now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond()));
            File saveFile = new File(request.getServletContext().getResource("/").getFile() + "images" + File.separator + printerId + ".bmp");
            if (!saveFile.exists()) {
                saveFile.getParentFile().mkdirs();
                saveFile.createNewFile();
            }

            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(saveFile));
            stream.write(bytes);
            stream.close();

            resizeImageToCorrectSize(saveFile);

            Printer printer = printerService.findByPrinterId(printerId);

            printer.setLogoPath(saveFile.getPath());
            printerService.savePrinter(printer);


            try {
                printer.openConnection();
                printer.initLogo(saveFile.getPath());
                printer.closeConnection();
            } catch (Exception ignored) {
                printer.closeConnection();
                response.sendError(400, "Not able to initialize the Logo. Are the params correct?");
                return;
            }
        }
    }

    @RequestMapping(value = "/printCurrentLogo", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void printCurrentLogo(@RequestBody Printer printer) throws IOException, SerialPortException {
        printer.openConnection();
        printer.printLogo();
        printer.sendMessage("\n\n\n\n\n\n\n\n");
        printer.cutPaper();
        printer.closeConnection();
    }

    @RequestMapping(value = "/print", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void print(@RequestBody ThermalPrinterLine[] thermalPrinterLines, HttpServletRequest request, HttpServletResponse response) {
        Printer printer;
        try {
            if (thermalPrinterLines != null) {
                try {
                    printer = printerService.findDefaultPrinter(request);
                    printer.openConnection();

                    Arrays.asList(thermalPrinterLines).stream().forEach(escapeSequence -> {
                        try {
                            escapeSequence.getFormats().stream().forEach(format -> {
                                try {
                                    printer.sendMessage(EscapeCodes.getEscapeCodeForLineFormatBefore(format));
                                } catch (Exception e) {
                                    log.error(e.getMessage());
                                }
                            });
                            printer.sendMessage(escapeSequence.getLine());
                            escapeSequence.getFormats().stream().forEach(format -> {
                                try {
                                    printer.sendMessage(EscapeCodes.getEscapeCodeForLineFormatAfter(format));
                                } catch (Exception e) {
                                    log.error(e.getMessage());
                                }
                            });
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                    });
                    printer.sendMessage("\n\n\n\n\n\n\n");
                    printer.cutPaper();
                    printer.closeConnection();
                } catch (Exception e) {
                    response.sendError(500, "Error when retrieving Printer Info");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resizeImageToCorrectSize(File imageFile) {
        ImageScaler imageScaler = null;
        try {
            imageScaler = new ImageScaler(ImageIO.read(imageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageScaler.createScaledImage(551, 220, ImageScaler.ScaleType.FIT);
        imageScaler.saveScaledImage(imageFile, ImageScaler.ImageType.IMAGE_PNG);
    }
}
