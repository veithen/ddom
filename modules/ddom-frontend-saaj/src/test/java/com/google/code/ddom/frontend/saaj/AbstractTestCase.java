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

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.Text;

import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public abstract class AbstractTestCase {
    @ValidatedTestResource(reference=RISAAJUtil.class, actual=DDOMSAAJUtil.class)
    private SAAJUtil saajUtil;
    
    protected final String soapVersion;

    public AbstractTestCase(String soapVersion) {
        this.soapVersion = soapVersion;
    }
    
    protected final SOAPEnvelope createSOAPEnvelope() {
        if (soapVersion.equals(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE)) {
            return saajUtil.createSOAP11Envelope();
        } else {
            return saajUtil.createSOAP12Envelope();
        }
    }
    
    protected final SOAPHeader createEmptySOAPHeader() {
        return (SOAPHeader)createSOAPEnvelope().getOwnerDocument().createElementNS(soapVersion, "SOAP-ENV:Header");
    }
    
    protected final SOAPBody createEmptySOAPBody() {
        return (SOAPBody)createSOAPEnvelope().getOwnerDocument().createElementNS(soapVersion, "SOAP-ENV:Body");
    }
    
    protected final SOAPFault createEmptySOAPFault() {
        return (SOAPFault)createSOAPEnvelope().getOwnerDocument().createElementNS(soapVersion, "SOAP-ENV:Fault");
    }
    
    protected final Text createText(String content) {
        return (Text)createSOAPEnvelope().getOwnerDocument().createTextNode(content);
    }
    
    protected final Text createComment(String content) {
        return (Text)createSOAPEnvelope().getOwnerDocument().createComment(content);
    }
    
    protected final Text createCDATASection(String content) {
        return (Text)createSOAPEnvelope().getOwnerDocument().createCDATASection(content);
    }
}
