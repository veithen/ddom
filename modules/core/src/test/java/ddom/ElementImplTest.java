package ddom;

import javax.xml.XMLConstants;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@RunWith(DOMTestRunner.class)
public class ElementImplTest {
    @Validated @Test
    public void testGetLocalNameAfterCreateElementWithPrefix() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElement("p:name");
        Assert.assertNull(element.getLocalName());
    }
    
    @Validated @Test
    public void testGetPrefixFromCreateElementWithPrefix() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElement("p:test");
        Assert.assertNull(element.getPrefix());
    }

    @Validated @Test
    public void testGetPrefixFromCreateElementWithoutPrefix() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElement("test");
        Assert.assertNull(element.getPrefix());
    }
    
    @Validated @Test
    public void testGetPrefixFromCreateElementNSWithPrefix() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElementNS("urn:ns", "p:test");
        Assert.assertEquals("p", element.getPrefix());
    }
    
    @Validated @Test
    public void testGetPrefixFromCreateElementNSWithoutPrefix() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElementNS("urn:ns", "test");
        Assert.assertNull(element.getPrefix());
    }
    
    @Validated @Test
    public void testSetPrefixAfterCreateElementWithoutPrefix() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElement("name");
        try {
            element.setPrefix("p");
            Assert.fail("Exception expected");
        } catch (DOMException ex) {
            Assert.assertEquals(DOMException.NAMESPACE_ERR, ex.code);
        }
    }
    
    @Validated @Test
    public void testGetNamespaceURIFromCreateElement() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElement("test");
        Assert.assertNull(element.getNamespaceURI());
    }
    
    @Validated @Test
    public void testGetTagNameFromCreateElementNSWithPrefix() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElementNS("urn:ns", "p:test");
        Assert.assertEquals("p:test", element.getTagName());
    }
    
    @Validated @Test
    public void testGetTagNameFromCreateElementNSWithoutPrefix() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElementNS("urn:ns", "test");
        Assert.assertEquals("test", element.getTagName());
    }
    
    @Validated @Test
    public void testGetTagNameFromNamespaceAwareParseWithPrefix() {
        Document doc = DOMUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Element element = doc.getDocumentElement();
        Assert.assertEquals("p:test", element.getTagName());
    }
    
    @Validated @Test
    public void testGetTagNameFromNonNamespaceAwareParseWithPrefix() {
        Document doc = DOMUtil.parse(false, "<p:test xmlns:p='urn:ns'/>");
        Element element = doc.getDocumentElement();
        Assert.assertEquals("p:test", element.getTagName());
    }
    
    @Validated @Test
    public void testGetAttributeAfterSetAttributeNSWithPrefix() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElement("test");
        element.setAttributeNS("urn:ns", "p:att", "value");
        Assert.assertEquals("value", element.getAttribute("p:att"));
    }
    
    @Validated @Test
    public void testGetAttributeNSAfterSetAttributeWithPrefix() {
        Document doc = DOMUtil.parse(false, "<p:test xmlns:p='urn:ns'/>");
        Element element = doc.getDocumentElement();
        element.setAttribute("p:test", "value");
        Assert.assertEquals("", element.getAttributeNS("urn:ns", "test"));
    }
    
    @Validated @Test
    public void testSetAttributeNSNamespaceDeclaration() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:p", "urn:ns");
        Assert.assertEquals("urn:ns", element.lookupNamespaceURI("p"));
        Assert.assertEquals("p", element.lookupPrefix("urn:ns"));
    }
    
    @Validated @Test
    public void testSetAttributeAndCheckDOMLevel() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.setAttribute("attr", "val");
        Attr attr = element.getAttributeNode("attr");
        // Since the attribute has been created using a DOM 1 method, localName must be null
        Assert.assertNull(attr.getLocalName());
    }
}
