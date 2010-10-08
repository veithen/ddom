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
import com.google.code.ddom.frontend.saaj.ext.SOAPHeaderElementExtension;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPHeaderElement;

@Mixin(SOAPHeaderElementExtension.class)
public abstract class SOAPHeaderElementSupport implements SAAJSOAPHeaderElement {
    public String getActor() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean getMustUnderstand() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean getRelay() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getRole() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setActor(String arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setMustUnderstand(boolean arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setRelay(boolean arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setRole(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
}