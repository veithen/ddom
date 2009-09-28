package ddom.utils.dom.iterator;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DOMTestUtil {
    public static Document toDOM(String xml) throws SAXException {
        try {
            DocumentBuilderFactory factory = new DocumentBuilderFactoryImpl();
            factory.setNamespaceAware(true);
            return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (IOException ex) {
            // StringReader should never cause IOException
            throw new Error(ex);
        } catch (ParserConfigurationException ex) {
            // We should never get here
            throw new Error(ex);
        }
    }
}
