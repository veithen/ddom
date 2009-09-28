package ddom;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XercesDOMUtilImpl implements DOMUtilImpl {
    public static final XercesDOMUtilImpl INSTANCE = new XercesDOMUtilImpl();
    
    public Document newDocument() {
        try {
            DocumentBuilderFactory factory = new DocumentBuilderFactoryImpl();
            return factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException ex) {
            throw new Error(ex);
        }
    }
    
    public Document parse(boolean namespaceAware, String xml) {
        try {
            DocumentBuilderFactory factory = new DocumentBuilderFactoryImpl();
            factory.setNamespaceAware(namespaceAware);
            return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (SAXException ex) {
            throw new Error(ex);
        } catch (IOException ex) {
            throw new Error(ex);
        } catch (ParserConfigurationException ex) {
            throw new Error(ex);
        }
    }
}
