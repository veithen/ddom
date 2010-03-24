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
package com.google.code.ddom.frontend.dom.aspects;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.frontend.dom.intf.DOMAttribute;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;

public aspect AttributeSupport {
    public final boolean DOMAttribute.hasAttributes() {
        return false;
    }

    public final NamedNodeMap DOMAttribute.getAttributes() {
        return null;
    }
    
    public final String DOMAttribute.getValue() {
        try {
            return coreGetValue();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final void DOMAttribute.setValue(String value) throws DOMException {
        try {
            coreSetValue(value);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final Element DOMAttribute.getOwnerElement() {
        return (Element)coreGetOwnerElement();
    }

    public final boolean DOMAttribute.getSpecified() {
        // TODO
        return true;
    }

    public final Node DOMAttribute.cloneNode(boolean deep) {
        // TODO: optimize!
        // Attributes are always deep cloned
        return deepClone();
    }
}
