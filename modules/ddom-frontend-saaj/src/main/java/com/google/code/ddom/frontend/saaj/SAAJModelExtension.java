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

import javax.xml.soap.SOAPConstants;

import com.google.code.ddom.core.ext.SimpleModelExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAP11BodyExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAP11EnvelopeExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAP11FaultExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAP11HeaderExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAP12BodyExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAP12EnvelopeExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAP12FaultExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAP12HeaderExtension;

public class SAAJModelExtension extends SimpleModelExtension {
    public Class<?> mapElement(String namespaceURI, String localName) {
        if (localName.equals("Envelope")) {
            if (SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE.equals(namespaceURI)) {
                return SOAP11EnvelopeExtension.class;
            } else if (SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE.equals(namespaceURI)) {
                return SOAP12EnvelopeExtension.class;
            } else {
                return null;
            }
        } else if (localName.equals("Body")) {
            if (SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE.equals(namespaceURI)) {
                return SOAP11BodyExtension.class;
            } else if (SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE.equals(namespaceURI)) {
                return SOAP12BodyExtension.class;
            } else {
                return null;
            }
        } else if (localName.equals("Header")) {
            if (SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE.equals(namespaceURI)) {
                return SOAP11HeaderExtension.class;
            } else if (SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE.equals(namespaceURI)) {
                return SOAP12HeaderExtension.class;
            } else {
                return null;
            }
        } else if (localName.equals("Fault")) {
            if (SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE.equals(namespaceURI)) {
                return SOAP11FaultExtension.class;
            } else if (SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE.equals(namespaceURI)) {
                return SOAP12FaultExtension.class;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
