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

import org.apache.commons.lang.ObjectUtils;

import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.frontend.dom.intf.DOMAttribute;
import com.google.code.ddom.frontend.dom.intf.DOMCharacterData;
import com.google.code.ddom.frontend.dom.intf.DOMCoreNode;
import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.google.code.ddom.frontend.dom.intf.DOMDocumentFragment;
import com.google.code.ddom.frontend.dom.intf.DOMDocumentType;
import com.google.code.ddom.frontend.dom.intf.DOMElement;
import com.google.code.ddom.frontend.dom.intf.DOMEntityReference;
import com.google.code.ddom.frontend.dom.intf.DOMProcessingInstruction;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;

/**
 * Implements the namespace lookup methods using the algorithms described in <a
 * href="http://www.w3.org/TR/DOM-Level-3-Core/namespaces-algorithms.html">Appendix B</a> of the DOM
 * Level 3 Core specification.
 * 
 * @author Andreas Veithen
 */
public aspect NamespaceLookup {
    public final CoreElement DOMElement.getNamespaceContext() {
        return this;
    }
    
    public final CoreElement DOMAttribute.getNamespaceContext() {
        return coreGetOwnerElement();
    }
    
    public final CoreElement DOMDocument.getNamespaceContext() {
        try {
            return coreGetDocumentElement();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final CoreElement DOMDocumentFragment.getNamespaceContext() {
        return null;
    }
    
    public final CoreElement DOMDocumentType.getNamespaceContext() {
        return null;
    }
    
    public final CoreElement DOMCharacterData.getNamespaceContext() {
        return coreGetParentElement();
    }
    
    public final CoreElement DOMProcessingInstruction.getNamespaceContext() {
        return coreGetParentElement();
    }
    
    public final CoreElement DOMEntityReference.getNamespaceContext() {
        return coreGetParentElement();
    }
    
    public final String DOMCoreNode.lookupNamespaceURI(String prefix) {
        try {
            CoreElement contextElement = getNamespaceContext();
            return contextElement == null ? null : contextElement.coreLookupNamespaceURI(prefix, false);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final String DOMCoreNode.lookupPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            return null;
        } else {
            try {
                CoreElement contextElement = getNamespaceContext();
                return contextElement == null ? null : contextElement.coreLookupPrefix(namespaceURI, false);
            } catch (CoreModelException ex) {
                throw DOMExceptionUtil.translate(ex);
            }
        }
    }

    public final boolean DOMCoreNode.isDefaultNamespace(String namespaceURI) {
        try {
            CoreElement contextElement = getNamespaceContext();
            return contextElement == null ? false : ObjectUtils.equals(namespaceURI, contextElement.coreLookupNamespaceURI(null, false));
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
}
