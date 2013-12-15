/*
 * Copyright 2009-2011,2013 Andreas Veithen
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

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSUnawareElement;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.DOMElement;
import com.googlecode.ddom.frontend.dom.intf.DOMNSUnawareElement;
import com.googlecode.ddom.frontend.dom.support.DOMExceptionTranslator;

@Mixin(CoreNSUnawareElement.class)
public abstract class NSUnawareElementSupport implements DOMNSUnawareElement {
    public final DOMElement shallowCloneWithoutAttributes() throws DeferredBuildingException {
        return (DOMNSUnawareElement)coreGetNodeFactory().createElement(coreGetOwnerDocument(true), coreGetName());
    }
    
    public final String getTagName() {
        try {
            return coreGetName();
        } catch (CoreModelException ex) {
            throw DOMExceptionTranslator.translate(ex);
        }
    }
}
