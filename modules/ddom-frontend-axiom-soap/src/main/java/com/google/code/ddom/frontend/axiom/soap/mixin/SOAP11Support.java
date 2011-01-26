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

import org.apache.axiom.om.OMFactory;

import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.intf.AxiomNodeFactory;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11Body;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11Envelope;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11Fault;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11FaultCode;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11FaultDetail;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11FaultReason;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11FaultRole;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11Header;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP11HeaderBlock;
import com.google.code.ddom.frontend.axiom.soap.intf.HasSOAPVersion;
import com.google.code.ddom.frontend.axiom.soap.support.SOAPVersionEx;
import com.googlecode.ddom.frontend.Mixin;

@Mixin({AxiomSOAP11Body.class, AxiomSOAP11Envelope.class, AxiomSOAP11Fault.class, AxiomSOAP11FaultCode.class,
        AxiomSOAP11FaultDetail.class, AxiomSOAP11FaultReason.class, AxiomSOAP11FaultRole.class,
        AxiomSOAP11Header.class, AxiomSOAP11HeaderBlock.class})
public abstract class SOAP11Support implements AxiomElement, HasSOAPVersion {
    public final SOAPVersionEx getSOAPVersionEx() {
        return SOAPVersionEx.SOAP11;
    }
    
    public OMFactory getDefaultOMFactory() {
        return ((AxiomNodeFactory)coreGetNodeFactory()).getSOAP11Factory();
    }
}
