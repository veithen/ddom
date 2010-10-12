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

import static org.junit.Assert.*;

import java.util.Iterator;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
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
