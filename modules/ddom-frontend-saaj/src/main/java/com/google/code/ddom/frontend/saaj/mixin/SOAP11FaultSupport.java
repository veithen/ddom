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
package com.google.code.ddom.frontend.saaj.mixin;

import javax.xml.soap.SOAPException;

import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.saaj.ext.SOAP11FaultExtension;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPFault;
import com.google.code.ddom.frontend.saaj.support.SOAPVersion;

@Mixin(SOAP11FaultExtension.class)
public abstract class SOAP11FaultSupport implements SAAJSOAPFault {
    public final SOAPVersion getSOAPVersion() {
        return SOAPVersion.SOAP11;
    }

    public final String getFaultRole() {
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    public final void setFaultRole(String uri) throws SOAPException {
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }
}
