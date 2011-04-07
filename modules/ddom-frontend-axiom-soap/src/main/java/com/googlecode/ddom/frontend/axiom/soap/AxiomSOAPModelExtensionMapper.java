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
package com.googlecode.ddom.frontend.axiom.soap;

import javax.xml.namespace.QName;

import org.apache.axiom.soap.SOAPConstants;

import com.googlecode.ddom.core.ext.ModelExtensionMapper;
import com.googlecode.ddom.frontend.axiom.soap.support.SOAPVersionEx;

// TODO: this should only be used if a plain OMFactory is used; however, it is always used
public class AxiomSOAPModelExtensionMapper implements ModelExtensionMapper {
    private static final SOAPVersionEx[] SOAP_VERSIONS = { SOAPVersionEx.SOAP11, SOAPVersionEx.SOAP12 };
    
    private static final int STATE_NONE = 0;
    private static final int STATE_ENVELOPE = 1;
    private static final int STATE_HEADER = 2;
    private static final int STATE_BODY = 3;
    private static final int STATE_FAULT = 4;
    private static final int STATE_FAULT_CODE = 5;
    private static final int STATE_FAULT_REASON = 6;
    
    private int depth;
    private SOAPVersionEx version;
    private int state = STATE_NONE;
    
    public Class<?> startElement(String namespaceURI, String localName) {
        depth++;
        switch (state) {
            case STATE_NONE:
                if (depth == 1 && namespaceURI != null && localName.equals(SOAPConstants.SOAPENVELOPE_LOCAL_NAME)) {
                    for (SOAPVersionEx candidate : SOAP_VERSIONS) {
                        if (candidate.getEnvelopeURI().equals(namespaceURI)) {
                            state = STATE_ENVELOPE;
                            version = candidate;
                            return version.getSOAPEnvelopeClass();
                        }
                    }
                }
                break;
            case STATE_ENVELOPE:
                if (depth == 2 && version.getEnvelopeURI().equals(namespaceURI)) {
                    if (localName.equals(SOAPConstants.HEADER_LOCAL_NAME)) {
                        state = STATE_HEADER;
                        return version.getSOAPHeaderClass();
                    } else if (localName.equals(SOAPConstants.BODY_LOCAL_NAME)) {
                        state = STATE_BODY;
                        return version.getSOAPBodyClass();
                    }
                }
                break;
            case STATE_HEADER:
                if (depth == 3) {
                    return version.getSOAPHeaderBlockClass();
                }
                break;
            case STATE_BODY:
                if (depth == 3 && version.getEnvelopeURI().equals(namespaceURI)
                        && localName.equals(SOAPConstants.BODY_FAULT_LOCAL_NAME)) {
                    state = STATE_FAULT;
                    return version.getSOAPFaultClass();
                }
                break;
            case STATE_FAULT:
                if (depth == 4) {
                    if (testQName(version.getFaultCodeQName(), namespaceURI, localName)) {
                        state = STATE_FAULT_CODE;
                        return version.getSOAPFaultCodeClass();
                    } else if (testQName(version.getFaultReasonQName(), namespaceURI, localName)) {
                        state = STATE_FAULT_REASON;
                        return version.getSOAPFaultReasonClass();
                    } else if (testQName(version.getFaultNodeQName(), namespaceURI, localName)) {
                        return version.getSOAPFaultNodeClass();
                    } else if (testQName(version.getFaultRoleQName(), namespaceURI, localName)) {
                        return version.getSOAPFaultRoleClass();
                    } else if (testQName(version.getFaultDetailQName(), namespaceURI, localName)) {
                        return version.getSOAPFaultDetailClass();
                    }
                }
                break;
            case STATE_FAULT_REASON:
                if (depth == 5 && testQName(version.getFaultTextQName(), namespaceURI, localName)) {
                    return version.getSOAPFaultTextClass();
                }
                break;
            case STATE_FAULT_CODE:
                if (depth >= 5) {
                    if (testQName(version.getFaultValueQName(), namespaceURI, localName)) {
                        return version.getSOAPFaultValueClass();
                    } else if (testQName(version.getFaultSubCodeQName(), namespaceURI, localName)) {
                        return version.getSOAPFaultSubCodeClass();
                    }
                }
                
        }
        return null;
    }

    private boolean testQName(QName qname, String namespaceURI, String localName) {
        if (qname == null) {
            return false;
        } else {
            return qname.getLocalPart().equals(localName) && qname.getNamespaceURI().equals(namespaceURI);
        }
    }
    
    public void endElement() {
        switch (state) {
            case STATE_ENVELOPE:
                if (depth == 1) {
                    state = STATE_NONE;
                }
                break;
            case STATE_HEADER:
            case STATE_BODY:
                if (depth == 2) {
                    state = STATE_ENVELOPE;
                }
                break;
            case STATE_FAULT:
                if (depth == 3) {
                    state = STATE_BODY;
                }
                break;
            case STATE_FAULT_CODE:
            case STATE_FAULT_REASON:
                if (depth == 4) {
                    state = STATE_FAULT;
                }
        }
        depth--;
    }
}
