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
package com.googlecode.ddom.frontend.saaj.support;

import javax.xml.soap.SOAPConstants;

import com.googlecode.ddom.core.Sequence;
import com.googlecode.ddom.core.SequenceBuilder;
import com.googlecode.ddom.frontend.saaj.intf.SAAJDetail;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11Body;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11Fault;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11Header;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP12Body;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP12Fault;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP12Header;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPFault;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPFaultElement;

public abstract class SOAPVersion {
    public static final SOAPVersion SOAP11 = new SOAPVersion(
            SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE,
            new SequenceBuilder()
                .addItem(SAAJSOAP11Header.class, SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "Header")
                .addItem(SAAJSOAP11Body.class, SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "Body")
                .enableMatchByInterface().build(),
            new SequenceBuilder()
                .addItem(SAAJSOAPFaultElement.class, "", "faultcode")
                .addItem(SAAJSOAPFaultElement.class, "", "faultstring")
                .addItem(SAAJSOAPFaultElement.class, "", "faultactor")
                .addItem(SAAJDetail.class, "", "detail").build(),
            "actor") {
        
        @Override
        public Class<? extends SAAJSOAPFault> getSOAPFaultClass() {
            return SAAJSOAP11Fault.class;
        }

        @Override
        public String formatMustUnderstand(boolean mustUnderstand) {
            return mustUnderstand ? "1" : "0";
        }

        @Override
        public boolean parseMustUnderstand(String mustUnderstand) {
            return mustUnderstand.equals("1") || mustUnderstand.equalsIgnoreCase("true");
        }
    };
    
    public static final SOAPVersion SOAP12 = new SOAPVersion(
            SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE,
            new SequenceBuilder()
                .addItem(SAAJSOAP12Header.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Header")
                .addItem(SAAJSOAP12Body.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Body")
                .enableMatchByInterface().build(),
            new SequenceBuilder()
                .addItem(SAAJSOAPFaultElement.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Code")
                .addItem(SAAJSOAPFaultElement.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Reason")
                .addItem(SAAJSOAPFaultElement.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Role")
                .addItem(SAAJDetail.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Detail").build(),
            "role") {
        
        @Override
        public Class<? extends SAAJSOAPFault> getSOAPFaultClass() {
            return SAAJSOAP12Fault.class;
        }

        @Override
        public String formatMustUnderstand(boolean mustUnderstand) {
            return mustUnderstand ? "true" : "false";
        }

        @Override
        public boolean parseMustUnderstand(String mustUnderstand) {
            return mustUnderstand.equals("1") || mustUnderstand.equals("true");
        }
    };
    
    private final String envelopeNamespaceURI;
    private final Sequence envelopeSequence;
    private final Sequence faultSequence;
    private final String actorAttributeLocalName;
    
    public SOAPVersion(String envelopeNamespaceURI, Sequence envelopeSequence, Sequence faultSequence,
            String actorAttributeLocalName) {
        this.envelopeNamespaceURI = envelopeNamespaceURI;
        this.envelopeSequence = envelopeSequence;
        this.faultSequence = faultSequence;
        this.actorAttributeLocalName = actorAttributeLocalName;
    }
    
    public String getEnvelopeNamespaceURI() {
        return envelopeNamespaceURI;
    }
    
    public Sequence getEnvelopeSequence() {
        return envelopeSequence;
    }
    
    public abstract Class<? extends SAAJSOAPFault> getSOAPFaultClass();
    
    public Sequence getFaultSequence() {
        return faultSequence;
    }

    public String getActorAttributeLocalName() {
        return actorAttributeLocalName;
    }
    
    public abstract String formatMustUnderstand(boolean mustUnderstand);
    public abstract boolean parseMustUnderstand(String mustUnderstand);
}
