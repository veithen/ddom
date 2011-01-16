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
package com.google.code.ddom.frontend.axiom.soap.support;

import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP11Version;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAP12Version;
import org.apache.axiom.soap.SOAPConstants;
import org.apache.axiom.soap.SOAPVersion;

import com.google.code.ddom.core.Sequence;
import com.google.code.ddom.core.SequenceBuilder;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11Body;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11Envelope;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11Fault;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11FaultDetail;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11Header;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12Body;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12Envelope;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12Fault;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultDetail;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12Header;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPBody;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPEnvelope;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFault;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultDetail;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPHeader;

public abstract class SOAPVersionEx {
    public static final SOAPVersionEx SOAP11 = new SOAPVersionEx(
            SOAP11Version.getSingleton(),
            new SequenceBuilder()
                .addItem(AxiomSOAP11Header.class, SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI, SOAPConstants.HEADER_LOCAL_NAME)
                .addItem(AxiomSOAP11Body.class, SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI, SOAPConstants.BODY_LOCAL_NAME)
                .enableMatchByInterface().build()) {
        
        @Override
        public Class<? extends AxiomSOAPEnvelope> getSOAPEnvelopeClass() {
            return AxiomSOAP11Envelope.class;
        }

        @Override
        public Class<? extends AxiomSOAPHeader> getSOAPHeaderClass() {
            return AxiomSOAP11Header.class;
        }

        @Override
        public Class<? extends AxiomSOAPBody> getSOAPBodyClass() {
            return AxiomSOAP11Body.class;
        }

        @Override
        public Class<? extends AxiomSOAPFault> getSOAPFaultClass() {
            return AxiomSOAP11Fault.class;
        }

        @Override
        public Class<? extends AxiomSOAPFaultDetail> getSOAPFaultDetailClass() {
            return AxiomSOAP11FaultDetail.class;
        }
    };

    public static final SOAPVersionEx SOAP12 = new SOAPVersionEx(
            SOAP12Version.getSingleton(),
            new SequenceBuilder()
                .addItem(AxiomSOAP12Header.class, SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI, SOAPConstants.HEADER_LOCAL_NAME)
                .addItem(AxiomSOAP12Body.class, SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI, SOAPConstants.BODY_LOCAL_NAME)
                .enableMatchByInterface().build()) {
        
        @Override
        public Class<? extends AxiomSOAPEnvelope> getSOAPEnvelopeClass() {
            return AxiomSOAP12Envelope.class;
        }

        @Override
        public Class<? extends AxiomSOAPHeader> getSOAPHeaderClass() {
            return AxiomSOAP12Header.class;
        }

        @Override
        public Class<? extends AxiomSOAPBody> getSOAPBodyClass() {
            return AxiomSOAP12Body.class;
        }

        @Override
        public Class<? extends AxiomSOAPFault> getSOAPFaultClass() {
            return AxiomSOAP12Fault.class;
        }

        @Override
        public Class<? extends AxiomSOAPFaultDetail> getSOAPFaultDetailClass() {
            return AxiomSOAP12FaultDetail.class;
        }
    };
    
    private final SOAPVersion soapVersion;
    private final Sequence envelopeSequence;
    
    public SOAPVersionEx(SOAPVersion soapVersion, Sequence envelopeSequence) {
        this.soapVersion = soapVersion;
        this.envelopeSequence = envelopeSequence;
    }

    public SOAPVersion getSOAPVersion() {
        return soapVersion;
    }

    public final String getEnvelopeURI() {
        return soapVersion.getEnvelopeURI();
    }

    public Sequence getEnvelopeSequence() {
        return envelopeSequence;
    }

    public abstract Class<? extends AxiomSOAPEnvelope> getSOAPEnvelopeClass();
    public abstract Class<? extends AxiomSOAPHeader> getSOAPHeaderClass();
    public abstract Class<? extends AxiomSOAPBody> getSOAPBodyClass();
    public abstract Class<? extends AxiomSOAPFault> getSOAPFaultClass();
    public abstract Class<? extends AxiomSOAPFaultDetail> getSOAPFaultDetailClass();
}
