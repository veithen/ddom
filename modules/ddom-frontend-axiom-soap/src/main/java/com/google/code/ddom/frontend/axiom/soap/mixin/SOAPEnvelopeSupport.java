/*
 * Copyright 2009-2010 Andreas Veithen
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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.soap.SOAPHeader;

import com.google.code.ddom.frontend.axiom.soap.ext.SOAPEnvelope;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPEnvelope;
import com.google.code.ddom.spi.model.Mixin;

@Mixin(SOAPEnvelope.class)
public abstract class SOAPEnvelopeSupport implements AxiomSOAPEnvelope {
    public SOAPHeader getHeader() throws OMException {
        OMElement firstElement = getFirstElement();
        return firstElement instanceof SOAPHeader ? (SOAPHeader)firstElement : null;
    }
}
