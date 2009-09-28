package ddom;

import javax.xml.XMLConstants;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;

@RunWith(DOMTestRunner.class)
public class NSDeclTest {
    @Validated @Test
    public void testGetNamespaceURI() {
        Document doc = DOMUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, nsAttr.getNamespaceURI());
    }

    @Validated @Test
    public void testGetLocalName() {
        Document doc = DOMUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals("p", nsAttr.getLocalName());
    }

    @Validated @Test
    public void testGetPrefix() {
        Document doc = DOMUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals(XMLConstants.XMLNS_ATTRIBUTE, nsAttr.getPrefix());
    }

    @Validated @Test
    public void testGetName() {
        Document doc = DOMUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals("xmlns:p", nsAttr.getName());
    }

    @Validated @Test
    public void testGetNamespaceURIForDefaultNamespace() {
        Document doc = DOMUtil.parse(true, "<test xmlns='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, nsAttr.getNamespaceURI());
    }

    @Validated @Test
    public void testGetLocalNameForDefaultNamespace() {
        Document doc = DOMUtil.parse(true, "<test xmlns='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals(XMLConstants.XMLNS_ATTRIBUTE, nsAttr.getLocalName());
    }

    @Validated @Test
    public void testGetPrefixForDefaultNamespace() {
        Document doc = DOMUtil.parse(true, "<test xmlns='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertNull(nsAttr.getPrefix());
    }

    @Validated @Test
    public void testGetNameForDefaultNamespace() {
        Document doc = DOMUtil.parse(true, "<test xmlns='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals(XMLConstants.XMLNS_ATTRIBUTE, nsAttr.getName());
    }
}
