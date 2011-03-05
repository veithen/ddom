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

import javax.xml.soap.SOAPEnvelope;

import com.googlecode.ddom.frontend.saaj.intf.SAAJDocument;

public interface SOAPVersion {
    SOAPVersion SOAP11 = new SOAPVersion() {
        public SOAPEnvelope createEnvelope(SAAJDocument document) {
            return document.createSOAP11Envelope();
        }

        public String getContentType() {
            return "text/xml";
        }
    };

    SOAPVersion SOAP12 = new SOAPVersion() {
        public SOAPEnvelope createEnvelope(SAAJDocument document) {
            return document.createSOAP12Envelope();
        }

        public String getContentType() {
            return "application/soap+xml";
        }
    };
    
    SOAPEnvelope createEnvelope(SAAJDocument document);
    String getContentType();
}
