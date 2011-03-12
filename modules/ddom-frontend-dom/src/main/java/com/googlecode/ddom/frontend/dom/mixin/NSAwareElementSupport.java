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
package com.googlecode.ddom.frontend.dom.mixin;

import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.DOMNSAwareElement;

@Mixin(CoreNSAwareElement.class)
public abstract class NSAwareElementSupport implements DOMNSAwareElement {
    public final CoreElement shallowCloneWithoutAttributes() {
        return coreGetNodeFactory().createElement(coreGetOwnerDocument(true), coreGetNamespaceURI(), coreGetLocalName(), coreGetPrefix());
    }
    
    public final String getTagName() {
        return internalGetName();
    }
}