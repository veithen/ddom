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
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPFaultElement;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Node;

import com.google.code.ddom.utils.test.Validated;

public abstract class SOAPFaultTest extends AbstractTestCase {
    public SOAPFaultTest(String soapVersion) {
        super(soapVersion);
    }
    
    protected abstract void checkFaultCodeElement(SOAPFaultElement element);
    protected abstract void checkFaultStringElement(SOAPFaultElement element);
    
    @Validated @Test @Ignore // TODO
    public void testSetFaultCode() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        System.out.println(fault.getPrefix());
        fault.setFaultCode("SOAP-ENV:Server");
        Node child = fault.getFirstChild();
        assertTrue(child instanceof SOAPFaultElement);
        checkFaultCodeElement((SOAPFaultElement)child);
    }
    
    @Validated @Test
    public void testSetFaultString() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        fault.setFaultString("test");
        Node child = fault.getFirstChild();
        assertTrue(child instanceof SOAPFaultElement);
        checkFaultStringElement((SOAPFaultElement)child);
    }
    
    @Validated @Test(expected=SOAPException.class)
    public void testAddDetailTwice() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        fault.addDetail();
        fault.addDetail();
    }
    
    @Validated @Test
    public void testCreateDetailEntryUsingCreateElementNS() throws Exception {
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
