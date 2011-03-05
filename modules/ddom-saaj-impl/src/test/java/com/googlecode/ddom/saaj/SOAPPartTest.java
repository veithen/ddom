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
package com.googlecode.ddom.saaj;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Element;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;
import com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl;

@RunWith(ValidatedTestRunner.class)
public class SOAPPartTest {
    @ValidatedTestResource(reference=SOAPMessageFactory1_1Impl.class, actual=SOAP11MessageFactory.class)
    private MessageFactory factory;
    
    @Validated @Test
    public void testCreateEnvelopeWithCreateElementNS() throws Exception {
        SOAPPart soapPart = factory.createMessage().getSOAPPart();
        Element element = soapPart.createElementNS(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "SOAP-ENV:Envelope");
        assertTrue(element instanceof SOAPEnvelope);
        soapPart.appendChild(element);
        SOAPEnvelope envelope = soapPart.getEnvelope();
        assertNotNull(envelope);
    }
    
    @Validated @Test
    public void testGetParentElement() throws Exception {
        SOAPPart soapPart = factory.createMessage().getSOAPPart();
        assertNull(soapPart.getParentElement());
    }
    
    @Validated @Test(expected=SOAPException.class)
    public void testSetParentElement() throws Exception {
        SOAPPart soapPart = factory.createMessage().getSOAPPart();
        SOAPElement element = (SOAPElement)soapPart.createElementNS("urn:ns", "p:root");
        soapPart.setParentElement(element);
    }
    
    @Validated @Test(expected=SOAPException.class)
    public void testSetParentElementNull() throws Exception {
        SOAPPart soapPart = factory.createMessage().getSOAPPart();
        soapPart.setParentElement(null);
    }
    
    @Validated @Test
    public void testGetValue() throws Exception {
        SOAPPart soapPart = factory.createMessage().getSOAPPart();
        assertNull(soapPart.getValue());
    }
    
    @Validated @Test(expected=IllegalStateException.class)
    public void testSetValue() throws Exception {
        SOAPPart soapPart = factory.createMessage().getSOAPPart();
        soapPart.setValue("test");
    }
    
    @Validated @Test(expected=IllegalStateException.class)
    public void testSetValueNull() throws Exception {
        SOAPPart soapPart = factory.createMessage().getSOAPPart();
        soapPart.setValue(null);
    }
    
    /**
     * Tests the behavior of {@link SOAPPart#getContent()} for a plain SOAP message created from an
     * input stream.
     * 
     * @throws Exception
     * 
     * @see SOAPMessageTest#testWriteTo()
     */
    @Validated @Test
    public void testGetContent() throws Exception {
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml; charset=utf-8");
        InputStream in = SOAPPartTest.class.getResourceAsStream("soap11/message.xml");
        byte[] orgContent = IOUtils.toByteArray(in);
        SOAPMessage message = factory.createMessage(headers, new ByteArrayInputStream(orgContent));
        
        // Get the content before accessing the SOAP part
        Source source1 = message.getSOAPPart().getContent();
        assertTrue(source1 instanceof StreamSource);
        byte[] content1 = IOUtils.toByteArray(((StreamSource)source1).getInputStream());
        assertTrue(Arrays.equals(orgContent, content1));
        
        // Now access the SOAP part and get the content again
        message.getSOAPPart().getEnvelope();
        // Note that the fact that we can still access the SOAP part (although we have consumed
        // the input stream returned by getContent) means that the SAAJ implementation has
        // copied the content of the stream.
        Source source2 = message.getSOAPPart().getContent();
        // The source is now a DOMSource. 
        assertTrue(source2 instanceof DOMSource);
    }
}
