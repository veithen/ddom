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

import org.apache.axiom.om.OMFactory;

import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomElement;
import com.googlecode.ddom.frontend.axiom.intf.AxiomNodeFactory;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12Body;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12Envelope;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12Fault;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultCode;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultDetail;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultNode;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultReason;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultRole;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultSubCode;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultText;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultValue;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12Header;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAP12HeaderBlock;
import com.googlecode.ddom.frontend.axiom.soap.intf.HasSOAPVersion;
import com.googlecode.ddom.frontend.axiom.soap.support.SOAPVersionEx;

@Mixin({AxiomSOAP12Body.class, AxiomSOAP12Envelope.class, AxiomSOAP12Fault.class, AxiomSOAP12FaultCode.class,
        AxiomSOAP12FaultDetail.class, AxiomSOAP12FaultNode.class, AxiomSOAP12FaultReason.class, AxiomSOAP12FaultRole.class,
        AxiomSOAP12FaultSubCode.class, AxiomSOAP12FaultText.class, AxiomSOAP12FaultValue.class, AxiomSOAP12Header.class, AxiomSOAP12HeaderBlock.class})
public abstract class SOAP12Support implements AxiomElement, HasSOAPVersion {
    public final SOAPVersionEx getSOAPVersionEx() {
        return SOAPVersionEx.SOAP12;
    }
    
    public OMFactory getDefaultOMFactory() {
        return ((AxiomNodeFactory)coreGetNodeFactory()).getSOAP12Factory();
    }
}
