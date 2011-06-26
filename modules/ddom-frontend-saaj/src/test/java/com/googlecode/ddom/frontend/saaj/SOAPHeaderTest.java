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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.code.ddom.utils.test.Validated;

/**
 * Contains SOAP version independent test cases for {@link SOAPHeader}.
 * 
 * @author Andreas Veithen
 */
public abstract class SOAPHeaderTest extends AbstractTestCase {
    public SOAPHeaderTest(String soapVersion) {
        super(soapVersion);
    }

    @Validated @Test
    public final void testGetChildElementsReification() {
        SOAPHeader header = createEmptySOAPHeader();
        header.appendChild(header.getOwnerDocument().createElementNS("urn:test", "p:test"));
        Iterator<?> it = header.getChildElements();
        assertTrue(it.hasNext());
        Object child = it.next();
        assertTrue(child instanceof SOAPHeaderElement);
    }
    
    // TODO: same tests for other addChildElement variants!
    @Validated @Test
    public final void testAddChildElementReification() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        SOAPElement child = header.addChildElement((SOAPElement)header.getOwnerDocument().createElementNS("urn:test", "p:test"));
        assertTrue(child instanceof SOAPHeaderElement);
    }
    
    @Validated @Test(expected=SOAPException.class) @Ignore // TODO: later
    public final void testAddChildElementWithoutNamespace() throws Exception {
        createEmptySOAPHeader().addChildElement("test");
    }
    
    @Validated @Test
    public final void testAddChildElementUsingTriplet() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        SOAPElement element = header.addChildElement("test", "p", "urn:ns");
        assertTrue(element instanceof SOAPHeaderElement);
    }
    
    @Validated @Test
    public final void testAddHeaderElementUsingName() throws Exception {
        SOAPEnvelope env = createSOAPEnvelope();
        SOAPHeader header = env.addHeader();
        SOAPHeaderElement element = header.addHeaderElement(env.createName("test", "ns", "http://example.org"));
        assertSame(header.getFirstChild(), element);
        assertEquals("test", element.getLocalName());
        assertEquals("ns", element.getPrefix());
        assertEquals("http://example.org", element.getNamespaceURI());
    }
    
    @Validated @Test
    public final void testAddHeaderElementUsingQName() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        SOAPHeaderElement element = header.addHeaderElement(new QName("http://example.org", "test", "ns"));
        assertSame(header.getFirstChild(), element);
        assertEquals("test", element.getLocalName());
        assertEquals("ns", element.getPrefix());
        assertEquals("http://example.org", element.getNamespaceURI());
    }
    
    @Validated @Test
    public final void testExamineAllHeaderElementsReification() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        Document document = header.getOwnerDocument();
        header.appendChild(document.createElementNS("urn:ns", "p:header1"));
        header.appendChild(document.createElementNS("urn:ns", "p:header2"));
        Iterator it = header.examineAllHeaderElements();
        
        assertTrue(it.hasNext());
        Element headerElement = (Element)it.next();
        assertTrue(headerElement instanceof SOAPHeaderElement);
        assertEquals("urn:ns", headerElement.getNamespaceURI());
        assertEquals("header1", headerElement.getLocalName());
        assertEquals("p", headerElement.getPrefix());
        
        assertTrue(it.hasNext());
        headerElement = (Element)it.next();
        assertTrue(headerElement instanceof SOAPHeaderElement);
        assertEquals("urn:ns", headerElement.getNamespaceURI());
        assertEquals("header2", headerElement.getLocalName());
        assertEquals("p", headerElement.getPrefix());
        
        assertFalse(it.hasNext());
    }
    
    @Validated @Test
    public final void testExamineHeaderElements() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        
        SOAPHeaderElement element1 = header.addHeaderElement(new QName("urn:ns", "header1"));
        element1.setActor("urn:actor1");
        element1.setMustUnderstand(true);
        
        SOAPHeaderElement element2 = header.addHeaderElement(new QName("urn:ns", "header2"));
        element2.setActor("urn:actor2");
        element2.setMustUnderstand(true);
        
        SOAPHeaderElement element3 = header.addHeaderElement(new QName("urn:ns", "header3"));
        element3.setActor("urn:actor1");
        element3.setMustUnderstand(false);
        
        Iterator it = header.examineHeaderElements("urn:actor1");
        assertTrue(it.hasNext());
        assertSame(element1, it.next());
        assertTrue(it.hasNext());
        assertSame(element3, it.next());
        assertFalse(it.hasNext());
    }
    
    @Validated @Test
    public final void testExamineMustUnderstandHeaderElements() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        
        SOAPHeaderElement element1 = header.addHeaderElement(new QName("urn:ns", "header1"));
        element1.setActor("urn:actor1");
        element1.setMustUnderstand(true);
        
        SOAPHeaderElement element2 = header.addHeaderElement(new QName("urn:ns", "header2"));
        element2.setActor("urn:actor2");
        element2.setMustUnderstand(true);
        
        SOAPHeaderElement element3 = header.addHeaderElement(new QName("urn:ns", "header3"));
        element3.setActor("urn:actor2");
        element3.setMustUnderstand(false);
        
        SOAPHeaderElement element4 = header.addHeaderElement(new QName("urn:ns", "header4"));
        element4.setActor("urn:actor2");
        element4.setMustUnderstand(true);
        
        header.addHeaderElement(new QName("urn:ns", "header5"));
        
        Iterator it = header.examineMustUnderstandHeaderElements("urn:actor2");
        assertTrue(it.hasNext());
        assertSame(element2, it.next());
        assertTrue(it.hasNext());
        assertSame(element4, it.next());
        assertFalse(it.hasNext());
    }
    
    @Validated @Test
    public void testExtractAllHeaderElements() throws Exception {
        SOAPEnvelope env = createSOAPEnvelope();
        SOAPHeader header = env.addHeader();
        SOAPHeaderElement headerElement1 = header.addHeaderElement(new QName("urn:ns1", "test1", "p1"));
        SOAPHeaderElement headerElement2 = header.addHeaderElement(new QName("urn:ns2", "test2", "p2"));
        Iterator it = header.extractAllHeaderElements();
        assertTrue(it.hasNext());
        assertSame(headerElement1, it.next());
        assertTrue(it.hasNext());
        assertSame(headerElement2, it.next());
        assertFalse(it.hasNext());
        assertEquals(0, header.getChildNodes().getLength());
    }
    
    @Validated @Test
    public void testExtractAllHeaderElementsPartialConsumption() throws Exception {
        SOAPEnvelope env = createSOAPEnvelope();
        SOAPHeader header = env.addHeader();
        header.addHeaderElement(new QName("urn:ns1", "test1", "p1"));
        header.addHeaderElement(new QName("urn:ns2", "test2", "p2"));
        header.addHeaderElement(new QName("urn:ns3", "test3", "p3"));
        Iterator it = header.extractAllHeaderElements();
        it.next();
        assertEquals(0, header.getChildNodes().getLength());
    }
    
    @Validated @Test
    public final void testExtractHeaderElementsPartialConsumption() throws Exception {
        SOAPEnvelope env = createSOAPEnvelope();
        SOAPHeader header = env.addHeader();
        SOAPHeaderElement element1 = header.addHeaderElement(env.createName("test", "p", "urn:ns1"));
        element1.setActor("urn:my-actor");
        SOAPHeaderElement element2 = header.addHeaderElement(env.createName("test", "p", "urn:ns2"));
        element2.setActor("urn:my-actor");
        Iterator it = header.extractHeaderElements("urn:my-actor");
        // Only consume the first element from the iterator
        assertTrue(it.hasNext());
        assertSame(element1, it.next());
        // Although only one element has been consumed, all matching elements have been removed
        assertNull(header.getFirstChild());
    }
}
