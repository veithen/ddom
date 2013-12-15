/*
 * Copyright 2009-2013 Andreas Veithen
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
package com.googlecode.ddom.frontend.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.xml.XMLConstants;

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
    @ValidatedTestResource(reference=XercesDOMUtil.class, actual=DDOMUtil.class)
    private DOMUtil domUtil;
    
    @Validated @Test
    public void testGetLocalNameAfterCreateElementWithPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("p:name");
        assertNull(element.getLocalName());
    }
    
    @Validated @Test
    public void testGetPrefixFromCreateElementWithPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("p:test");
        assertNull(element.getPrefix());
    }

    @Validated @Test
    public void testGetPrefixFromCreateElementWithoutPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("test");
        assertNull(element.getPrefix());
    }
    
    @Validated @Test
    public void testGetPrefixFromCreateElementNSWithPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS("urn:ns", "p:test");
        assertEquals("p", element.getPrefix());
    }
    
    @Validated @Test
    public void testGetPrefixFromCreateElementNSWithoutPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS("urn:ns", "test");
        assertNull(element.getPrefix());
    }
    
    @Validated @Test
    public void testSetPrefixAfterCreateElementWithoutPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("name");
        try {
            element.setPrefix("p");
            fail("Exception expected");
        } catch (DOMException ex) {
            assertEquals(DOMException.NAMESPACE_ERR, ex.code);
        }
    }
    
    @Validated @Test
    public void testSetPrefixEmpty() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.setPrefix("");
        assertNull(element.getPrefix());
    }
    
    @Validated @Test
    public void testGetNamespaceURIFromCreateElement() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("test");
        assertNull(element.getNamespaceURI());
    }
    
    @Validated @Test
    public void testGetTagNameFromCreateElementNSWithPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS("urn:ns", "p:test");
        assertEquals("p:test", element.getTagName());
    }
    
    @Validated @Test
    public void testGetTagNameFromCreateElementNSWithoutPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS("urn:ns", "test");
        assertEquals("test", element.getTagName());
    }
    
    @Validated @Test
    public void testGetTagNameFromNamespaceAwareParseWithPrefix() {
        Document doc = domUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Element element = doc.getDocumentElement();
        assertEquals("p:test", element.getTagName());
    }
    
    @Validated @Test
    public void testGetTagNameFromNonNamespaceAwareParseWithPrefix() {
        Document doc = domUtil.parse(false, "<p:test xmlns:p='urn:ns'/>");
        Element element = doc.getDocumentElement();
        assertEquals("p:test", element.getTagName());
    }
    
    @Validated @Test
    public void testGetAttributeAfterSetAttributeNSWithPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("test");
        element.setAttributeNS("urn:ns", "p:att", "value");
        assertEquals("value", element.getAttribute("p:att"));
    }
    
    @Validated @Test
    public void testGetAttributeNSAfterSetAttributeWithoutPrefix() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("test");
        element.setAttribute("att", "value");
        assertEquals("value", element.getAttributeNS(null, "att"));
    }
    
    @Validated @Test
    public void testGetAttributeNSAfterSetAttributeWithPrefix() {
        Document doc = domUtil.parse(false, "<p:test xmlns:p='urn:ns'/>");
        Element element = doc.getDocumentElement();
        element.setAttribute("p:test", "value");
        assertEquals("", element.getAttributeNS("urn:ns", "test"));
    }
    
    /**
     * @see DocumentTest#testCreateAttributeNSNamespaceDeclaration()
     */
    @Validated @Test
    public void testSetAttributeNSNamespaceDeclaration() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:p", "urn:ns");
        assertEquals("urn:ns", element.lookupNamespaceURI("p"));
        assertEquals("p", element.lookupPrefix("urn:ns"));
    }
    
    @Validated @Test
    public void testSetAttributeAndCheckDOMLevel() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.setAttribute("attr", "val");
        Attr attr = element.getAttributeNode("attr");
        // Since the attribute has been created using a DOM 1 method, localName must be null
        assertNull(attr.getLocalName());
    }
    
    @Validated @Test
    public void testSetAttributeNodeNSReplacingNamespaceDeclaration() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        Attr attr1 = doc.createAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:p");
        attr1.setValue("urn:ns1");
        Attr attr2 = doc.createAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:p");
        attr2.setValue("urn:ns2");
        element.setAttributeNodeNS(attr1);
        element.setAttributeNodeNS(attr2);
        assertEquals(1, element.getAttributes().getLength());
    }
    
    @Validated @Test
    public void testSetIdAttributeNSWithoutNamespace() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.setAttributeNS(null, "id", "#myId");
        element.setIdAttributeNS(null, "id", true);
        Attr attr = (Attr)element.getAttributes().item(0);
        assertTrue(attr.isId());
        assertEquals("id", attr.getLocalName());
        assertNull(attr.getNamespaceURI());
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
        assertEquals("test", element.getTextContent());
    }
    
    @Validated @Test
    public void testGetTextContentWithCommentChild() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.appendChild(doc.createTextNode("a"));
        element.appendChild(doc.createComment("b"));
        element.appendChild(doc.createTextNode("c"));
        assertEquals("ac", element.getTextContent());
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
    
    @Validated @Test
    public void testCloneNodeWithNamespaceDeclaration() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:p", "urn:test");
        Element clone = (Element)element.cloneNode(true);
        assertEquals("urn:test", clone.getAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "p"));
    }
    
    @Validated @Test
    public void testGetElementsByTagNameWithNSUnawareElements() {
        Document doc = domUtil.parse(false, "<root><a/><b><a/></b><a/></root>");
        assertEquals(3, doc.getElementsByTagName("a").getLength());
    }
}
