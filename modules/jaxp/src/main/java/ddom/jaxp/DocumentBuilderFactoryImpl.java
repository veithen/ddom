package ddom.jaxp;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;

public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {
    @Override
    public Object getAttribute(String name) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getFeature(String name) throws ParserConfigurationException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        Map<String,Object> props = new HashMap<String,Object>();
        props.put(XMLInputFactory.IS_VALIDATING, isValidating());
        props.put(XMLInputFactory.IS_NAMESPACE_AWARE, isNamespaceAware());
// TODO       private boolean whitespace = false;
        props.put(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, isExpandEntityReferences());
        props.put(XMLInputFactory.IS_COALESCING, isCoalescing());
        return new DocumentBuilderImpl(/*isNamespaceAware(),*/ isIgnoringComments(), props);
    }

    @Override
    public void setAttribute(String name, Object value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFeature(String name, boolean value) throws ParserConfigurationException {
        // TODO Auto-generated method stub
        
    }
}
