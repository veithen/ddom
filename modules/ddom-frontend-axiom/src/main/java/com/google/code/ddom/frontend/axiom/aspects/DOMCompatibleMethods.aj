/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.ddom.frontend.axiom.aspects;

import com.google.code.ddom.frontend.axiom.intf.AxiomNamedNode;
import com.google.code.ddom.frontend.axiom.intf.AxiomProcessingInstruction;

/**
 * Aspect defining methods that overlap with DOM. All methods defined here have the same behavior in
 * DOM and Axiom. In order to avoid conflicts during weaving, this aspect will be removed
 * dynamically if the DOM and Axiom frontends are used simultaneously. Thus, the methods defined by
 * the DOM frontend will take precedence over the methods defined here.
 * 
 * @author Andreas Veithen
 */
public aspect DOMCompatibleMethods {
    public final String AxiomNamedNode.getLocalName() {
        return coreGetLocalName();
    }

    public String AxiomProcessingInstruction.getTarget() {
        return coreGetTarget();
    }
}
