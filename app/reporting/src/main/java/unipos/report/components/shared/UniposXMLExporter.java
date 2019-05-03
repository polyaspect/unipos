package unipos.report.components.shared;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRTextExporterContext;
import net.sf.jasperreports.engine.fill.JRTemplatePrintText;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by dominik on 23.09.15.
 */
public class UniposXMLExporter extends JRAbstractExporter<TextReportConfiguration, TextExporterConfiguration, WriterExporterOutput, JRTextExporterContext>
{
    private static final String TXT_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.txt.";
    public static final String EXCEPTION_MESSAGE_KEY_REQUIRED_POSITIVE_PAGE_OR_CHARACTER_WIDTH = "export.text.required.positive.page.or.character.width";
    public static final String EXCEPTION_MESSAGE_KEY_CHARACTER_WIDTH_NEGATIVE = "export.text.character.width.negative";
    public static final String EXCEPTION_MESSAGE_KEY_REQUIRED_POSITIVE_PAGE_OR_CHARACTER_HEIGHT = "export.text.required.positive.page.or.character.height";
    public static final String EXCEPTION_MESSAGE_KEY_CHARACTER_HEIGHT_NEGATIVE = "export.text.character.height.negative";

    protected Writer writer;
    char[][] pageData;
    protected int pageWidthInChars;
    protected int pageHeightInChars;
    protected float charWidth;
    protected float charHeight;
    protected String pageSeparator;
    protected String lineSeparator;
    protected boolean isTrimLineRight;

    protected static final String systemLineSeparator = System.getProperty("line.separator");

    //CustomStuff
    protected Document document;
    protected Element rootElement;

    protected class ExporterContext extends BaseExporterContext implements JRTextExporterContext
    {
    }

    /**
     * @see #UniposXMLExporter(JasperReportsContext)
     */
    public UniposXMLExporter()
    {
        this(DefaultJasperReportsContext.getInstance());
    }


    /**
     *
     */
    public UniposXMLExporter(JasperReportsContext jasperReportsContext)
    {
        super(jasperReportsContext);

        exporterContext = new ExporterContext();
    }


    /**
     *
     */
    protected Class<TextExporterConfiguration> getConfigurationInterface()
    {
        return TextExporterConfiguration.class;
    }


    /**
     *
     */
    protected Class<TextReportConfiguration> getItemConfigurationInterface()
    {
        return TextReportConfiguration.class;
    }


    /**
     *
     */
    @SuppressWarnings("deprecation")
    protected void ensureOutput()
    {
        if (exporterOutput == null)
        {
            exporterOutput =
                    new net.sf.jasperreports.export.parameters.ParametersWriterExporterOutput(
                            getJasperReportsContext(),
                            getParameters(),
                            getCurrentJasperPrint()
                    );
        }
    }


    /**
     *
     */
    public void exportReport() throws JRException
    {
		/*   */
        ensureJasperReportsContext();
        ensureInput();

        initExport();

        ensureOutput();

        writer = getExporterOutput().getWriter();

        try
        {
            exportReportToWriter();
        }
        catch (IOException e)
        {
            throw
                    new JRException(
                            EXCEPTION_MESSAGE_KEY_OUTPUT_WRITER_ERROR,
                            new Object[]{jasperPrint.getName()},
                            e);
        }
        catch (TransformerException e)
        {
            throw
                    new JRException(
                            EXCEPTION_MESSAGE_KEY_OUTPUT_WRITER_ERROR,
                            new Object[]{jasperPrint.getName()},
                            e);
        } catch (XPathExpressionException e) {
            new JRException(
                    EXCEPTION_MESSAGE_KEY_OUTPUT_WRITER_ERROR,
                    new Object[]{jasperPrint.getName()},
                    e);
        } finally
        {
            getExporterOutput().close();
        }
    }


    @Override
    protected void initExport()
    {
        super.initExport();

        //Instanciate the new XML Document
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // root elements
        document = docBuilder.newDocument();
        rootElement = document.createElement("Report");

        TextExporterConfiguration configuration = getCurrentConfiguration();

        lineSeparator = configuration.getLineSeparator();
        if (lineSeparator == null)
        {
            lineSeparator = systemLineSeparator;
        }

        pageSeparator = configuration.getPageSeparator();
        if (pageSeparator == null)
        {
            pageSeparator = systemLineSeparator + systemLineSeparator;
        }

        isTrimLineRight = configuration.isTrimLineRight();
    }


