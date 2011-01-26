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
package com.google.code.ddom.frontend.axiom.soap.mixin;

import org.apache.axiom.om.OMException;
import org.apache.axiom.soap.SOAPFaultCode;
import org.apache.axiom.soap.SOAPFaultDetail;
import org.apache.axiom.soap.SOAPFaultNode;
import org.apache.axiom.soap.SOAPFaultReason;
import org.apache.axiom.soap.SOAPFaultRole;
import org.apache.axiom.soap.SOAPProcessingException;

import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFault;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultCode;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultDetail;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultReason;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultRole;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.Mixin;

@Mixin(AxiomSOAPFault.class)
public abstract class SOAPFaultSupport implements AxiomSOAPFault {
    public final SOAPFaultCode getCode() {
        try {
            return (AxiomSOAPFaultCode)coreGetElementFromSequence(getSOAPVersionEx().getFaultSequence(), 0, false);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final void setCode(SOAPFaultCode soapFaultCode) throws SOAPProcessingException {
        try {
            coreInsertElementInSequence(getSOAPVersionEx().getFaultSequence(), 0, (AxiomSOAPFaultCode)soapFaultCode);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final SOAPFaultReason getReason() {
        try {
            return (AxiomSOAPFaultReason)coreGetElementFromSequence(getSOAPVersionEx().getFaultSequence(), 1, false);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final void setReason(SOAPFaultReason reason) throws SOAPProcessingException {
        try {
            coreInsertElementInSequence(getSOAPVersionEx().getFaultSequence(), 1, (AxiomSOAPFaultReason)reason);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final SOAPFaultRole getRole() {
        try {
            return (AxiomSOAPFaultRole)coreGetElementFromSequence(getSOAPVersionEx().getFaultSequence(), 2, false);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final void setRole(SOAPFaultRole role) throws SOAPProcessingException {
        try {
            coreInsertElementInSequence(getSOAPVersionEx().getFaultSequence(), 2, (AxiomSOAPFaultRole)role);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final SOAPFaultDetail getDetail() {
        try {
            return (AxiomSOAPFaultDetail)coreGetElementFromSequence(getSOAPVersionEx().getFaultSequence(), 3, false);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final void setDetail(SOAPFaultDetail detail) throws SOAPProcessingException {
        try {
            coreInsertElementInSequence(getSOAPVersionEx().getFaultSequence(), 3, (AxiomSOAPFaultDetail)detail);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public Exception getException() throws OMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPFaultNode getNode() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setException(Exception e) throws OMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setNode(SOAPFaultNode node) throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
