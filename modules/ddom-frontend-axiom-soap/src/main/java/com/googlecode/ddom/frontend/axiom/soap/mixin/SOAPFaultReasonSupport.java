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

import java.util.List;

import org.apache.axiom.soap.SOAPFaultText;
import org.apache.axiom.soap.SOAPProcessingException;

import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultReason;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultText;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionUtil;

@Mixin(AxiomSOAPFaultReason.class)
public abstract class SOAPFaultReasonSupport implements AxiomSOAPFaultReason {
    public final void addSOAPText(SOAPFaultText soapFaultText) throws SOAPProcessingException {
        addChild(soapFaultText);
    }

    public List getAllSoapTexts() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultText getFirstSOAPText() {
        try {
            return coreGetFirstChildByType(AxiomSOAPFaultText.class);
        } catch (DeferredParsingException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public SOAPFaultText getSOAPFaultText(String language) {
        // TODO
        throw new UnsupportedOperationException();
    }

}
