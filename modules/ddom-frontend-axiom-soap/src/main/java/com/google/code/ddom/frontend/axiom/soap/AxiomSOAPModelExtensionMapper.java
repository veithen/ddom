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
package com.google.code.ddom.frontend.axiom.soap;

import com.google.code.ddom.core.ext.ModelExtensionMapper;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP11EnvelopeExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP12EnvelopeExtension;

// TODO: this should only be used if a plain OMFactory is used; however, it is always used
public class AxiomSOAPModelExtensionMapper implements ModelExtensionMapper {
    private static final String SOAP11_NAMESPACE_URI = "http://schemas.xmlsoap.org/soap/envelope/";
    private static final String SOAP12_NAMESPACE_URI = "http://www.w3.org/2003/05/soap-envelope";
    
    private int depth;
    private String soapNamespaceURI;
    private boolean isSoap12;
    
    public Class<?> startElement(String namespaceURI, String localName) {
        switch (depth++) {
            case 0:
                if (namespaceURI == null) {
                    // TODO
                    return null;
                } else if (namespaceURI.equals(SOAP11_NAMESPACE_URI)) {
                    soapNamespaceURI = namespaceURI;
                    isSoap12 = false;
                } else if (namespaceURI.equals(SOAP12_NAMESPACE_URI)) {
                    soapNamespaceURI = namespaceURI;
                    isSoap12 = true;
                } else {
                    throw new RuntimeException(); // TODO
                }
                if (!localName.equals("Envelope")) {
                    throw new RuntimeException(); // TODO
                }
                return isSoap12 ? SOAP12EnvelopeExtension.class : SOAP11EnvelopeExtension.class;
            default:
                return null;
        }
    }

    public void endElement() {
        depth--;
    }
}
