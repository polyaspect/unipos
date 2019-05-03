package unipos.report.components.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


import com.sun.org.apache.xerces.internal.util.DOMUtil;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;


public class DOMUtilExt extends DOMUtil {

    /**
     * l          * Sorts the children of the given node upto the specified depth if
     * l          * available
     * l          *
     * l          * @param node -
     * l          *            node whose children will be sorted
     * l          * @param descending -
     * true for sorting in descending order
     *
     * @param depth      -
     *                   depth upto which to sort in DOM
     * @param comparator -
     *                   comparator used to sort, if null a default NodeName
     *                   comparator is used.
     */
    public static void sortChildNodes(Node node, boolean descending,
                                      int depth, Comparator comparator) {

        List nodes = new ArrayList();
        NodeList childNodeList = node.getChildNodes();
        if (depth > 0 && childNodeList.getLength() > 0) {
            for (int i = 0; i < childNodeList.getLength(); i++) {
                Node tNode = childNodeList.item(i);
                sortChildNodes(tNode, descending, depth - 1,
                        comparator);
                // Remove empty text nodes
                if ((!(tNode instanceof Text))
                        || (tNode instanceof Text && ((Text) tNode)
                        .getTextContent().trim().length() > 1)) {
                    nodes.add(tNode);
                }
            }
            Comparator comp = (comparator != null) ? comparator
                    : new DefaultNodeNameComparator();
            if (descending) {
                //if descending is true, get the reverse ordered comparator
                Collections.sort(nodes, Collections.reverseOrder(comp));
            } else {
                Collections.sort(nodes, comp);
            }

            for (Iterator iter = nodes.iterator(); iter.hasNext(); ) {
                Node element = (Node) iter.next();
                node.appendChild(element);
            }
        }

    }
    public static Element reformatReportNode(Node SourceRootElement, boolean sortXmlTree) throws XPathExpressionException {
        if(sortXmlTree) {
            sortChildNodes(SourceRootElement, false, 1 ,null);
        }

        //Instanciate the new XML Document
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // root elements
        Document document = docBuilder.newDocument();
        Element newRootElement = document.createElement("Report");
        document.appendChild(newRootElement);

        int row = -1;
        NodeList nodeList = SourceRootElement.getChildNodes();

        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NamedNodeMap attributes = node.getAttributes();
            if(Integer.parseInt(attributes.getNamedItem("row").getNodeValue()) == row) {
                continue;
            }
            row = Integer.parseInt(attributes.getNamedItem("row").getNodeValue());
            Element rowElement = newRootElement.getOwnerDocument().createElement("Row");
            rowElement.setAttribute("row", String.valueOf(row));

            XPathExpression expr = xpath.compile("//Field[@row="+row+"]");
            NodeList nl = (NodeList) expr.evaluate(SourceRootElement, XPathConstants.NODESET);
            for(int a = 0; a < nl.getLength(); a++) {
                Node importedNode = document.importNode(nl.item(a), true);
                rowElement.appendChild(importedNode);
            }
            newRootElement.appendChild(rowElement);
        }

        return newRootElement;
    }
}

class DefaultNodeNameComparator implements Comparator {

    public int compare(Object arg0, Object arg1) {
        NamedNodeMap namedNodeMap0 = ((Node)arg0).getAttributes();
        NamedNodeMap namedNodeMap1 = ((Node)arg1).getAttributes();

        int i = Integer.parseInt(namedNodeMap0.getNamedItem("row").getNodeValue())
                -  Integer.parseInt(namedNodeMap1.getNamedItem("row").getNodeValue());
        if (i != 0) return i;

        i = Integer.parseInt(namedNodeMap0.getNamedItem("col").getNodeValue())
                -  Integer.parseInt(namedNodeMap1.getNamedItem("col").getNodeValue());
        return i;
    }

}
