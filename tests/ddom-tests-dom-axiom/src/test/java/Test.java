import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axiom.om.OMElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.model.ModelBuilder;

public class Test {
    @org.junit.Test
    public void test() throws Exception {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.addFrontend("dom");
        modelBuilder.addFrontend("axiom");
        Document document = (Document)DocumentHelper.newInstance().newDocument(modelBuilder.buildModelDefinition());
        Element element = document.createElementNS("urn:test", "p:root");
        ((OMElement)element).addAttribute("attr", "test", null);
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(element), new StreamResult(System.out));
    }
}
