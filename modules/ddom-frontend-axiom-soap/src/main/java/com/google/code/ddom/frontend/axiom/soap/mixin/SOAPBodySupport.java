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

import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPFault;

import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAPBodyExtension;

@Mixin(SOAPBodyExtension.class)
public abstract class SOAPBodySupport implements SOAPBody {
    public SOAPFault addFault(Exception e) throws OMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void addFault(SOAPFault soapFault) throws OMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPFault getFault() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getFirstElementLocalName() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMNamespace getFirstElementNS() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean hasFault() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
