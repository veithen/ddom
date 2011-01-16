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
package com.google.code.ddom.frontend.saaj;

import javax.xml.soap.SOAPConstants;

import com.google.code.ddom.core.ext.SimpleModelExtension;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAP11Body;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAP11Envelope;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAP11Fault;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAP11Header;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAP12Body;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAP12Envelope;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAP12Fault;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAP12Header;

public class SAAJModelExtension extends SimpleModelExtension {
    public Class<?> mapElement(String namespaceURI, String localName) {
        if (localName.equals("Envelope")) {
            if (SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE.equals(namespaceURI)) {
                return SAAJSOAP11Envelope.class;
            } else if (SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE.equals(namespaceURI)) {
                return SAAJSOAP12Envelope.class;
            } else {
                return null;
            }
        } else if (localName.equals("Body")) {
            if (SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE.equals(namespaceURI)) {
                return SAAJSOAP11Body.class;
            } else if (SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE.equals(namespaceURI)) {
                return SAAJSOAP12Body.class;
            } else {
                return null;
            }
        } else if (localName.equals("Header")) {
            if (SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE.equals(namespaceURI)) {
                return SAAJSOAP11Header.class;
            } else if (SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE.equals(namespaceURI)) {
                return SAAJSOAP12Header.class;
            } else {
                return null;
            }
        } else if (localName.equals("Fault")) {
            if (SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE.equals(namespaceURI)) {
                return SAAJSOAP11Fault.class;
            } else if (SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE.equals(namespaceURI)) {
                return SAAJSOAP12Fault.class;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
