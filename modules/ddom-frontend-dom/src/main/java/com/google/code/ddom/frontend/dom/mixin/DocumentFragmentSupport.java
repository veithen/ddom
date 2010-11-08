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
package com.google.code.ddom.frontend.dom.mixin;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.google.code.ddom.core.CoreDocumentFragment;
import com.google.code.ddom.core.CoreElement;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.dom.intf.*;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;

@Mixin(CoreDocumentFragment.class)
public abstract class DocumentFragmentSupport implements DOMDocumentFragment {
    public final boolean hasAttributes() {
        return false;
    }

    public final NamedNodeMap getAttributes() {
        return null;
    }

    public final Node cloneNode(boolean deep) {
        // TODO: check this (maybe a fragment is always deeply cloned?)
        return deep ? deepClone() : shallowClone();
    }

    public final Node shallowClone() {
        return (Node)coreGetNodeFactory().createDocumentFragment(coreGetOwnerDocument(true));
    }

    public final Document getOwnerDocument() {
        return (Document)coreGetOwnerDocument(true);
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
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node getNextSibling() {
        return null;
    }

    public final Node getPreviousSibling() {
        return null;
    }

    public final short getNodeType() {
        return Node.DOCUMENT_FRAGMENT_NODE;
    }

    public final String getNodeValue() throws DOMException {
        return null;
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String getNodeName() {
        return "#document-fragment";
    }
    
    public final CoreElement getNamespaceContext() {
        return null;
    }

    public final String getNamespaceURI() {
        return null;
    }

    public final String getPrefix() {
        return null;
    }

    public final void setPrefix(String prefix) throws DOMException {
        // Ignored
    }

    public final String getLocalName() {
        return null;
    }
    
    public final void normalize(NormalizationConfig config) throws AbortNormalizationException {
        normalizeChildren(config);
    }
}
