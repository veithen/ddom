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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.frontend.dom.intf.AbortNormalizationException;
import com.google.code.ddom.frontend.dom.intf.DOMAttribute;
import com.google.code.ddom.frontend.dom.intf.NormalizationConfig;
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

    public final Document DOMAttribute.getOwnerDocument() {
        return (Document)coreGetDocument();
    }
    
    public final Node DOMAttribute.getParentNode() {
        return null;
    }

    public final String DOMAttribute.getTextContent() {
        try {
            return coreGetTextContent();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void DOMAttribute.setTextContent(String textContent) {
        try {
            coreSetValue(textContent);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final Node DOMAttribute.getNextSibling() {
        return null;
    }

    public final Node DOMAttribute.getPreviousSibling() {
        return null;
    }

    public final short DOMAttribute.getNodeType() {
        return Node.ATTRIBUTE_NODE;
    }

    public final String DOMAttribute.getNodeValue() throws DOMException {
        return getValue();
    }

    public final void DOMAttribute.setNodeValue(String nodeValue) throws DOMException {
        setValue(nodeValue);
    }

    public final String DOMAttribute.getNodeName() {
        return getName();
    }
    
    public final CoreElement DOMAttribute.getNamespaceContext() {
        return coreGetOwnerElement();
    }
    
    public final void DOMAttribute.normalize(NormalizationConfig config) throws AbortNormalizationException {
        normalizeChildren(config);
    }

    public final TypeInfo DOMAttribute.getSchemaTypeInfo() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
