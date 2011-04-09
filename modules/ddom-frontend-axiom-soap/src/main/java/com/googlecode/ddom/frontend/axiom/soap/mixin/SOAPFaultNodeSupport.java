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

import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultNode;

@Mixin(AxiomSOAPFaultNode.class)
public abstract class SOAPFaultNodeSupport implements AxiomSOAPFaultNode {
    // TODO: these methods conflict with DOM methods!
/*
    public String getNodeValue() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setNodeValue(String uri) {
        // TODO
        throw new UnsupportedOperationException();
    }
*/
}
