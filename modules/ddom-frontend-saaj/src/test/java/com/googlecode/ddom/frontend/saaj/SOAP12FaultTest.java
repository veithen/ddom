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

import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPFaultElement;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;

import com.google.code.ddom.utils.test.Validated;

public class SOAP12FaultTest extends SOAPFaultTest {
    public SOAP12FaultTest() {
        super(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
    }

    @Override
    protected void checkFaultCodeElement(SOAPFaultElement element) {
        assertEquals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, element.getNamespaceURI());
        assertEquals("Code", element.getLocalName());
    }

    @Override
    protected void checkFaultStringElement(SOAPFaultElement element) {
        assertEquals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, element.getNamespaceURI());
        assertEquals("Reason", element.getLocalName());
    }
    
    @Validated @Test
    public void testSetFaultNode() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        fault.setFaultNode("urn:mynode");
        Element child = (Element)fault.getFirstChild();
        assertEquals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, child.getNamespaceURI());
        assertEquals("Node", child.getLocalName());
    }
    
    @Validated @Test
    public void testGetFaultNode() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        fault.setFaultNode("urn:mynode");
        assertEquals("urn:mynode", fault.getFaultNode());
    }
    
    @Validated @Test
    public void testGetFaultNodeNotPresent() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        assertNull(fault.getFaultNode());
    }
}