    @Override
    protected void initReport()
    {
        super.initReport();

        TextReportConfiguration configuration = getCurrentItemConfiguration();

        Float charWidthValue = configuration.getCharWidth();
        charWidth = charWidthValue == null ? 0 : charWidthValue;
        if (charWidth < 0)
        {
            throw
                    new JRRuntimeException(
                            EXCEPTION_MESSAGE_KEY_CHARACTER_WIDTH_NEGATIVE,
                            (Object[])null
                    );
        }
        else if (charWidth == 0)
        {
            Integer pageWidthInCharsValue = configuration.getPageWidthInChars();
            pageWidthInChars = pageWidthInCharsValue == null ? 0 : pageWidthInCharsValue;

            if (pageWidthInChars <= 0)
            {
                throw
                        new JRRuntimeException(
                                EXCEPTION_MESSAGE_KEY_REQUIRED_POSITIVE_PAGE_OR_CHARACTER_WIDTH,
                                (Object[])null
                        );
            }

            charWidth = jasperPrint.getPageWidth() / (float)pageWidthInChars;
        }
        else
        {
            pageWidthInChars = (int)(jasperPrint.getPageWidth() / charWidth);
        }


        Float charHeightValue = configuration.getCharHeight();
        charHeight = charHeightValue == null ? 0 : charHeightValue;
        if (charHeight < 0)
        {
            throw
                    new JRRuntimeException(
                            EXCEPTION_MESSAGE_KEY_CHARACTER_HEIGHT_NEGATIVE,
                            (Object[])null
                    );
        }
        else if (charHeight == 0)
        {
            Integer pageHeightInCharsValue = configuration.getPageHeightInChars();
            pageHeightInChars = pageHeightInCharsValue == null ? 0 : pageHeightInCharsValue;
            if (pageHeightInChars <= 0)
            {
                throw
                        new JRRuntimeException(
                                EXCEPTION_MESSAGE_KEY_REQUIRED_POSITIVE_PAGE_OR_CHARACTER_HEIGHT,
                                (Object[])null
                        );
            }

            charHeight = jasperPrint.getPageHeight() / (float)pageHeightInChars;
        }
        else
        {
            pageHeightInChars = (int)(jasperPrint.getPageHeight() / charHeight);
        }
    }


    /**
     *
     */
    protected void exportReportToWriter() throws JRException, IOException, TransformerException, XPathExpressionException {
        List<ExporterInputItem> items = exporterInput.getItems();

        for(int reportIndex = 0; reportIndex < items.size(); reportIndex++)
        {
            ExporterInputItem item = items.get(reportIndex);

            setCurrentExporterInputItem(item);

            List<JRPrintPage> pages = jasperPrint.getPages();
            if (pages != null && pages.size() > 0)
            {
                PageRange pageRange = getPageRange();
                int startPageIndex = (pageRange == null || pageRange.getStartPageIndex() == null) ? 0 : pageRange.getStartPageIndex();
                int endPageIndex = (pageRange == null || pageRange.getEndPageIndex() == null) ? (pages.size() - 1) : pageRange.getEndPageIndex();

                for(int i = startPageIndex; i <= endPageIndex; i++)
                {
                    if (Thread.interrupted())
                    {
                        throw new ExportInterruptedException();
                    }

                    JRPrintPage page = pages.get(i);

					/*   */
                    exportPage(page);
                }
            }
        }

        writer.flush();
    }


