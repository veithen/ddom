package com.google.code.ddom.dom.impl;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@RunWith(DOMTestRunner.class)
public class AttrImplTest {
    @Validated @Test
    public void testGetNextSibling() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElement("test");
        element.setAttribute("attr1", "val");
        element.setAttribute("attr2", "val");
        Attr attr = (Attr)element.getAttributes().item(0);
        // The attributes of an element are not siblings of each other, so getNextSibling must return null
        Assert.assertNull(attr.getNextSibling());
    }
    
    @Validated @Test
    public void testGetLocalNameAfterCreateAttributeWithPrefix() {
        Document doc = DOMUtil.newDocument();
        Attr attr = doc.createAttribute("p:name");
        Assert.assertNull(attr.getLocalName());
    }
    
    @Validated @Test
    public void testSetPrefixAfterCreateAttributeWithoutPrefix() {
        Document doc = DOMUtil.newDocument();
        Attr attr = doc.createAttribute("name");
        try {
            attr.setPrefix("p");
            Assert.fail("Exception expected");
        } catch (DOMException ex) {
            Assert.assertEquals(DOMException.NAMESPACE_ERR, ex.code);
        }
    }
}
