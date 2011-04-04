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
package com.googlecode.ddom.frontend.axiom.soap.mixin;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.soap.SOAPConstants;
import org.apache.axiom.soap.SOAPFaultCode;
import org.apache.axiom.soap.SOAPFaultDetail;
import org.apache.axiom.soap.SOAPFaultNode;
import org.apache.axiom.soap.SOAPFaultReason;
import org.apache.axiom.soap.SOAPFaultRole;
import org.apache.axiom.soap.SOAPProcessingException;

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFault;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultCode;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultDetail;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultNode;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultReason;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultRole;
import com.googlecode.ddom.frontend.axiom.soap.support.SOAPVersionEx;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionUtil;

@Mixin(AxiomSOAPFault.class)
public abstract class SOAPFaultSupport implements AxiomSOAPFault {
    public final SOAPFaultCode getCode() {
        try {
            SOAPVersionEx version = getSOAPVersionEx();
            return (AxiomSOAPFaultCode)coreGetElementFromSequence(version.getFaultSequence(), version.getFaultCodeIndex(), false);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final void setCode(SOAPFaultCode soapFaultCode) throws SOAPProcessingException {
        try {
            SOAPVersionEx version = getSOAPVersionEx();
            coreInsertElementInSequence(version.getFaultSequence(), version.getFaultCodeIndex(), (AxiomSOAPFaultCode)soapFaultCode);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final SOAPFaultReason getReason() {
        try {
            SOAPVersionEx version = getSOAPVersionEx();
            return (AxiomSOAPFaultReason)coreGetElementFromSequence(version.getFaultSequence(), version.getFaultReasonIndex(), false);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final void setReason(SOAPFaultReason reason) throws SOAPProcessingException {
        try {
            SOAPVersionEx version = getSOAPVersionEx();
            coreInsertElementInSequence(version.getFaultSequence(), version.getFaultReasonIndex(), (AxiomSOAPFaultReason)reason);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final SOAPFaultNode getNode() {
        try {
            SOAPVersionEx version = getSOAPVersionEx();
            return (AxiomSOAPFaultNode)coreGetElementFromSequence(version.getFaultSequence(), version.getFaultNodeIndex(), false);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final void setNode(SOAPFaultNode node) throws SOAPProcessingException {
        try {
            SOAPVersionEx version = getSOAPVersionEx();
            coreInsertElementInSequence(version.getFaultSequence(), version.getFaultNodeIndex(), (AxiomSOAPFaultNode)node);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final SOAPFaultRole getRole() {
        try {
            SOAPVersionEx version = getSOAPVersionEx();
            return (AxiomSOAPFaultRole)coreGetElementFromSequence(version.getFaultSequence(), version.getFaultRoleIndex(), false);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final void setRole(SOAPFaultRole role) throws SOAPProcessingException {
        try {
            SOAPVersionEx version = getSOAPVersionEx();
            coreInsertElementInSequence(version.getFaultSequence(), version.getFaultRoleIndex(), (AxiomSOAPFaultRole)role);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final SOAPFaultDetail getDetail() {
        try {
            SOAPVersionEx version = getSOAPVersionEx();
            return (AxiomSOAPFaultDetail)coreGetElementFromSequence(version.getFaultSequence(), version.getFaultDetailIndex(), false);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final void setDetail(SOAPFaultDetail detail) throws SOAPProcessingException {
        try {
            SOAPVersionEx version = getSOAPVersionEx();
            coreInsertElementInSequence(version.getFaultSequence(), version.getFaultDetailIndex(), (AxiomSOAPFaultDetail)detail);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final Exception getException() throws OMException {
        SOAPFaultDetail detail = getDetail();
        if (detail == null) {
            return null;
        } else {
            OMElement exceptionElement = detail.getFirstChildWithName(
                    new QName(SOAPConstants.SOAP_FAULT_DETAIL_EXCEPTION_ENTRY));
            if (exceptionElement != null) {
                return new Exception(exceptionElement.getText());
            } else {
                return null;
            }
        }
    }

    public final void setException(Exception exception) throws OMException {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter out = new PrintWriter(sw, false);
            exception.printStackTrace(out);
            out.flush();
            SOAPVersionEx version = getSOAPVersionEx();
            AxiomSOAPFaultDetail detail = (AxiomSOAPFaultDetail)coreGetElementFromSequence(version.getFaultSequence(), version.getFaultDetailIndex(), true);
            CoreNSAwareElement entry = detail.coreAppendElement("", SOAPConstants.SOAP_FAULT_DETAIL_EXCEPTION_ENTRY, "");
            entry.coreAppendCharacterData(sw.toString());
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
}
