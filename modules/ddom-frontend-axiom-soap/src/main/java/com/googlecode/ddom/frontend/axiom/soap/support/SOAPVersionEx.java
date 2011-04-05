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
package com.googlecode.ddom.frontend.axiom.soap.support;

import javax.xml.namespace.QName;

import org.apache.axiom.soap.SOAP11Version;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAP12Version;
import org.apache.axiom.soap.SOAPConstants;
import org.apache.axiom.soap.SOAPVersion;

import com.googlecode.ddom.core.Sequence;
import com.googlecode.ddom.core.SequenceBuilder;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP11Body;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP11Envelope;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP11Fault;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP11FaultCode;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP11FaultDetail;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP11FaultReason;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP11FaultRole;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP11Header;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP11HeaderBlock;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12Body;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12Envelope;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12Fault;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultCode;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultDetail;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultNode;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultReason;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultRole;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultSubCode;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultValue;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12Header;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12HeaderBlock;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPBody;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPEnvelope;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFault;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultCode;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultDetail;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultNode;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultReason;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultRole;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultSubCode;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultValue;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPHeader;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPHeaderBlock;

public abstract class SOAPVersionEx {
    public static final SOAPVersionEx SOAP11 = new SOAPVersionEx(
            SOAP11Version.getSingleton(),
            new SequenceBuilder()
                .addItem(AxiomSOAP11FaultCode.class, "", "faultcode")
                .addItem(AxiomSOAP11FaultReason.class, "", "faultstring")
                .addItem(AxiomSOAP11FaultRole.class, "", "faultactor")
                .addItem(AxiomSOAP11FaultDetail.class, "", "detail")
                .enableMatchByInterface().build(),
            null) {
        
        @Override
        public QName getFaultNodeQName() {
            return null;
        }

        @Override
        public QName getFaultValueQName() {
            return null;
        }

        @Override
        public QName getFaultSubCodeQName() {
            return null;
        }

        @Override
        public Class<? extends AxiomSOAPEnvelope> getSOAPEnvelopeClass() {
            return AxiomSOAP11Envelope.class;
        }

        @Override
        public Class<? extends AxiomSOAPHeader> getSOAPHeaderClass() {
            return AxiomSOAP11Header.class;
        }

        @Override
        public Class<? extends AxiomSOAPHeaderBlock> getSOAPHeaderBlockClass() {
            return AxiomSOAP11HeaderBlock.class;
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
        public Class<? extends AxiomSOAPFaultCode> getSOAPFaultCodeClass() {
            return AxiomSOAP11FaultCode.class;
        }

        @Override
        public Class<? extends AxiomSOAPFaultReason> getSOAPFaultReasonClass() {
            return AxiomSOAP11FaultReason.class;
        }

        @Override
        public Class<? extends AxiomSOAPFaultNode> getSOAPFaultNodeClass() {
            return null;
        }

        @Override
        public Class<? extends AxiomSOAPFaultRole> getSOAPFaultRoleClass() {
            return AxiomSOAP11FaultRole.class;
        }

        @Override
        public Class<? extends AxiomSOAPFaultDetail> getSOAPFaultDetailClass() {
            return AxiomSOAP11FaultDetail.class;
        }

        @Override
        public Class<? extends AxiomSOAPFaultValue> getSOAPFaultValueClass() {
            return null;
        }

        @Override
        public Class<? extends AxiomSOAPFaultSubCode> getSOAPFaultSubCodeClass() {
            return null;
        }

        @Override
        public boolean isUltimateReceiverRole(String role) {
            return role == null || role.length() == 0;
        }

        @Override
        public boolean isNoneRole(String role) {
            return false;
        }
        
        @Override
        public String formatMustUnderstand(boolean mustUnderstand) {
            return mustUnderstand ? SOAPConstants.ATTR_MUSTUNDERSTAND_1 : SOAPConstants.ATTR_MUSTUNDERSTAND_0;
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
    };

    public static final SOAPVersionEx SOAP12 = new SOAPVersionEx(
            SOAP12Version.getSingleton(),
            new SequenceBuilder()
                .addItem(AxiomSOAP12FaultCode.class, SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI, "Code")
                .addItem(AxiomSOAP12FaultReason.class, SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI, "Reason")
                .addItem(AxiomSOAP12FaultNode.class, SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI, "Node")
                .addItem(AxiomSOAP12FaultRole.class, SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI, "Role")
                .addItem(AxiomSOAP12FaultDetail.class, SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI, "Detail")
                .enableMatchByInterface().build(),
            new SequenceBuilder()
                .addItem(AxiomSOAP12FaultValue.class, SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI, "Value")
                .addItem(AxiomSOAP12FaultSubCode.class, SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI, "SubCode")
                .enableMatchByInterface().build()) {
        
        @Override
        public QName getFaultNodeQName() {
            return SOAP12Constants.QNAME_FAULT_NODE;
        }

        @Override
        public QName getFaultValueQName() {
            return SOAP12Constants.QNAME_FAULT_VALUE;
        }

        @Override
        public QName getFaultSubCodeQName() {
            return SOAP12Constants.QNAME_FAULT_SUBCODE;
        }

        @Override
        public Class<? extends AxiomSOAPEnvelope> getSOAPEnvelopeClass() {
            return AxiomSOAP12Envelope.class;
        }

        @Override
        public Class<? extends AxiomSOAPHeader> getSOAPHeaderClass() {
            return AxiomSOAP12Header.class;
        }

        @Override
        public Class<? extends AxiomSOAPHeaderBlock> getSOAPHeaderBlockClass() {
            return AxiomSOAP12HeaderBlock.class;
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
        public Class<? extends AxiomSOAPFaultCode> getSOAPFaultCodeClass() {
            return AxiomSOAP12FaultCode.class;
        }

        @Override
        public Class<? extends AxiomSOAPFaultReason> getSOAPFaultReasonClass() {
            return AxiomSOAP12FaultReason.class;
        }

        @Override
        public Class<? extends AxiomSOAPFaultNode> getSOAPFaultNodeClass() {
            return AxiomSOAP12FaultNode.class;
        }

        @Override
        public Class<? extends AxiomSOAPFaultRole> getSOAPFaultRoleClass() {
            return AxiomSOAP12FaultRole.class;
        }

        @Override
        public Class<? extends AxiomSOAPFaultDetail> getSOAPFaultDetailClass() {
            return AxiomSOAP12FaultDetail.class;
        }

        @Override
        public Class<? extends AxiomSOAPFaultValue> getSOAPFaultValueClass() {
            return AxiomSOAP12FaultValue.class;
        }

        @Override
        public Class<? extends AxiomSOAPFaultSubCode> getSOAPFaultSubCodeClass() {
            return AxiomSOAP12FaultSubCode.class;
        }

        @Override
        public boolean isUltimateReceiverRole(String role) {
            return role == null || role.length() == 0 || role.equals(SOAP12Constants.SOAP_ROLE_ULTIMATE_RECEIVER);
        }

        @Override
        public boolean isNoneRole(String role) {
            return role.equals(SOAP12Constants.SOAP_ROLE_NONE);
        }
        
        @Override
        public String formatMustUnderstand(boolean mustUnderstand) {
            return mustUnderstand ? SOAPConstants.ATTR_MUSTUNDERSTAND_TRUE : SOAPConstants.ATTR_MUSTUNDERSTAND_FALSE;
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
    };
    
    private final SOAPVersion soapVersion;
    private final Sequence envelopeSequence;
    private final Sequence faultSequence;
    private final Sequence faultClassifierSequence;
    
    public SOAPVersionEx(SOAPVersion soapVersion, Sequence faultSequence, Sequence faultClassifierSequence) {
        this.soapVersion = soapVersion;
        envelopeSequence = new SequenceBuilder()
                .addItem(getSOAPHeaderClass(), getEnvelopeURI(), SOAPConstants.HEADER_LOCAL_NAME)
                .addItem(getSOAPBodyClass(), getEnvelopeURI(), SOAPConstants.BODY_LOCAL_NAME)
                .enableMatchByInterface().build();
        this.faultSequence = faultSequence;
        this.faultClassifierSequence = faultClassifierSequence;
    }

    public final SOAPVersion getSOAPVersion() {
        return soapVersion;
    }

    public final String getEnvelopeURI() {
        return soapVersion.getEnvelopeURI();
    }

    public final QName getFaultCodeQName() {
        return soapVersion.getFaultCodeQName();
    }

    public final QName getFaultReasonQName() {
        return soapVersion.getFaultReasonQName();
    }
    
    public abstract QName getFaultNodeQName();

    public final QName getFaultRoleQName() {
        return soapVersion.getFaultRoleQName();
    }

    public final QName getFaultDetailQName() {
        return soapVersion.getFaultDetailQName();
    }
    
    public abstract QName getFaultValueQName();
    public abstract QName getFaultSubCodeQName();

    public final Sequence getEnvelopeSequence() {
        return envelopeSequence;
    }

    public final Sequence getFaultSequence() {
        return faultSequence;
    }

    public final Sequence getFaultClassifierSequence() {
        return faultClassifierSequence;
    }

    public abstract Class<? extends AxiomSOAPEnvelope> getSOAPEnvelopeClass();
    public abstract Class<? extends AxiomSOAPHeader> getSOAPHeaderClass();
    public abstract Class<? extends AxiomSOAPHeaderBlock> getSOAPHeaderBlockClass();
    public abstract Class<? extends AxiomSOAPBody> getSOAPBodyClass();
    public abstract Class<? extends AxiomSOAPFault> getSOAPFaultClass();
    public abstract Class<? extends AxiomSOAPFaultCode> getSOAPFaultCodeClass();
    public abstract Class<? extends AxiomSOAPFaultReason> getSOAPFaultReasonClass();
    public abstract Class<? extends AxiomSOAPFaultNode> getSOAPFaultNodeClass();
    public abstract Class<? extends AxiomSOAPFaultRole> getSOAPFaultRoleClass();
    public abstract Class<? extends AxiomSOAPFaultDetail> getSOAPFaultDetailClass();
    public abstract Class<? extends AxiomSOAPFaultValue> getSOAPFaultValueClass();
    public abstract Class<? extends AxiomSOAPFaultSubCode> getSOAPFaultSubCodeClass();
    
    public abstract boolean isUltimateReceiverRole(String role);
    public abstract boolean isNoneRole(String role);

    public abstract String formatMustUnderstand(boolean mustUnderstand);
    // TODO: in the SAAJ front-end we also have a SOAP version specific parseMustUnderstand method; should we have that for Axiom too?
    
    public abstract int getFaultCodeIndex();
    public abstract int getFaultReasonIndex();
    public abstract int getFaultNodeIndex();
    public abstract int getFaultRoleIndex();
    public abstract int getFaultDetailIndex();
}
