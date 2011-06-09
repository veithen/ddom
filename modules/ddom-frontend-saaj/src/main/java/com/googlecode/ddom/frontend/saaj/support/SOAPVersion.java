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
import javax.xml.soap.SOAPEnvelope;

import com.googlecode.ddom.core.Sequence;
import com.googlecode.ddom.core.SequenceBuilder;
import com.googlecode.ddom.frontend.saaj.intf.SAAJDetail;
import com.googlecode.ddom.frontend.saaj.intf.SAAJDocument;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11Body;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11Fault;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11Header;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP12Body;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP12Fault;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP12Header;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPBody;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPElement;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPFault;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPFaultElement;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPHeader;

public abstract class SOAPVersion {
    public static final SOAPVersion SOAP11 = new SOAPVersion(
            new SequenceBuilder()
                .addItem(SAAJSOAPFaultElement.class, "", "faultcode")
                .addItem(SAAJSOAPFaultElement.class, "", "faultstring")
                .addItem(SAAJSOAPFaultElement.class, "", "faultactor")
                .addItem(SAAJDetail.class, "", "detail").build(),
            null) {
        
        @Override
        public String getEnvelopeNamespaceURI() {
            return SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE;
        }
        
        @Override
        public Class<? extends SAAJSOAPHeader> getSOAPHeaderClass() {
            return SAAJSOAP11Header.class;
        }

        @Override
        public Class<? extends SAAJSOAPBody> getSOAPBodyClass() {
            return SAAJSOAP11Body.class;
        }

        @Override
        public Class<? extends SAAJSOAPFault> getSOAPFaultClass() {
            return SAAJSOAP11Fault.class;
        }

        @Override
        public String getActorAttributeLocalName() {
            return "actor";
        }

        @Override
        public String formatMustUnderstand(boolean mustUnderstand) {
            return mustUnderstand ? "1" : "0";
        }

        @Override
        public boolean parseMustUnderstand(String mustUnderstand) {
            return mustUnderstand.equals("1") || mustUnderstand.equalsIgnoreCase("true");
        }
        
        @Override
        public int getFaultCodeIndex() {
            return 0;
        }
        
        @Override
        public int getFaultReasonIndex(){
            return 1;
        }
        
        @Override
        public int getFaultNodeIndex(){
            return -1;
        }
        
        @Override
        public int getFaultRoleIndex(){
            return 2;
        }
        
        @Override
        public int getFaultDetailIndex(){
            return 3;
        }
        
        @Override
        public SOAPEnvelope createEnvelope(SAAJDocument document) {
            return document.createSOAP11Envelope();
        }

        @Override
        public String getContentType() {
            return "text/xml";
        }
    };
    
    public static final SOAPVersion SOAP12 = new SOAPVersion(
            new SequenceBuilder()
                .addItem(SAAJSOAPFaultElement.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Code")
                .addItem(SAAJSOAPFaultElement.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Reason")
                .addItem(SAAJSOAPFaultElement.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Node")
                .addItem(SAAJSOAPFaultElement.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Role")
                .addItem(SAAJDetail.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Detail").build(),
            new SequenceBuilder()
                .addItem(SAAJSOAPElement.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Value")
                .addItem(SAAJSOAPElement.class, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Subcode")
                .enableMatchByInterface().build()) {
        
        @Override
        public String getEnvelopeNamespaceURI() {
            return SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE;
        }
        
        @Override
        public Class<? extends SAAJSOAPHeader> getSOAPHeaderClass() {
            return SAAJSOAP12Header.class;
        }

        @Override
        public Class<? extends SAAJSOAPBody> getSOAPBodyClass() {
            return SAAJSOAP12Body.class;
        }

        @Override
        public Class<? extends SAAJSOAPFault> getSOAPFaultClass() {
            return SAAJSOAP12Fault.class;
        }

        @Override
        public String getActorAttributeLocalName() {
            return "role";
        }

        @Override
        public String formatMustUnderstand(boolean mustUnderstand) {
            return mustUnderstand ? "true" : "false";
        }

        @Override
        public boolean parseMustUnderstand(String mustUnderstand) {
            return mustUnderstand.equals("1") || mustUnderstand.equals("true");
        }
        
        @Override
        public int getFaultCodeIndex() {
            return 0;
        }
        
        @Override
        public int getFaultReasonIndex(){
            return 1;
        }
        
        @Override
        public int getFaultNodeIndex(){
            return 2;
        }
        
        @Override
        public int getFaultRoleIndex(){
            return 3;
        }
        
        @Override
        public int getFaultDetailIndex(){
            return 4;
        }
        
        @Override
        public SOAPEnvelope createEnvelope(SAAJDocument document) {
            return document.createSOAP12Envelope();
        }

        @Override
        public String getContentType() {
            return "application/soap+xml";
        }
    };
    
    private final Sequence envelopeSequence;
    private final Sequence faultSequence;
    private final Sequence faultCodeSequence;
    
    public SOAPVersion(Sequence faultSequence, Sequence faultCodeSequence) {
        envelopeSequence = new SequenceBuilder()
                .addItem(getSOAPHeaderClass(), getEnvelopeNamespaceURI(), "Header")
                .addItem(getSOAPBodyClass(), getEnvelopeNamespaceURI(), "Body")
                .enableMatchByInterface().build();
        this.faultSequence = faultSequence;
        this.faultCodeSequence = faultCodeSequence;
    }
    
    public abstract String getEnvelopeNamespaceURI();
    
    public final Sequence getEnvelopeSequence() {
        return envelopeSequence;
    }
    
    public abstract Class<? extends SAAJSOAPHeader> getSOAPHeaderClass();
    public abstract Class<? extends SAAJSOAPBody> getSOAPBodyClass();
    public abstract Class<? extends SAAJSOAPFault> getSOAPFaultClass();
    
    public final Sequence getFaultSequence() {
        return faultSequence;
    }

    public abstract String getActorAttributeLocalName();
    
    public abstract String formatMustUnderstand(boolean mustUnderstand);
    public abstract boolean parseMustUnderstand(String mustUnderstand);
    
    public abstract int getFaultCodeIndex();
    public abstract int getFaultReasonIndex();
    public abstract int getFaultNodeIndex();
    public abstract int getFaultRoleIndex();
    public abstract int getFaultDetailIndex();
    
    public final Sequence getFaultCodeSequence() {
        return faultCodeSequence;
    }

    public abstract SOAPEnvelope createEnvelope(SAAJDocument document);
    
    public abstract String getContentType();
}
