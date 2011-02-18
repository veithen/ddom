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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;

import org.junit.Test;
import org.w3c.dom.Element;

import com.google.code.ddom.utils.test.Validated;

/**
 * Contains SOAP version independent test cases for {@link SOAPBody} and {@link SOAPBodyElement}.
 * 
 * @author Andreas Veithen
 */
public abstract class SOAPBodyTest extends AbstractTestCase {
    public SOAPBodyTest(String soapVersion) {
        super(soapVersion);
    }

    @Validated @Test(expected=SOAPException.class)
    public final void testAddFaultTwice() throws Exception {
        SOAPBody body = createEmptySOAPBody();
        body.addFault();
        body.addFault();
    }
    
    @Validated @Test
    public final void testGetFaultWithCreateElementNS() throws Exception {
        SOAPBody body = createEmptySOAPBody();
        Element faultElement = body.getOwnerDocument().createElementNS(soapVersion, "soap:Fault");
        body.appendChild(faultElement);
        SOAPFault fault = body.getFault();
        // createElementNS actually creates a node of type SOAPFault in this case.
        // Therefore getFault returns the original node.
        assertSame(faultElement, fault);
    }
    
    @Validated @Test
    public final void testHasFault() throws Exception {
        SOAPBody body = createEmptySOAPBody();
        assertFalse(body.hasFault());
        body.addFault();
        assertTrue(body.hasFault());
    }
    
    @Validated @Test
    public final void testAddChildElementUsingTriplet() throws Exception {
        SOAPBody body = createEmptySOAPBody();
        SOAPElement element = body.addChildElement("test", "p", "urn:ns");
        assertTrue(element instanceof SOAPBodyElement);
    }
    
    @Validated @Test
    public final void testAddBodyElementUsingName() throws Exception {
        SOAPEnvelope env = createSOAPEnvelope();
        SOAPBody body = env.addBody();
        SOAPBodyElement element = body.addBodyElement(env.createName("test", "ns", "http://example.org"));
        assertSame(body.getFirstChild(), element);
        assertEquals("test", element.getLocalName());
        assertEquals("ns", element.getPrefix());
        assertEquals("http://example.org", element.getNamespaceURI());
    }
    
    @Validated @Test
    public final void testAddBodyElementUsingQName() throws Exception {
        SOAPBody body = createEmptySOAPBody();
        SOAPBodyElement element = body.addBodyElement(new QName("http://example.org", "test", "ns"));
        assertSame(body.getFirstChild(), element);
        assertEquals("test", element.getLocalName());
        assertEquals("ns", element.getPrefix());
        assertEquals("http://example.org", element.getNamespaceURI());
    }
    
    @Validated @Test
    public final void testAddChildElementFaultUsingTriplet() throws Exception {
        SOAPBody body = createEmptySOAPBody();
        SOAPElement element = body.addChildElement("Fault", "soap-env", soapVersion);
        assertTrue(element instanceof SOAPFault);
    }
    
    @Validated @Test
    public final void testAddBodyElementFaultUsingName() throws Exception {
        SOAPEnvelope env = createSOAPEnvelope();
        SOAPBody body = env.addBody();
        SOAPBodyElement element = body.addBodyElement(env.createName("Fault", "soap-env", soapVersion));
        assertTrue(element instanceof SOAPFault);
    }
    
    @Validated @Test
    public final void testAddBodyElementFaultUsingQName() throws Exception {
        SOAPBody body = createEmptySOAPBody();
        SOAPBodyElement element = body.addBodyElement(new QName(soapVersion, "Fault", "soap-env"));
        assertTrue(element instanceof SOAPFault);
    }
    
    @Validated @Test
    public final void testGetChildElementsReification() {
        SOAPBody body = createEmptySOAPBody();
        body.appendChild(body.getOwnerDocument().createElementNS("urn:test", "p:test"));
        Iterator<?> it = body.getChildElements();
        assertTrue(it.hasNext());
        Object child = it.next();
        assertTrue(child instanceof SOAPBodyElement);
    }
    
    @Validated @Test
    public final void testAddChildElementReification() throws Exception {
        SOAPBody body = createEmptySOAPBody();
        SOAPElement child = body.addChildElement((SOAPElement)body.getOwnerDocument().createElementNS("urn:test", "p:test"));
        assertTrue(child instanceof SOAPBodyElement);
    }
}
