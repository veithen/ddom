/*
 * Copyright 2009 Andreas Veithen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.ddom.frontend.dom;

import javax.xml.XMLConstants;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class ElementTest {
    @ValidatedTestResource(reference=XercesDOMUtilImpl.class, actual=DDOMUtilImpl.class)
    private DOMUtilImpl domUtil;
    
    @Validated @Test
    public void testGetLocalNameAfterCreateElementWithPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("p:name");
        Assert.assertNull(element.getLocalName());
    }
    
    @Validated @Test
    public void testGetPrefixFromCreateElementWithPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("p:test");
        Assert.assertNull(element.getPrefix());
    }

    @Validated @Test
    public void testGetPrefixFromCreateElementWithoutPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("test");
        Assert.assertNull(element.getPrefix());
    }
    
    @Validated @Test
    public void testGetPrefixFromCreateElementNSWithPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS("urn:ns", "p:test");
        Assert.assertEquals("p", element.getPrefix());
    }
    
    @Validated @Test
    public void testGetPrefixFromCreateElementNSWithoutPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS("urn:ns", "test");
        Assert.assertNull(element.getPrefix());
    }
    
    @Validated @Test
    public void testSetPrefixAfterCreateElementWithoutPrefix() {
        Document doc = domUtil.newDocument();
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
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("test");
        Assert.assertNull(element.getNamespaceURI());
    }
    
    @Validated @Test
    public void testGetTagNameFromCreateElementNSWithPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS("urn:ns", "p:test");
        Assert.assertEquals("p:test", element.getTagName());
    }
    
    @Validated @Test
    public void testGetTagNameFromCreateElementNSWithoutPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS("urn:ns", "test");
        Assert.assertEquals("test", element.getTagName());
    }
    
    @Validated @Test
    public void testGetTagNameFromNamespaceAwareParseWithPrefix() {
        Document doc = domUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Element element = doc.getDocumentElement();
        Assert.assertEquals("p:test", element.getTagName());
    }
    
    @Validated @Test
    public void testGetTagNameFromNonNamespaceAwareParseWithPrefix() {
        Document doc = domUtil.parse(false, "<p:test xmlns:p='urn:ns'/>");
        Element element = doc.getDocumentElement();
        Assert.assertEquals("p:test", element.getTagName());
    }
    
    @Validated @Test
    public void testGetAttributeAfterSetAttributeNSWithPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("test");
        element.setAttributeNS("urn:ns", "p:att", "value");
        Assert.assertEquals("value", element.getAttribute("p:att"));
    }
    
    @Validated @Test
    public void testGetAttributeNSAfterSetAttributeWithPrefix() {
        Document doc = domUtil.parse(false, "<p:test xmlns:p='urn:ns'/>");
        Element element = doc.getDocumentElement();
        element.setAttribute("p:test", "value");
        Assert.assertEquals("", element.getAttributeNS("urn:ns", "test"));
    }
    
    /**
     * @see DocumentTest#testCreateAttributeNSNamespaceDeclaration()
     */
    @Validated @Test
    public void testSetAttributeNSNamespaceDeclaration() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:p", "urn:ns");
        Assert.assertEquals("urn:ns", element.lookupNamespaceURI("p"));
        Assert.assertEquals("p", element.lookupPrefix("urn:ns"));
    }
    
    @Validated @Test
    public void testSetAttributeAndCheckDOMLevel() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.setAttribute("attr", "val");
        Attr attr = element.getAttributeNode("attr");
        // Since the attribute has been created using a DOM 1 method, localName must be null
        Assert.assertNull(attr.getLocalName());
    }
    
    @Validated @Test(expected=NullPointerException.class)
    public void testInsertBeforeWithNullNewChild() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        Text child = doc.createTextNode("test");
        element.appendChild(child);
        element.insertBefore(null, child);
    }

    @Validated @Test(expected=NullPointerException.class)
    public void testAppendChildWithNullArgument() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.appendChild(null);
    }
    
    @Validated @Test(expected=NullPointerException.class)
    public void testRemoveChildWithNullArgument() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.removeChild(null);
    }

    @Validated @Test(expected=NullPointerException.class)
    public void testReplaceChildWithNullNewChild() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        Text child = doc.createTextNode("test");
        element.appendChild(child);
        element.replaceChild(null, child);
    }

    @Validated @Test(expected=NullPointerException.class)
    public void testReplaceChildWithNullOldChild() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.replaceChild(doc.createTextNode("test"), null);
    }
    
    @Validated @Test
    public void testGetTextContent() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.appendChild(doc.createTextNode("te"));
        element.appendChild(doc.createTextNode("st"));
        Assert.assertEquals("test", element.getTextContent());
    }
    
    /**
     * Test that invoking {@link Element#removeAttribute(String)} has no effect if there is no
     * attribute with the given name. This is not covered by the DOM Conformance Test Suite.
     */
    @Validated @Test
    public void testRemoveAttributeNotExisting() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("test");
        element.removeAttribute("unknown");
    }
    
    /**
     * Test that invoking {@link Element#removeAttributeNS(String, String)} has no effect if there is no
     * attribute with the given name. This is not covered by the DOM Conformance Test Suite.
     */
    @Validated @Test
    public void testRemoveAttributeNSNotExisting() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.removeAttributeNS("urn:some:namespace", "unknown");
    }
}
