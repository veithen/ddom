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

import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFault;

@Mixin(AxiomSOAPFault.class)
public abstract class SOAPFaultSupport implements AxiomSOAPFault {
    public SOAPFaultCode getCode() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPFaultDetail getDetail() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Exception getException() throws OMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPFaultNode getNode() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPFaultReason getReason() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPFaultRole getRole() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setCode(SOAPFaultCode soapFaultCode) throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setDetail(SOAPFaultDetail detail) throws SOAPProcessingException {
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

    public void setReason(SOAPFaultReason reason) throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setRole(SOAPFaultRole role) throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
