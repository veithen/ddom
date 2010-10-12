/*
 * Copyright 2009-2010 Andreas Veithen
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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;

import com.google.code.ddom.utils.test.Validated;

public abstract class SOAPEnvelopeTest extends AbstractTestCase {
    public SOAPEnvelopeTest(String soapVersion) {
        super(soapVersion);
    }

    @Validated @Test @Ignore
    public final void testName() {
        SOAPEnvelope envelope = createSOAPEnvelope();
        assertEquals("http://schemas.xmlsoap.org/soap/envelope/", envelope.getNamespaceURI());
        assertEquals("Envelope", envelope.getLocalName());
    }
    
    @Validated @Test
    public final void testGetHeaderWithCreateElementNS() throws Exception {
        SOAPEnvelope envelope = createSOAPEnvelope();
        Element headerElement = envelope.getOwnerDocument().createElementNS(soapVersion, "soap:Header");
        envelope.appendChild(headerElement);
        SOAPHeader header = envelope.getHeader();
        assertSame(headerElement, header);
    }
    
    @Validated @Test(expected=SOAPException.class)
    public final void testAddHeaderTwice() throws Exception {
        SOAPEnvelope envelope = createSOAPEnvelope();
        envelope.addHeader();
        envelope.addHeader();
    }
    
    @Validated @Test
    public final void testGetBodyWithCreateElementNS() throws Exception {
        SOAPEnvelope envelope = createSOAPEnvelope();
        Element bodyElement = envelope.getOwnerDocument().createElementNS(soapVersion, "soap:Body");
        envelope.appendChild(bodyElement);
        SOAPBody body = envelope.getBody();
        assertSame(bodyElement, body);
    }
    
    @Validated @Test(expected=SOAPException.class)
    public final void testAddBodyTwice() throws Exception {
        SOAPEnvelope envelope = createSOAPEnvelope();
        envelope.addBody();
        envelope.addBody();
    }
    
    @Validated @Test
    public final void testCreateName() throws Exception {
        SOAPEnvelope envelope = createSOAPEnvelope();
        Name name = envelope.createName("test", "p", "urn:ns1");
        assertEquals("test", name.getLocalName());
        assertEquals("p", name.getPrefix());
        assertEquals("urn:ns1", name.getURI());
        assertEquals("p:test", name.getQualifiedName());
    }
    
    @Validated @Test
    public final void testCreateNameWithEmptyPrefix() throws Exception {
        SOAPEnvelope envelope = createSOAPEnvelope();
        Name name = envelope.createName("test", "", "urn:ns1");
        assertEquals("test", name.getLocalName());
        assertEquals("", name.getPrefix());
        assertEquals("urn:ns1", name.getURI());
        assertEquals("test", name.getQualifiedName());
    }
    
    @Validated @Test
    public final void testCreateNameWithNullPrefix() throws Exception {
        SOAPEnvelope envelope = createSOAPEnvelope();
        Name name = envelope.createName("test", null, "urn:ns1");
        assertEquals("test", name.getLocalName());
        assertEquals("", name.getPrefix());
        assertEquals("urn:ns1", name.getURI());
        assertEquals("test", name.getQualifiedName());
    }
    
    @Validated @Test
    public final void testCreateNameWithoutNamespace() throws Exception {
        SOAPEnvelope envelope = createSOAPEnvelope();
        Name name = envelope.createName("test", null, null);
        assertEquals("test", name.getLocalName());
        assertEquals("", name.getPrefix());
        assertEquals("", name.getURI());
        assertEquals("test", name.getQualifiedName());
    }
    
    @Validated @Test
    public final void testCreateName2() throws Exception {
        SOAPEnvelope envelope = createSOAPEnvelope();
        Name name = envelope.createName("test");
        assertEquals("test", name.getLocalName());
        assertEquals("", name.getPrefix());
        assertEquals("", name.getURI());
        assertEquals("test", name.getQualifiedName());
    }
    
    @Validated @Test @Ignore
    public final void testHeaderElements() throws Exception {
        SOAPEnvelope envelope = createSOAPEnvelope();
        SOAPHeader header = envelope.addHeader();

        SOAPHeaderElement headerEle = header.addHeaderElement(envelope.createName("foo1",
                                                                                  "f1",
                                                                                  "foo1-URI"));
        headerEle.setActor("actor-URI");
        headerEle.setMustUnderstand(true);

        Iterator iterator = header.extractHeaderElements("actor-URI");
        int cnt = 0;
        while (iterator.hasNext()) {
            cnt++;
            SOAPHeaderElement resultHeaderEle = (SOAPHeaderElement)iterator.next();

            assertEquals(headerEle.getActor(), resultHeaderEle.getActor());
            assertEquals(resultHeaderEle.getMustUnderstand(), headerEle.getMustUnderstand());
        }
        assertTrue(cnt == 1);
        iterator = header.extractHeaderElements("actor-URI");
        assertTrue(!iterator.hasNext());
    }
}
