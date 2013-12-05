/*
 * Copyright 2009-2010,2013 Andreas Veithen
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

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreEntityReference;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.DOMEntityReference;
import com.googlecode.ddom.frontend.dom.support.DOMExceptionTranslator;

@Mixin(CoreEntityReference.class)
public abstract class EntityReferenceSupport implements DOMEntityReference {
    public final Node cloneNode(boolean deep) {
        return (Node)coreGetNodeFactory().createEntityReference(coreGetOwnerDocument(true), coreGetName());
    }

    public final String getTextContent() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setTextContent(@SuppressWarnings("unused") String textContent) {
        throw DOMExceptionTranslator.newDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR);
    }

    public final short getNodeType() {
        return Node.ENTITY_REFERENCE_NODE;
    }
    
    public final String getNodeName() {
        return coreGetName();
    }
    
    public final CoreElement getNamespaceContext() {
        return coreGetParentElement();
    }
}
