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

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPFaultElement;

import org.junit.Test;
import org.w3c.dom.Node;

import com.google.code.ddom.utils.test.Validated;

/**
 * Contains SOAP version independent test cases for {@link SOAPFault}, {@link SOAPFaultElement},
 * {@link Detail} and {@link DetailEntry}.
 * 
 * @author Andreas Veithen
 */
public abstract class SOAPFaultTest extends AbstractTestCase {
    public SOAPFaultTest(String soapVersion) {
        super(soapVersion);
    }
    
    protected abstract SOAPElement appendFaultCodeElement(SOAPFault fault) throws SOAPException;
    protected abstract void checkFaultCodeElement(SOAPFaultElement element);
    protected abstract void checkFaultStringElement(SOAPFaultElement element);
    
    @Validated @Test
    public final void testGetFaultCode() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        String code = fault.getPrefix() + ":Server";
        appendFaultCodeElement(fault).setTextContent(code);
        assertEquals(code, fault.getFaultCode());
    }

    /**
     * Test the behavior of {@link SOAPFault#getFaultCode()} if no fault code is present. Note that
     * the reference implementation throws a {@link NullPointerException} in this case and that the
     * SAAJ specification is not clear about the expected behavior. Our implementation returns
     * <code>null</code>.
     * 
     * @throws Exception
     */
    @Test
    public final void testGetFaultCodeOnEmptyFault() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        assertNull(fault.getFaultCode());
    }

    @Validated @Test(expected=SOAPException.class)
    public final void testSetFaultCodeAsStringWithUnboundPrefix() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        fault.setFaultCode("unbound:test");
    }
    
    @Validated @Test
    public final void testSetFaultString() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        fault.setFaultString("test");
        Node child = fault.getFirstChild();
        assertTrue(child instanceof SOAPFaultElement);
        checkFaultStringElement((SOAPFaultElement)child);
    }
    
    @Validated @Test(expected=SOAPException.class)
    public final void testAddDetailTwice() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        fault.addDetail();
        fault.addDetail();
    }
    
    @Validated @Test
    public final void testAddDetailEntryUsingQName() throws Exception {
        Detail detail = createEmptySOAPFault().addDetail();
        DetailEntry detailEntry = detail.addDetailEntry(new QName("urn:ns", "mydetail", "p"));
        assertEquals("urn:ns", detailEntry.getNamespaceURI());
        assertEquals("mydetail", detailEntry.getLocalName());
        assertEquals("p", detailEntry.getPrefix());
        assertSame(detailEntry, detail.getFirstChild());
    }
    
    @Validated @Test
    public final void testAddDetailEntryUsingQNameWithoutNamespace() throws Exception {
        Detail detail = createEmptySOAPFault().addDetail();
        DetailEntry detailEntry = detail.addDetailEntry(new QName("mydetail"));
        // TODO: Sun's implementation returns an empty string here (which I believe is incorrect)
//        assertNull(detailEntry.getNamespaceURI());
        assertEquals("mydetail", detailEntry.getLocalName());
        assertNull(detailEntry.getPrefix());
        assertSame(detailEntry, detail.getFirstChild());
    }
    
    @Validated @Test
    public final void testAddDetailEntryUsingName() throws Exception {
        SOAPEnvelope envelope = createSOAPEnvelope();
        Detail detail = envelope.addBody().addFault().addDetail();
        DetailEntry detailEntry = detail.addDetailEntry(envelope.createName("mydetail", "p", "urn:ns"));
        assertEquals("urn:ns", detailEntry.getNamespaceURI());
        assertEquals("mydetail", detailEntry.getLocalName());
        assertEquals("p", detailEntry.getPrefix());
        assertSame(detailEntry, detail.getFirstChild());
    }
    
    @Validated @Test
    public final void testCreateDetailEntryUsingCreateElementNS() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        Detail detail = fault.addDetail();
        detail.appendChild(fault.getOwnerDocument().createElementNS("urn:ns", "p:test"));
        Iterator<?> it = detail.getDetailEntries();
        assertTrue(it.hasNext());
        // The implementation silently replaces the Element by a DetailEntry
        DetailEntry detailEntry = (DetailEntry)it.next();
        assertEquals("urn:ns", detailEntry.getNamespaceURI());
        assertEquals("test", detailEntry.getLocalName());
    }
}
