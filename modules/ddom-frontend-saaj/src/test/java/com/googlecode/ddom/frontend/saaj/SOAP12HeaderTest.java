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
import static org.junit.Assert.assertTrue;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;

import org.junit.Test;

import com.google.code.ddom.utils.test.Validated;

public class SOAP12HeaderTest extends SOAPHeaderTest {
    public SOAP12HeaderTest() {
        super(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
    }
    
    @Validated @Test(expected=SOAPException.class)
    public void testAddNotUnderstoodHeaderElementWithoutNamespace() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        header.addNotUnderstoodHeaderElement(new QName("header"));
    }
    
    @Validated @Test(expected=SOAPException.class)
    public void testAddNotUnderstoodHeaderElementWithNullArgument() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        header.addNotUnderstoodHeaderElement(null);
    }
    
    @Validated @Test
    public void testAddNotUnderstoodHeaderElement() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        header.addNotUnderstoodHeaderElement(new QName("urn:ns", "header", "p"));
        SOAPElement element = (SOAPElement)header.getChildElements(new QName(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "NotUnderstood")).next();
        assertEquals("p:header", element.getAttributeNS(null, "qname"));
        assertEquals("urn:ns", element.getAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "p"));
    }

    @Validated @Test
    public void testAddNotUnderstoodHeaderElementWithoutPrefix() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        header.addNotUnderstoodHeaderElement(new QName("urn:ns", "header"));
        SOAPElement element = (SOAPElement)header.getChildElements(new QName(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "NotUnderstood")).next();
        String qname = element.getAttributeNS(null, "qname");
        int idx = qname.indexOf(':');
        assertTrue(idx != -1);
        String prefix = qname.substring(0, idx);
        String localName = qname.substring(idx+1);
        assertEquals("header", localName);
        assertEquals("urn:ns", element.getAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, prefix));
    }
}
