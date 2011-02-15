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
package com.google.code.ddom.frontend.saaj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Element;

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
    
    @Validated @Test
    public void testAddChildElement() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        SOAPElement child = element.addChildElement("test", "p", "urn:ns");
        assertEquals("urn:ns", child.getNamespaceURI());
        assertEquals("p", child.getPrefix());
        assertEquals("test", child.getLocalName());
    }
    
    @Validated @Test @Ignore // TODO
    public void testGetValueSingleTextChild() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.addTextNode("test");
        assertEquals("test", element.getValue());
    }
    
    @Validated @Test @Ignore // TODO
    public void testGetValueTwoTextChildren() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.addTextNode("foo");
        element.addTextNode("bar");
        assertEquals(2, element.getChildNodes().getLength());
        assertEquals("foobar", element.getValue());
    }
    
    @Validated @Test @Ignore // TODO
    public void testGetValueSingleElementChild() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.appendChild(element.getOwnerDocument().createElementNS("urn:ns", "p:child"));
        assertNull(element.getValue());
    }
    
    @Validated @Test @Ignore // TODO
    public void testGetValueNoChildren() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        assertNull(element.getValue());
    }
    
    @Validated @Test @Ignore // TODO
    public void testGetValueMixedContent() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.addTextNode("foo");
        element.appendChild(element.getOwnerDocument().createElementNS("urn:ns", "p:child"));
        element.addTextNode("bar");
        assertEquals("foo", element.getValue());
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
}
