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

import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12Body;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12Envelope;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12Fault;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultCode;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultDetail;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultReason;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12FaultRole;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAP12Header;
import com.google.code.ddom.frontend.axiom.soap.intf.HasSOAPVersion;
import com.google.code.ddom.frontend.axiom.soap.support.SOAPVersionEx;

@Mixin({AxiomSOAP12Body.class, AxiomSOAP12Envelope.class, AxiomSOAP12Fault.class, AxiomSOAP12FaultCode.class,
        AxiomSOAP12FaultDetail.class, AxiomSOAP12FaultReason.class, AxiomSOAP12FaultRole.class, AxiomSOAP12Header.class})
public abstract class SOAP12Support implements HasSOAPVersion {
    public final SOAPVersionEx getSOAPVersionEx() {
        return SOAPVersionEx.SOAP12;
    }
}
