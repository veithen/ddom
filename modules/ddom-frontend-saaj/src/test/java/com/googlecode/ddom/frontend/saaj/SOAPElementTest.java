/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.frontend.saaj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.Text;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class SOAPElementTest {
    @ValidatedTestResource(reference=RISAAJUtil.class, actual=DDOMSAAJUtil.class)
    private SAAJUtil saajUtil;

    /**
     * Test that a namespace declaration added using
     * {@link SOAPElement#addNamespaceDeclaration(String, String)} can be retrieved using
     * {@link Element#getAttributeNS(String, String)}.
     * 
     * @throws SOAPException
     */
    @Validated @Test
    public void testAddNamespaceDeclaration() throws SOAPException {
        SOAPElement element = saajUtil.createSOAPElement("urn:test", "test", "p");
        element.addNamespaceDeclaration("ns", "urn:ns");
        assertEquals("urn:ns", element.getAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "ns"));
    }

    /**
     * Test that a call do {@link SOAPElement#addNamespaceDeclaration(String, String)} for a prefix
     * that is already declared on the same element will replace the existing namespace declaration.
     * 
     * @throws SOAPException
     */
    @Validated @Test
    public void testAddNamespaceDeclarationForExistingPrefix() throws SOAPException {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.addNamespaceDeclaration("p", "urn:ns1");
        element.addNamespaceDeclaration("p", "urn:ns2");
        assertEquals("urn:ns2", element.lookupNamespaceURI("p"));
        assertNull(element.lookupPrefix("urn:ns1"));
        assertEquals("p", element.lookupPrefix("urn:ns2"));
    }
    
    /**
     * Test that {@link SOAPElement#getAttributeValue(Name)} returns <code>null</code> if the
     * requested attribute doesn't exist.
     * 
     * @throws Exception
     */
    @Validated @Test
    public void testGetAttributeValueByNameNonExisting() throws Exception {
        SOAPEnvelope envelope = saajUtil.createSOAP11Envelope();
        SOAPBody body = envelope.addBody();
        SOAPElement element = body.addChildElement("test", "p", "urn:test");
        assertNull(element.getAttributeValue(envelope.createName("attr")));
    }
    
    /**
     * Test the behavior of {@link SOAPElement#getAttributeValue(Name)} for an attribute without
     * namespace.
     * 
     * @throws Exception
     */
    @Validated @Test
    public void testGetAttributeValueByNameWithoutNamespace() throws Exception {
        SOAPEnvelope envelope = saajUtil.createSOAP11Envelope();
        SOAPBody body = envelope.addBody();
        SOAPElement element = body.addChildElement("test", "p", "urn:test");
        element.setAttributeNS(null, "attr", "value");
        assertEquals("value", element.getAttributeValue(envelope.createName("attr")));
    }
    
    /**
     * Test the behavior of {@link SOAPElement#getAttributeValue(Name)} for an attribute with
     * namespace. In particular, check that the prefix is not considered when matching attribute
     * names.
     * 
     * @throws Exception
     */
    @Validated @Test
    public void testGetAttributeValueByNameWithNamespace() throws Exception {
        SOAPEnvelope envelope = saajUtil.createSOAP11Envelope();
        SOAPBody body = envelope.addBody();
        SOAPElement element = body.addChildElement("test", "p", "urn:test");
        element.setAttributeNS("urn:test", "p:attr", "value");
        assertEquals("value", element.getAttributeValue(envelope.createName("attr", "", "urn:test")));
    }
    
    /**
     * Test that {@link SOAPElement#getAllAttributes()} returns the correct {@link Name} for an
     * attribute with a namespace.
     */
    @Validated @Test
    public void testGetAllAttributesWithNamespace() {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.setAttributeNS("urn:ns", "p:test", "value");
        Iterator<?> it = element.getAllAttributes();
        assertTrue(it.hasNext());
        Name name = (Name)it.next();
        assertEquals("urn:ns", name.getURI());
        assertEquals("p", name.getPrefix());
        assertEquals("test", name.getLocalName());
        assertEquals("p:test", name.getQualifiedName());
    }
    
    /**
     * Test that {@link SOAPElement#getAllAttributes()} returns the correct {@link Name} for an
     * attribute without namespace.
     */
    @Validated @Test
    public void testGetAllAttributesWithoutNamespace() {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.setAttributeNS(null, "test", "value");
        Iterator<?> it = element.getAllAttributes();
        assertTrue(it.hasNext());
        Name name = (Name)it.next();
        assertTrue(StringUtils.isEmpty(name.getURI()));
        assertTrue(StringUtils.isEmpty(name.getPrefix()));
        assertEquals("test", name.getLocalName());
        assertEquals("test", name.getQualifiedName());
    }
    
    @Validated @Test
    public void testAddTextNode() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        SOAPElement returnValue = element.addTextNode("text");
        assertSame(element, returnValue);
        assertEquals("text", element.getTextContent());
    }
    
    @Validated @Test
    public void testAddTextNode2() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.addTextNode("ABC");
        element.addTextNode("DEF");
        assertEquals(2, element.getChildNodes().getLength());
        assertEquals("ABCDEF", element.getTextContent());
    }
    
    /**
     * Checks the behavior of {@link SOAPElement#addTextNode(String)} when called with a string that
     * contains a character that is invalid according to the XML specification. The reference
     * implementation doesn't check for invalid characters.
     * 
     * @throws Exception
     */
    @Validated @Test
    public void testAddTextNodeWithInvalidChar() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        String testString = String.valueOf((char)24);
        element.addTextNode(testString);
        assertEquals(testString, element.getTextContent());
    }
    
    @Validated @Test
    public void testAddChildElement() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        SOAPElement child = element.addChildElement("test", "p", "urn:ns");
        assertEquals("urn:ns", child.getNamespaceURI());
        assertEquals("p", child.getPrefix());
        assertEquals("test", child.getLocalName());
    }
    
    @Validated @Test
    public void testGetValueSingleTextChild() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.addTextNode("test");
        assertEquals("test", element.getValue());
    }
    
    @Validated @Test
    public void testGetValueTwoTextChildren() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.addTextNode("foo");
        element.addTextNode("bar");
        assertEquals(2, element.getChildNodes().getLength());
        assertEquals("foobar", element.getValue());
    }
    
    @Validated @Test
    public void testGetValueSingleElementChild() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.appendChild(element.getOwnerDocument().createElementNS("urn:ns", "p:child"));
        assertNull(element.getValue());
    }
    
    @Validated @Test
    public void testGetValueNoChildren() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        assertNull(element.getValue());
    }
    
    @Validated @Test
    public void testGetValueMixedContent() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.addTextNode("foo");
        element.appendChild(element.getOwnerDocument().createElementNS("urn:ns", "p:child"));
        element.addTextNode("bar");
        assertEquals("foo", element.getValue());
    }
    
    @Validated @Test
    public void testSetValueNoChildren() {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        String value = "test";
        element.setValue(value);
        assertEquals(value, element.getValue());
        Node child = element.getFirstChild();
        assertTrue(child instanceof Text);
        assertEquals(value, ((Text)child).getValue());
        assertNull(child.getNextSibling());
    }
    
    @Validated @Test
    public void testSetValueSingleTextChild() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.addTextNode("initial content");
        Text text = (Text)element.getFirstChild();
        String value = "new value";
        element.setValue(value);
        assertEquals(value, text.getValue());
    }
    
    @Validated @Test(expected=IllegalStateException.class)
    public void testSetValueTwoTextChildren() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.addTextNode("foo");
        element.addTextNode("bar");
        element.setValue("test");
    }
    
    @Validated @Test
    public void testGetElementQNameWithNamespace() {
        SOAPElement element = saajUtil.createSOAPElement("urn:test", "test", "p");
        assertEquals(new QName("urn:test", "test", "p"), element.getElementQName());
    }
    
    @Validated @Test
    public void testGetElementQNameWithoutNamespace() {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        assertEquals(new QName("test"), element.getElementQName());
    }

    @Validated @Test
    public void testGetElementQNameWithDefaultNamespace() {
        SOAPElement element = saajUtil.createSOAPElement("urn:test", "test", null);
        assertEquals(new QName("urn:test", "test"), element.getElementQName());
    }
    
    @Validated @Test
    public void testGetElementNameWithNamespace() {
        SOAPElement element = saajUtil.createSOAPElement("urn:test", "test", "p");
        Name name = element.getElementName();
        assertEquals("urn:test", name.getURI());
        assertEquals("test", name.getLocalName());
        assertEquals("p", name.getPrefix());
    }
    
    @Validated @Test
    public void testGetElementNameWithoutNamespace() {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        Name name = element.getElementName();
        assertEquals("", name.getURI());
        assertEquals("test", name.getLocalName());
        assertEquals("", name.getPrefix());
    }

    @Validated @Test
    public void testGetElementNameWithDefaultNamespace() {
        SOAPElement element = saajUtil.createSOAPElement("urn:test", "test", null);
        Name name = element.getElementName();
        assertEquals("urn:test", name.getURI());
        assertEquals("test", name.getLocalName());
        assertEquals("", name.getPrefix());
    }
    
    @Validated @Test
    public void testGetParentElement() throws Exception {
        SOAPElement parent = saajUtil.createSOAPElement(null, "parent", null);
        SOAPElement child = parent.addChildElement(new QName("child"));
        assertSame(parent, child.getParentElement());
    }
    
    @Validated @Test
    public void testGetNamespaceURIForPrefixDeclaredOnParent() throws Exception {
        SOAPElement parent = saajUtil.createSOAPElement("urn:ns", "test", "p");
        parent.addNamespaceDeclaration("p", "urn:ns");
        SOAPElement child = parent.addChildElement(new QName("child"));
        assertEquals("urn:ns", child.getNamespaceURI("p"));
    }
    
    /**
     * Test that {@link SOAPElement#getNamespaceURI(String)} only takes into account explicit
     * namespace declarations. This is in contrast to
     * {@link org.w3c.dom.Node#lookupNamespaceURI(String)}, which also takes into account prefixes
     * used on elements, but for which no explicit namespace declaration exists.
     */
    @Validated @Test
    public void testGetNamespaceURIStrictLookup() {
        SOAPElement element = saajUtil.createSOAPElement("urn:ns", "test", "p");
        assertNull(element.getNamespaceURI("p"));
    }
    
    @Validated @Test
    public void testGetNamespaceURIUnbound() {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        assertNull(element.getNamespaceURI("p"));
    }
    
    @Validated @Test
    public void testGetNamespaceURIDefault() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement("urn:ns", "test", null);
        element.addNamespaceDeclaration("", "urn:ns");
        assertEquals("urn:ns", element.getNamespaceURI(""));
    }
}
