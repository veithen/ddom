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
package com.google.code.ddom.dom.impl;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.google.code.ddom.core.model.*;

/**
 * Aspect implementing {@link Node#getNodeValue()} and {@link Node#setNodeValue(String)}.
 * 
 * @author Andreas Veithen
 */
public aspect NodeValue {
    public final String AttributeImpl.getNodeValue() throws DOMException {
        return getValue();
    }

    public final void AttributeImpl.setNodeValue(String nodeValue) throws DOMException {
        setValue(nodeValue);
    }
    
    public final String CharacterDataImpl.getNodeValue() throws DOMException {
        return getData();
    }

    public final void CharacterDataImpl.setNodeValue(String nodeValue) throws DOMException {
        this.setData(nodeValue);
    }

    public final String DocumentFragmentImpl.getNodeValue() throws DOMException {
        return null;
    }

    public final void DocumentFragmentImpl.setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String DocumentImpl.getNodeValue() throws DOMException {
        return null;
    }

    public final void DocumentImpl.setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String DocumentTypeImpl.getNodeValue() throws DOMException {
        return null;
    }

    public final void DocumentTypeImpl.setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String ElementImpl.getNodeValue() throws DOMException {
        return null;
    }

    public final void ElementImpl.setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }
    
    public final String EntityReferenceImpl.getNodeValue() throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void EntityReferenceImpl.setNodeValue(String nodeValue) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String ProcessingInstructionImpl.getNodeValue() throws DOMException {
        return getData();
    }

    public final void ProcessingInstructionImpl.setNodeValue(String nodeValue) throws DOMException {
        setData(nodeValue);
    }
}
