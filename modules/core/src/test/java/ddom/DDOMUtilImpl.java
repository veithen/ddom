package ddom;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.w3c.dom.Document;

public class DDOMUtilImpl implements DOMUtilImpl {
    public static final DDOMUtilImpl INSTANCE = new DDOMUtilImpl();
    
    public Document newDocument() {
        return new DocumentImpl(null);
    }

    public Document parse(boolean namespaceAware, String xml) {
        // TODO: need to cleanup somehow
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        try {
            return new DocumentImpl(factory.createXMLStreamReader(new StringReader(xml)));
        } catch (XMLStreamException ex) {
            throw new Error(ex);
        }
    }
}
