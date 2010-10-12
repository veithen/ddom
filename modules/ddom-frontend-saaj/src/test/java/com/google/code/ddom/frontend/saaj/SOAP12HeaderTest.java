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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import org.junit.Test;

import com.google.code.ddom.utils.test.Validated;

public class SOAP12HeaderTest extends SOAPHeaderTest {
    public SOAP12HeaderTest() {
        super(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "role");
    }

    @Validated @Test
    public final void testGetSetMustUnderstandTrue() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        SOAPHeaderElement element = (SOAPHeaderElement)header.addChildElement("test", "p", "urn:ns");
        element.setMustUnderstand(true);
        assertEquals("true", element.getAttributeNS(header.getNamespaceURI(), "mustUnderstand"));
        assertTrue(element.getMustUnderstand());
    }
    
    @Validated @Test
    public final void testGetSetMustUnderstandFalse() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        SOAPHeaderElement element = (SOAPHeaderElement)header.addChildElement("test", "p", "urn:ns");
        element.setMustUnderstand(false);
        assertEquals("false", element.getAttributeNS(header.getNamespaceURI(), "mustUnderstand"));
        assertFalse(element.getMustUnderstand());
    }
    
    @Validated @Test
    public final void testGetMustUnderstandFromExistingAttributeTrueUpperCase() throws Exception {
        testGetMustUnderstandFromExistingAttribute("TRUE", false);
    }
}
