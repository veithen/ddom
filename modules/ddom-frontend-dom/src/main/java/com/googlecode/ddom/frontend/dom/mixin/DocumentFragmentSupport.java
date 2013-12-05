/*
 * Copyright 2009-2013 Andreas Veithen
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

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.*;
import com.googlecode.ddom.frontend.dom.support.DOMExceptionTranslator;
import com.googlecode.ddom.frontend.dom.support.Policies;

@Mixin(CoreDocumentFragment.class)
public abstract class DocumentFragmentSupport implements DOMDocumentFragment {
    public final boolean hasAttributes() {
        return false;
    }

    public final NamedNodeMap getAttributes() {
        return null;
    }

    public final DOMParentNode shallowClone() {
        return (DOMDocumentFragment)coreGetNodeFactory().createDocumentFragment(coreGetOwnerDocument(true));
    }

    public final Document getOwnerDocument() {
        return (Document)coreGetOwnerDocument(true);
    }

    public final Node getParentNode() {
        return null;
    }

    public final String getTextContent() {
        try {
            return coreGetTextContent(Policies.GET_TEXT_CONTENT);
        } catch (CoreModelException ex) {
            throw DOMExceptionTranslator.translate(ex);
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

    public final String getNodeName() {
        return "#document-fragment";
    }
    
    public final CoreElement getNamespaceContext() {
        return null;
    }

    public final void normalize(NormalizationConfig config) throws AbortNormalizationException {
        normalizeChildren(config);
    }
}
