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
package com.google.code.ddom.frontend.axiom.soap;

import org.apache.axiom.soap.SOAPConstants;

import com.google.code.ddom.core.ext.ModelExtensionMapper;
import com.google.code.ddom.frontend.axiom.soap.support.SOAPVersionEx;

// TODO: this should only be used if a plain OMFactory is used; however, it is always used
public class AxiomSOAPModelExtensionMapper implements ModelExtensionMapper {
    private static final SOAPVersionEx[] SOAP_VERSIONS = { SOAPVersionEx.SOAP11, SOAPVersionEx.SOAP12 };
    
    private int depth;
    private SOAPVersionEx version;
    private boolean inBody;
    
    public Class<?> startElement(String namespaceURI, String localName) {
        depth++;
        if (depth == 1) {
            if (namespaceURI != null && localName.equals(SOAPConstants.SOAPENVELOPE_LOCAL_NAME)) {
                for (SOAPVersionEx candidate : SOAP_VERSIONS) {
                    if (candidate.getEnvelopeURI().equals(namespaceURI)) {
                        version = candidate;
                        return version.getSOAPEnvelopeClass();
                    }
                }
            }
        } else if (version != null) {
            if (depth == 2) {
                if (version.getEnvelopeURI().equals(namespaceURI)) {
                    if (localName.equals(SOAPConstants.HEADER_LOCAL_NAME)) {
                        return version.getSOAPHeaderClass();
                    } else if (localName.equals(SOAPConstants.BODY_LOCAL_NAME)) {
                        inBody = true;
                        return version.getSOAPBodyClass();
                    }
                }
            } else if (depth == 3) {
                if (inBody) {
                    if (localName.equals(SOAPConstants.BODY_FAULT_LOCAL_NAME)) {
                        return version.getSOAPFaultClass();
                    }
                } else {
                    
                }
            }
        }
        return null;
    }

    public void endElement() {
        depth--;
    }
}
