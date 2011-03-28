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

import static org.junit.Assert.assertTrue;

import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPFaultElement;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

import com.google.code.ddom.utils.test.Validated;

public class SOAP11FaultTest extends SOAPFaultTest {
    public SOAP11FaultTest() {
        super(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE);
    }

    @Override
    protected void checkFaultCodeElement(SOAPFaultElement element) {
        // TODO
//        Assert.assertNull(element.getNamespaceURI());
        Assert.assertEquals("faultcode", element.getLocalName());
    }

    @Override
    protected void checkFaultStringElement(SOAPFaultElement element) {
        // TODO
//        Assert.assertNull(element.getNamespaceURI());
        Assert.assertEquals("faultstring", element.getLocalName());
    }

    // Custom fault codes are only allowed in SOAP 1.1
    @Validated @Test
    public void testSetFaultCodeAsStringCustom() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        fault.addNamespaceDeclaration("p", "urn:ns1");
        fault.setFaultCode("p:MyFaultCode");
        Node child = fault.getFirstChild();
        assertTrue(child instanceof SOAPFaultElement);
        checkFaultCodeElement((SOAPFaultElement)child);
    }
    
    @Validated @Test(expected=UnsupportedOperationException.class)
    public void testGetFaultRole() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        fault.getFaultRole();
    }
    
    @Validated @Test(expected=UnsupportedOperationException.class)
    public void testSetFaultRole() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        fault.setFaultRole("urn:myrole");
    }
    
    @Validated @Test(expected=UnsupportedOperationException.class)
    public void testGetFaultNode() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        fault.getFaultNode();
    }
    
    @Validated @Test(expected=UnsupportedOperationException.class)
    public void testSetFaultNode() throws Exception {
        SOAPFault fault = createEmptySOAPFault();
        fault.setFaultNode("mynode");
    }
}
