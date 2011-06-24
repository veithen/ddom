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
package com.googlecode.ddom.frontend.axiom.mixin.dom;

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomElement;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionUtil;

@Mixin(CoreNSAwareElement.class)
public abstract class ElementSupport implements AxiomElement {
    public final String getNamespaceURI() {
        try {
            String namespaceURI = coreGetNamespaceURI();
            return namespaceURI.length() == 0 ? null : namespaceURI;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
}