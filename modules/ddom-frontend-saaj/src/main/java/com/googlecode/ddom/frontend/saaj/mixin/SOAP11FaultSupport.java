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
package com.googlecode.ddom.frontend.saaj.mixin;

import javax.xml.soap.SOAPException;

import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11Fault;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPFault;

@Mixin(SAAJSOAP11Fault.class)
public abstract class SOAP11FaultSupport implements SAAJSOAPFault {
    public final String getFaultRole() {
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    public final void setFaultRole(String uri) throws SOAPException {
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }
}