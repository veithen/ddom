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
import org.w3c.dom.Node;

import com.google.code.ddom.frontend.dom.intf.*;

/**
 * Aspect implementing {@link Node#getNodeValue()} and {@link Node#setNodeValue(String)}.
 * 
 * @author Andreas Veithen
 */
public aspect NodeValue {
    public final String DOMAttribute.getNodeValue() throws DOMException {
        return getValue();
    }

    public final void DOMAttribute.setNodeValue(String nodeValue) throws DOMException {
        setValue(nodeValue);
    }
    
    public final String DOMCharacterData.getNodeValue() throws DOMException {
        return getData();
    }

    public final void DOMCharacterData.setNodeValue(String nodeValue) throws DOMException {
        this.setData(nodeValue);
    }

    public final String DOMDocumentFragment.getNodeValue() throws DOMException {
        return null;
    }

    public final void DOMDocumentFragment.setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String DOMDocument.getNodeValue() throws DOMException {
        return null;
    }

    public final void DOMDocument.setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String DOMDocumentTypeDeclaration.getNodeValue() throws DOMException {
        return null;
    }

    public final void DOMDocumentTypeDeclaration.setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String DOMElement.getNodeValue() throws DOMException {
        return null;
    }

    public final void DOMElement.setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }
    
    public final String DOMEntityReference.getNodeValue() throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void DOMEntityReference.setNodeValue(String nodeValue) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String DOMProcessingInstruction.getNodeValue() throws DOMException {
        return getData();
    }

    public final void DOMProcessingInstruction.setNodeValue(String nodeValue) throws DOMException {
        setData(nodeValue);
    }
}
