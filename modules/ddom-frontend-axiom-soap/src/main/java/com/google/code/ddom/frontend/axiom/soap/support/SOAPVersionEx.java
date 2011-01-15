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
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP11BodyExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP11EnvelopeExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP11FaultDetailExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP11FaultExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP11HeaderExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP12BodyExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP12EnvelopeExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP12FaultDetailExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP12FaultExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP12HeaderExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAPBodyExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAPEnvelopeExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAPFaultDetailExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAPFaultExtension;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAPHeaderExtension;

public abstract class SOAPVersionEx {
    public static final SOAPVersionEx SOAP11 = new SOAPVersionEx(
            SOAP11Version.getSingleton(),
            new SequenceBuilder()
                .addItem(SOAP11HeaderExtension.class, SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI, SOAPConstants.HEADER_LOCAL_NAME)
                .addItem(SOAP11BodyExtension.class, SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI, SOAPConstants.BODY_LOCAL_NAME)
                .enableMatchByInterface().build()) {
        
        public Class<? extends SOAPEnvelopeExtension> getSOAPEnvelopeExtension() {
            return SOAP11EnvelopeExtension.class;
        }

        @Override
        public Class<? extends SOAPHeaderExtension> getSOAPHeaderExtension() {
            return SOAP11HeaderExtension.class;
        }

        @Override
        public Class<? extends SOAPBodyExtension> getSOAPBodyExtension() {
            return SOAP11BodyExtension.class;
        }

        @Override
        public Class<? extends SOAPFaultExtension> getSOAPFaultExtension() {
            return SOAP11FaultExtension.class;
        }

        @Override
        public Class<? extends SOAPFaultDetailExtension> getSOAPFaultDetailExtension() {
            return SOAP11FaultDetailExtension.class;
        }
    };

    public static final SOAPVersionEx SOAP12 = new SOAPVersionEx(
            SOAP12Version.getSingleton(),
            new SequenceBuilder()
                .addItem(SOAP12HeaderExtension.class, SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI, SOAPConstants.HEADER_LOCAL_NAME)
                .addItem(SOAP12BodyExtension.class, SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI, SOAPConstants.BODY_LOCAL_NAME)
                .enableMatchByInterface().build()) {
        
        public Class<? extends SOAPEnvelopeExtension> getSOAPEnvelopeExtension() {
            return SOAP12EnvelopeExtension.class;
        }

        @Override
        public Class<? extends SOAPHeaderExtension> getSOAPHeaderExtension() {
            return SOAP12HeaderExtension.class;
        }

        @Override
        public Class<? extends SOAPBodyExtension> getSOAPBodyExtension() {
            return SOAP12BodyExtension.class;
        }

        @Override
        public Class<? extends SOAPFaultExtension> getSOAPFaultExtension() {
            return SOAP12FaultExtension.class;
        }

        @Override
        public Class<? extends SOAPFaultDetailExtension> getSOAPFaultDetailExtension() {
            return SOAP12FaultDetailExtension.class;
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

    public abstract Class<? extends SOAPEnvelopeExtension> getSOAPEnvelopeExtension();
    public abstract Class<? extends SOAPHeaderExtension> getSOAPHeaderExtension();
    public abstract Class<? extends SOAPBodyExtension> getSOAPBodyExtension();
    public abstract Class<? extends SOAPFaultExtension> getSOAPFaultExtension();
    public abstract Class<? extends SOAPFaultDetailExtension> getSOAPFaultDetailExtension();
}
