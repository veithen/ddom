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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPConstants;
import org.apache.axiom.soap.SOAPFault;

import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPBody;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFault;
import com.googlecode.ddom.frontend.axiom.soap.support.SOAPVersionEx;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionTranslator;
import com.googlecode.ddom.frontend.axiom.support.Policies;

@Mixin(AxiomSOAPBody.class)
public abstract class SOAPBodySupport implements AxiomSOAPBody {
    public final SOAPFault addFault(Exception e) throws OMException {
        try {
            SOAPVersionEx version = getSOAPVersionEx();
            AxiomSOAPFault fault = coreAppendElement(version.getSOAPFaultClass(),
                    version.getEnvelopeURI(), SOAPConstants.BODY_FAULT_LOCAL_NAME,
                    SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
            // TODO: fill fault with exception data
            return fault;
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public final void addFault(SOAPFault soapFault) throws OMException {
        try {
            coreAppendChild((AxiomSOAPFault)soapFault, Policies.NODE_MIGRATION_POLICY);
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public final SOAPFault getFault() {
        try {
            CoreElement firstElement = coreGetFirstChildByType(CoreElement.class);
            return firstElement instanceof SOAPFault ? (SOAPFault)firstElement : null;
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public final boolean hasFault() {
        return getFault() != null;
    }

    public final String getFirstElementLocalName() {
        OMElement element = getFirstElement();
        return element == null ? null : element.getLocalName();
    }

    public final OMNamespace getFirstElementNS() {
        OMElement element = getFirstElement();
        return element == null ? null : element.getNamespace();
    }
}
