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
import com.google.code.ddom.frontend.saaj.ext.DetailExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAP11BodyExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAP11HeaderExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAP12BodyExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAP12HeaderExtension;
import com.google.code.ddom.frontend.saaj.ext.SOAPFaultElementExtension;

public final class SOAPVersion {
    public static final SOAPVersion SOAP11 = new SOAPVersion(
            new SequenceBuilder()
                .addItem(SOAP11HeaderExtension.class, SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "Header")
                .addItem(SOAP11BodyExtension.class, SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "Body")
                .enableMatchByInterface().build(),
            new SequenceBuilder()
                .addItem(SOAPFaultElementExtension.class, null, "faultcode")
                .addItem(SOAPFaultElementExtension.class, null, "faultstring")
                .addItem(SOAPFaultElementExtension.class, null, "faultactor")
                .addItem(DetailExtension.class, null, "detail").build());
    
    public static final SOAPVersion SOAP12 = new SOAPVersion(
            new SequenceBuilder()
                .addItem(SOAP12HeaderExtension.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Header")
                .addItem(SOAP12BodyExtension.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Body")
                .enableMatchByInterface().build(),
            new SequenceBuilder()
                .addItem(SOAPFaultElementExtension.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Code")
                .addItem(SOAPFaultElementExtension.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Reason")
                .addItem(SOAPFaultElementExtension.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Role")
                .addItem(DetailExtension.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Detail").build());
    
    private final Sequence envelopeSequence;
    private final Sequence faultSequence;
    
    private SOAPVersion(Sequence envelopeSequence, Sequence faultSequence) {
        this.envelopeSequence = envelopeSequence;
        this.faultSequence = faultSequence;
    }

    public Sequence getEnvelopeSequence() {
        return envelopeSequence;
    }

    public Sequence getFaultSequence() {
        return faultSequence;
    }
}
