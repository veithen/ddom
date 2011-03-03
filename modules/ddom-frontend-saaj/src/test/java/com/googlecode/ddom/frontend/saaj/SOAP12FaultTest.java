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
package com.googlecode.ddom.frontend.saaj;

import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPFaultElement;

import org.junit.Assert;

public class SOAP12FaultTest extends SOAPFaultTest {
    public SOAP12FaultTest() {
        super(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
    }

    @Override
    protected void checkFaultCodeElement(SOAPFaultElement element) {
        Assert.assertEquals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, element.getNamespaceURI());
        Assert.assertEquals("Code", element.getLocalName());
    }

    @Override
    protected void checkFaultStringElement(SOAPFaultElement element) {
        Assert.assertEquals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, element.getNamespaceURI());
        Assert.assertEquals("Reason", element.getLocalName());
    }
}