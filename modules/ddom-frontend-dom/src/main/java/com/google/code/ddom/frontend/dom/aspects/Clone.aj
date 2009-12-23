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

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.google.code.ddom.core.model.*;
import com.google.code.ddom.frontend.dom.intf.DOMChildNode;
import com.google.code.ddom.frontend.dom.intf.DOMElement;
import com.google.code.ddom.frontend.dom.intf.DOMTextNode;
import com.google.code.ddom.spi.model.CoreAttribute;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreElement;
import com.google.code.ddom.spi.model.NodeFactory;

import com.google.code.ddom.frontend.dom.intf.*;

public aspect Clone {
    public final Node DOMAttribute.cloneNode(boolean deep) {
        // TODO: optimize!
        // Attributes are always deep cloned
        return deepClone();
    }
    
    public final Node DOMNSAwareTypedAttribute.shallowClone() {
        CoreDocument document = getDocument();
        return (Node)document.getNodeFactory().createAttribute(document, coreGetNamespaceURI(), coreGetLocalName(), coreGetPrefix(), null, coreGetType());
    }
    
    public final Node DOMNSUnawareTypedAttribute.shallowClone() {
        CoreDocument document = getDocument();
        return (Node)document.getNodeFactory().createAttribute(document, coreGetName(), null, coreGetType());
    }

    public final Node DOMNamespaceDeclaration.shallowClone() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public final Node DOMParentNode.deepClone() {
        Node clone = shallowClone();
        DOMChildNode child = (DOMChildNode)coreGetFirstChild();
        while (child != null) {
            clone.appendChild(child.cloneNode(true));
            child = (DOMChildNode)child.coreGetNextSibling();
        }
        return clone;
    }
    
    public abstract Node DOMParentNode.shallowClone();

    public final Node DOMComment.cloneNode(boolean deep) {
        CoreDocument document = getDocument();
        return (Node)document.getNodeFactory().createComment(document, getData());
    }

    public final Node DOMDocumentFragment.cloneNode(boolean deep) {
        // TODO: check this (maybe a fragment is always deeply cloned?)
        return deep ? deepClone() : shallowClone();
    }

    public final Node DOMDocumentFragment.shallowClone() {
        CoreDocument document = getDocument();
        return (Node)document.getNodeFactory().createDocumentFragment(document);
    }
    
    public final Node DOMDocument.cloneNode(boolean deep) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public final Node DOMDocument.shallowClone() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node DOMDocumentType.cloneNode(boolean deep) {
        // TODO: factory method here!
        return (Node)new DocumentTypeImpl(getDocument(), coreGetRootName(), coreGetPublicId(), coreGetSystemId());
    }
    
    public final Node DOMElement.cloneNode(boolean deep) {
        return deep ? deepClone() : shallowClone();
    }

    // TODO: review return type (should be DOMNode)
    public final Node DOMElement.shallowClone() {
        CoreElement clone = shallowCloneWithoutAttributes();
        CoreAttribute attr = coreGetFirstAttribute();
        while (attr != null) {
            // TODO: this could be optimized
            ((DOMElement)clone).setAttributeNode((Attr)((Attr)attr).cloneNode(false));
            attr = attr.coreGetNextAttribute();
        }
        return (Node)clone;
    }
    
    public abstract CoreElement DOMElement.shallowCloneWithoutAttributes();

    public final CoreElement DOMNSAwareElement.shallowCloneWithoutAttributes() {
        CoreDocument document = getDocument();
        NodeFactory factory = document.getNodeFactory();
        return factory.createElement(document, coreGetNamespaceURI(), coreGetLocalName(), coreGetPrefix(), true);
    }
    
    public final CoreElement DOMNSUnawareElement.shallowCloneWithoutAttributes() {
        CoreDocument document = getDocument();
        NodeFactory factory = document.getNodeFactory();
        return factory.createElement(document, coreGetName(), true);
    }

    public final Node DOMEntityReference.cloneNode(boolean deep) {
        CoreDocument document = getDocument();
        return (Node)document.getNodeFactory().createEntityReference(document, coreGetName());
    }

    public final Node DOMProcessingInstruction.cloneNode(boolean deep) {
        CoreDocument document = getDocument();
        return (Node)document.getNodeFactory().createProcessingInstruction(document, getTarget(), getData());
    }

    public final Node DOMTextNode.cloneNode(boolean deep) {
        return createNewTextNode(getData());
    }
    
    public abstract DOMTextNode DOMTextNode.createNewTextNode(String data);

    public final DOMTextNode DOMText.createNewTextNode(String data) {
        CoreDocument document = getDocument();
        return (DOMTextNode)document.getNodeFactory().createText(document, data);
    }

    public final DOMTextNode DOMCDATASection.createNewTextNode(String data) {
        CoreDocument document = getDocument();
        return (DOMTextNode)document.getNodeFactory().createCDATASection(document, data);
    }
}