    /**
     * Exports a page to the output writer. Only text elements within the page are considered. For each page, the engine
     * creates a matrix of characters and each rendered text element is placed at the appropriate position in the matrix.
     * After all texts are parsed, the character matrix is sent to the output writer.
     */
    protected void exportPage(JRPrintPage page) throws IOException, TransformerException, XPathExpressionException {
        List<JRPrintElement> elements = page.getElements();
        String qrCode = "";
        for (int i = 0; i < elements.size(); i++) {
            JRPrintElement element = elements.get(i);
            if(((JRTemplatePrintText)element).getFullText().equalsIgnoreCase("qr-code")) {
                qrCode = ((JRTemplatePrintText)elements.get(i+1)).getValue().toString();
            }
        }


        pageData = new char[pageHeightInChars][];
        for (int i = 0; i < pageHeightInChars; i++) {
            pageData[i] = new char[pageWidthInChars];
            Arrays.fill(pageData[i], ' ');
        }

        exportElements(elements);

//        for (int i = 0; i < pageHeightInChars; i++)
//        {
//            int lineLength = pageWidthInChars;
//            if (isTrimLineRight)
//            {
//                int j = pageWidthInChars - 1;
//                while (j >= 0 && pageData[i][j] == ' ')
//                {
//                    j--;
//                }
//                lineLength = j + 1;
//            }
//
//            writer.write(pageData[i], 0, lineLength);
//            writer.write(lineSeparator);
//        }

//        NodeList nodeList = rootElement.getChildNodes();
//
//        int marginLeft = Integer.MAX_VALUE;
//        int marginTop = Integer.MAX_VALUE;
//        for(int i = 0; i < nodeList.getLength(); i++) {
//            Node node = nodeList.item(i);
//            NamedNodeMap namedNodeMap = node.getAttributes();
//            for(int j = 0; j < namedNodeMap.getLength(); j++) {
//                Node attribute = namedNodeMap.item(j);
//                if(attribute.getNodeName().equalsIgnoreCase("row")) {
//                    if(Integer.parseInt(attribute.getNodeValue()) < marginTop)
//                        marginTop = Integer.parseInt(attribute.getNodeValue());
//                }
//                if(attribute.getNodeName().equalsIgnoreCase("col")) {
//                    if(Integer.parseInt(attribute.getNodeValue()) < marginLeft)
//                        marginLeft = Integer.parseInt(attribute.getNodeValue());
//                }
//            }
//        }
// --------------------------------------------------------------------------------------
        NodeList nodeList = rootElement.getChildNodes();

//        int marginLeft = Integer.MAX_VALUE;
//        int marginTop = Integer.MAX_VALUE;

//        String qrCode = "";
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if(node.hasChildNodes() && node.getFirstChild().getNodeValue().equalsIgnoreCase("qr-code")) {
                if (nodeList.item(i+1) != null) {
                    nodeList.item(i+1).getFirstChild().setNodeValue(qrCode);
                }
            }
//            NamedNodeMap namedNodeMap = node.getAttributes();
//            for(int j = 0; j < namedNodeMap.getLength(); j++) {
//                Node attribute = namedNodeMap.item(j);
//                if(attribute.getNodeName().equalsIgnoreCase("row")) {
//                    if(Integer.parseInt(attribute.getNodeValue()) < marginTop)
//                        marginTop = Integer.parseInt(attribute.getNodeValue());
//                }
//                if(attribute.getNodeName().equalsIgnoreCase("col")) {
//                    if(Integer.parseInt(attribute.getNodeValue()) < marginLeft)
//                        marginLeft = Integer.parseInt(attribute.getNodeValue());
//                }
//            }
        }
// -------------------------------------------------------------------------------------------------------

