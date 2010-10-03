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
package com.googlecode.ddom.saaj;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPPart;

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
}
