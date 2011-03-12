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
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axiom.soap.SOAPVersion;

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPEnvelope;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionUtil;

@Mixin(AxiomSOAPEnvelope.class)
public abstract class SOAPEnvelopeSupport implements AxiomSOAPEnvelope {
    public SOAPHeader getHeader() throws OMException {
        // TODO
        OMElement firstElement = getFirstElement();
        return firstElement instanceof SOAPHeader ? (SOAPHeader)firstElement : null;
    }

    public final SOAPBody getBody() throws OMException {
        try {
            return (SOAPBody)coreGetElementFromSequence(getSOAPVersionEx().getEnvelopeSequence(), 1, false);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final String getSOAPBodyFirstElementLocalName() {
        SOAPBody body = getBody();
        return body == null ? null : body.getFirstElementLocalName();
    }

    public final OMNamespace getSOAPBodyFirstElementNS() {
        SOAPBody body = getBody();
        return body == null ? null : body.getFirstElementNS();
    }

    public final SOAPVersion getVersion() {
        return getSOAPVersionEx().getSOAPVersion();
    }

    public boolean hasFault() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
