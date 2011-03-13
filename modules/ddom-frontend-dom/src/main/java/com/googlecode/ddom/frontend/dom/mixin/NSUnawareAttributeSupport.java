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
package com.googlecode.ddom.frontend.dom.mixin;

import org.w3c.dom.Node;

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSUnawareAttribute;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.DOMNSUnawareAttribute;
import com.googlecode.ddom.frontend.dom.support.DOMExceptionUtil;

@Mixin(CoreNSUnawareAttribute.class)
public abstract class NSUnawareAttributeSupport implements DOMNSUnawareAttribute {
    public final Node shallowClone() throws DeferredParsingException {
        return (Node)coreGetNodeFactory().createAttribute(coreGetOwnerDocument(true), coreGetName(), null, coreGetType());
    }
    
    public final String getName() {
        try {
            return coreGetName();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
}