        rootElement = (Element) document.importNode(DOMUtilExt.reformatReportNode(rootElement, true), true);
        rootElement.setAttribute("widthCharCount", String.valueOf(pageWidthInChars));
        document.appendChild(rootElement);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter StringWriter = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(StringWriter));
        String output = StringWriter.getBuffer().toString();

        writer.write(output.toCharArray(), 0, output.length());

        JRExportProgressMonitor progressMonitor = getCurrentItemConfiguration().getProgressMonitor();
        if (progressMonitor != null)
        {
            progressMonitor.afterPageExport();
        }
    }


    protected void exportElements(List<JRPrintElement> elements)
    {
        for (int i = 0; i < elements.size();i++)
        {
            Object element = elements.get(i);
            if (element instanceof JRPrintText)
            {
                exportText((JRPrintText) element);
            }
            else if (element instanceof JRPrintFrame)
            {
                JRPrintFrame frame = (JRPrintFrame) element;
                setFrameElementsOffset(frame, false);
                try
                {
                    exportElements(frame.getElements());
                }
                finally
                {
                    restoreElementOffsets();
                }
            }
        }
    }


    /**
     * Renders a text and places it in the output matrix.
     */
    protected void exportText(JRPrintText element)
    {
        if(element.getFullText().contains("----------")) {
            int i = 0;
        }
        int colSpan = getWidthInChars(element.getWidth());
        int rowSpan = getHeightInChars(element.getHeight());
        int col = getWidthInChars(element.getX() + getOffsetX());
        int row = getHeightInChars(element.getY() + getOffsetY());

        if (col + colSpan > pageWidthInChars)
        {
            //if the text exceeds the page width, truncate the column count
            colSpan = pageWidthInChars - col;
        }

        String allText;
        JRStyledText styledText = getStyledText(element);
        if (styledText == null)
        {
            allText = "";
        }
        else
        {
            if(element.getValue() != null && element.getValue() instanceof String) {
                allText = (String) element.getValue();
            } else {
                allText = styledText.getText();
            }
        }

        // if the space is too small, the element will not be rendered
        if (rowSpan <= 0 || colSpan <= 0)
        {
            return;
        }

        if (allText != null && allText.length() == 0)
        {
            return;
        }

        // uses an array of string buffers, since the maximum number of rows is already calculated
        StringBuffer[] rows = new StringBuffer[rowSpan];
        rows[0] = new StringBuffer();
        int rowIndex = 0;
        int rowPosition = 0;
        boolean isFirstLine = true;

        //////
        //////
        //////
//        int mypos = allText.length() - 1;
//        while (mypos >= 0 && allText.charAt(mypos) == ' ')
//        {
//            mypos--;
//        }
//        allText = allText.substring(0, mypos + 1);
        //////
        //////
        //////

        // first search for \n, because it causes immediate line break
        StringTokenizer lfTokenizer = new StringTokenizer(allText, "\n", true);
        label:while (lfTokenizer.hasMoreTokens()) {
            String line = lfTokenizer.nextToken();
            // if text starts with a new line:
            if(isFirstLine && line.equals("\n"))
            {
                rows[rowIndex].append("");
                rowIndex++;
                if(rowIndex == rowSpan || !lfTokenizer.hasMoreTokens())
                {
                    break label;
                }
                rowPosition = 0;
                rows[rowIndex] = new StringBuffer();
                line = lfTokenizer.nextToken();
            }

            isFirstLine = false;

            // if there is a series of new lines:
            int emptyLinesCount = 0;
            while(line.equals("\n") && lfTokenizer.hasMoreTokens())
            {
                emptyLinesCount ++;
                line = lfTokenizer.nextToken();
            }

            if(emptyLinesCount > 1)
            {
                for(int i = 0; i < emptyLinesCount-1; i++)
                {
                    rows[rowIndex].append("");
                    rowIndex++;
                    if(rowIndex == rowSpan)
                    {
                        break label;
                    }
                    rowPosition = 0;
                    rows[rowIndex] = new StringBuffer();
                    //if this is the last empty line:
                    if(!lfTokenizer.hasMoreTokens() && line.equals("\n"))
                    {
                        rows[rowIndex].append("");
                        break label;
                    }
                }
            }
            if(!line.equals("\n"))
            {

                StringTokenizer spaceTokenizer = new StringTokenizer(line, " ", true);

                // divide each text line in words
                while (spaceTokenizer.hasMoreTokens()) {
                    String word = spaceTokenizer.nextToken();

                    // situation: word is larger than the entire column
                    // in this case breaking occurs in the middle of the word
                    while (word.length() > colSpan) {
                        rows[rowIndex].append(word.substring(0, colSpan - rowPosition));
                        word = word.substring(colSpan - rowPosition, word.length());
                        rowIndex++;
                        if(rowIndex == rowSpan)
                        {
                            break label;
                        }
                        rowPosition = 0;
                        rows[rowIndex] = new StringBuffer();
                    }

                    // situation: word is larger than remaining space on the current line
                    // in this case, go to the next line
                    if (rowPosition + word.length() > colSpan)
                    {
                        rowIndex++;
                        if (rowIndex == rowSpan)
                        {
                            break label;
                        }
                        rowPosition = 0;
                        rows[rowIndex] = new StringBuffer();
                    }

                    // situation: the word is actually a space and it situated at the beginning of a new line
                    // in this case, it is removed
                    if (rowIndex > 0 && rowPosition == 0 && word.equals(" "))
                    {
                        continue;
                    }
                    // situation: the word is small enough to fit in the current line
                    // in this case just add the word and increment the cursor position
                    rows[rowIndex].append(word);
                    rowPosition += word.length();
                }


                rowIndex++;
                if(rowIndex == rowSpan)
                {
                    break;
                }
                rowPosition = 0;
                rows[rowIndex] = new StringBuffer();
            }
        }

        int colOffset = 0;
        int rowOffset = 0;

        switch (element.getVerticalTextAlign())
        {
            case BOTTOM :
            {
                rowOffset = rowSpan - rowIndex;
                break;
            }
            case MIDDLE :
            {
                rowOffset = (rowSpan - rowIndex) / 2;
                break;
            }
        }

        for (int i = 0; i < rowIndex; i++) {
            Element fieldElement = document.createElement("Field");

            String line = rows[i].toString();
            int pos = line.length() - 1;
            while (pos >= 0 && line.charAt(pos) == ' ')
            {
                pos--;
            }
            line = line.substring(0, pos + 1);
            switch (element.getHorizontalTextAlign())
            {
                case RIGHT :
                {
                    colOffset = colSpan - line.length();
                    fieldElement.setAttribute("horAlignment", "Right");
                    break;
                }
                case CENTER :
                {
                    colOffset = (colSpan - line.length()) / 2;
                    fieldElement.setAttribute("horAlignment", "Center");
                    break;
                }

                // if text is justified, there is no offset, but the line text is modified
                // the last line in the paragraph is not justified.
                case JUSTIFIED :
                {
                    if (i < rowIndex - 1) {
                        line = justifyText(line, colSpan);
                    }
                    break;
                }
            }

            fieldElement.setAttribute("chars", String.valueOf(line.length()));
            fieldElement.setAttribute("colSpan", String.valueOf(colSpan));
            fieldElement.setAttribute("col", String.valueOf(col+colOffset));
            fieldElement.setAttribute("row", String.valueOf(row+i));
            fieldElement.setAttribute("font-size", String.valueOf(element.getFontsize()));

            //Add the Elements to the XML Tree
            fieldElement.setTextContent(line);
            rootElement.appendChild(fieldElement);
        }
    }


    /**
     * Justifies the text inside a specified space.
     */
    private String justifyText(String s, int width)
    {
        StringBuffer justified = new StringBuffer();

        StringTokenizer t = new StringTokenizer(s, " ");
        int tokenCount = t.countTokens();
        if (tokenCount <= 1)
        {
            return s;
        }

        String[] words = new String[tokenCount];
        int i = 0;
        while (t.hasMoreTokens())
        {
            words[i++] = t.nextToken();
        }

        int emptySpace = width - s.length() + (words.length - 1);
        int spaceCount = emptySpace / (words.length - 1);
        int remainingSpace = emptySpace % (words.length - 1);

        char[] spaces = new char[spaceCount];
        Arrays.fill(spaces, ' ');

        for (i = 0; i < words.length - 1; i++)
        {
            justified.append(words[i]);
            justified.append(spaces);
            if (i < remainingSpace)
            {
                justified.append(' ');
            }
        }
        justified.append(words[words.length-1]);

        return justified.toString();
    }


    /**
     * Transforms height from pixel space to character space.
     */
    protected int getHeightInChars(int height)
    {
        //return (int) (((long) pageHeightInChars * height) / jasperPrint.getPageHeight());
        return Math.round(height / charHeight);
    }

    /**
     * Transforms width from pixel space to character space.
     */
    protected int getWidthInChars(int width)
    {
//		return pageWidthInChars * width / jasperPrint.getPageWidth();
        return Math.round(width / charWidth);
    }


    /**
     *
     */
    protected JRStyledText getStyledText(JRPrintText textElement)
    {
        return styledTextUtil.getStyledText(textElement, noneSelector);
    }

    /**
     *
     */
    public String getExporterKey()
    {
        return null;
    }

    /**
     *
     */
    public String getExporterPropertiesPrefix()
    {
        return TXT_EXPORTER_PROPERTIES_PREFIX;
    }
}

