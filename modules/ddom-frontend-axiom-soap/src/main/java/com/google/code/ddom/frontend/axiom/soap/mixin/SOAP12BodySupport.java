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

import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAP12BodyExtension;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPBody;
import com.google.code.ddom.frontend.axiom.soap.support.SOAPVersionEx;

@Mixin(SOAP12BodyExtension.class)
public abstract class SOAP12BodySupport implements AxiomSOAPBody {
    public final SOAPVersionEx getSOAPVersionEx() {
        return SOAPVersionEx.SOAP12;
    }
}
