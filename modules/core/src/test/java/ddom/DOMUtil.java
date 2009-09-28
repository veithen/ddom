package ddom;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DOMUtil {
    public static DOMUtilImpl impl;
    
    private DOMUtil() {}
    
    public static Document newDocument() {
        return impl.newDocument();
    }
    
    public static Document parse(boolean namespaceAware, String xml) {
        return impl.parse(namespaceAware, xml);
    }
    
    public static String toString(Node node) throws TransformerException {
        StringWriter out = new StringWriter();
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.transform(new DOMSource(node), new StreamResult(out));
        return out.toString();
    }
}
