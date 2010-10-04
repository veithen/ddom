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
package com.google.code.ddom.frontend.saaj.support;

import javax.xml.soap.SOAPConstants;

import com.google.code.ddom.core.Sequence;
import com.google.code.ddom.core.SequenceBuilder;

public class SOAPVersion {
    public static final SOAPVersion SOAP11 = new SOAPVersion(
            new SequenceBuilder()
                .addItem(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "Header")
                .addItem(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "Body").build());
    
    public static final SOAPVersion SOAP12 = new SOAPVersion(
            new SequenceBuilder()
                .addItem(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Header")
                .addItem(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Body").build());
    
    private final Sequence envelopeSequence;
    
    private SOAPVersion(Sequence envelopeSequence) {
        this.envelopeSequence = envelopeSequence;
    }

    public Sequence getEnvelopeSequence() {
        return envelopeSequence;
    }
}
