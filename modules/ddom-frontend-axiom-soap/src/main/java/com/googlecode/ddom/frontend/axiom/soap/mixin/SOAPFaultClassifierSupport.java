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

import org.apache.axiom.soap.SOAPFaultSubCode;
import org.apache.axiom.soap.SOAPFaultValue;
import org.apache.axiom.soap.SOAPProcessingException;

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.Sequence;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultClassifier;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionUtil;

@Mixin(AxiomSOAPFaultClassifier.class)
public abstract class SOAPFaultClassifierSupport implements AxiomSOAPFaultClassifier {
    public final SOAPFaultValue getValue() {
        Sequence seq = getSOAPVersionEx().getFaultClassifierSequence();
        if (seq == null) {
            return null;
        } else {
            try {
                return (SOAPFaultValue)coreGetElementFromSequence(seq, 0, false);
            } catch (CoreModelException ex) {
                throw AxiomExceptionUtil.translate(ex);
            }
        }
    }

    public final void setValue(SOAPFaultValue value) throws SOAPProcessingException {
        // TODO
        throw new SOAPProcessingException("Not supported");
    }

    public final SOAPFaultSubCode getSubCode() {
        Sequence seq = getSOAPVersionEx().getFaultClassifierSequence();
        if (seq == null) {
            return null;
        } else {
            try {
                return (SOAPFaultSubCode)coreGetElementFromSequence(seq, 1, false);
            } catch (CoreModelException ex) {
                throw AxiomExceptionUtil.translate(ex);
            }
        }
    }

    public final void setSubCode(SOAPFaultSubCode subCode) throws SOAPProcessingException {
        throw new SOAPProcessingException("Not supported");
    }
}
