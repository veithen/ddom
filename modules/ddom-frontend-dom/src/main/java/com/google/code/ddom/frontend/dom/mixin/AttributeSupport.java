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
package com.google.code.ddom.frontend.dom.mixin;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.frontend.dom.intf.AbortNormalizationException;
import com.google.code.ddom.frontend.dom.intf.DOMAttribute;
import com.google.code.ddom.frontend.dom.intf.NormalizationConfig;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;
import com.google.code.ddom.spi.model.Mixin;

@Mixin(CoreAttribute.class)
public abstract class AttributeSupport implements DOMAttribute {
    public final boolean hasAttributes() {
        return false;
    }

    public final NamedNodeMap getAttributes() {
        return null;
    }
    
    public final String getValue() {
        try {
            return coreGetValue();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final void setValue(String value) throws DOMException {
        try {
            coreSetValue(value);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final Element getOwnerElement() {
        return (Element)coreGetOwnerElement();
    }

    public final boolean getSpecified() {
        // TODO
        return true;
    }

    public final Node cloneNode(boolean deep) {
        // TODO: optimize!
        // Attributes are always deep cloned
        return deepClone();
    }

    public final Document getOwnerDocument() {
        return (Document)coreGetDocument();
    }
    
    public final Node getParentNode() {
        return null;
    }

    public final String getTextContent() {
        try {
            return coreGetTextContent();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void setTextContent(String textContent) {
        try {
            coreSetValue(textContent);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final Node getNextSibling() {
        return null;
    }

    public final Node getPreviousSibling() {
        return null;
    }

    public final short getNodeType() {
        return Node.ATTRIBUTE_NODE;
    }

    public final String getNodeValue() throws DOMException {
        return getValue();
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        setValue(nodeValue);
    }

    public final String getNodeName() {
        return getName();
    }
    
    public final CoreElement getNamespaceContext() {
        return coreGetOwnerElement();
    }
    
    public final void normalize(NormalizationConfig config) throws AbortNormalizationException {
        normalizeChildren(config);
    }

    public final TypeInfo getSchemaTypeInfo() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
