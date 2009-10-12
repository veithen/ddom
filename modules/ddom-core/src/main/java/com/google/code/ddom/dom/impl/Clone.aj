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

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.google.code.ddom.core.model.*;
import com.google.code.ddom.spi.model.CoreAttribute;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreElement;
import com.google.code.ddom.spi.model.NodeFactory;

public aspect Clone {
    public final Node AttributeImpl.cloneNode(boolean deep) {
        // TODO: optimize!
        // Attributes are always deep cloned
        return deepClone();
    }
    
    public final Node NSAwareTypedAttributeImpl.shallowClone() {
        CoreDocument document = getDocument();
        return (Node)document.getNodeFactory().createAttribute(document, coreGetNamespaceURI(), coreGetLocalName(), coreGetPrefix(), null, getType());
    }
    
    public final Node NSUnawareTypedAttributeImpl.shallowClone() {
        CoreDocument document = getDocument();
        return (Node)document.getNodeFactory().createAttribute(document, coreGetName(), null, getType());
    }

    public final Node NSDecl.shallowClone() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public final Node ParentNodeImpl.deepClone() {
        Node clone = shallowClone();
        DOMChildNode child = (DOMChildNode)coreGetFirstChild();
        while (child != null) {
            clone.appendChild(child.cloneNode(true));
            child = (DOMChildNode)child.coreGetNextSibling();
        }
        return clone;
    }
    
    public abstract Node ParentNodeImpl.shallowClone();

    public final Node CommentImpl.cloneNode(boolean deep) {
        CoreDocument document = getDocument();
        return (Node)document.getNodeFactory().createComment(document, getData());
    }

    public final Node DocumentFragmentImpl.cloneNode(boolean deep) {
        // TODO: check this (maybe a fragment is always deeply cloned?)
        return deep ? deepClone() : shallowClone();
    }

    public final Node DocumentFragmentImpl.shallowClone() {
        CoreDocument document = getDocument();
        return (Node)document.getNodeFactory().createDocumentFragment(document);
    }
    
    public final Node DocumentImpl.cloneNode(boolean deep) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public final Node DocumentImpl.shallowClone() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node DocumentTypeImpl.cloneNode(boolean deep) {
        // TODO: factory method here!
        return (Node)new DocumentTypeImpl(getDocument(), coreGetRootName(), coreGetPublicId(), coreGetSystemId());
    }
    
    public final Node ElementImpl.cloneNode(boolean deep) {
        return deep ? deepClone() : shallowClone();
    }

    // TODO: review return type (should be DOMNode)
    public final Node ElementImpl.shallowClone() {
        CoreElement clone = shallowCloneWithoutAttributes();
        CoreAttribute attr = internalGetFirstAttribute();
        while (attr != null) {
            // TODO: this could be optimized
            ((DOMElement)clone).setAttributeNode((Attr)((Attr)attr).cloneNode(false));
            attr = attr.internalGetNextAttribute();
        }
        return (Node)clone;
    }
    
    public abstract CoreElement ElementImpl.shallowCloneWithoutAttributes();

    public final CoreElement NSAwareElementImpl.shallowCloneWithoutAttributes() {
        CoreDocument document = getDocument();
        NodeFactory factory = document.getNodeFactory();
        return factory.createElement(document, coreGetNamespaceURI(), coreGetLocalName(), coreGetPrefix(), true);
    }
    
    public final CoreElement NSUnawareElementImpl.shallowCloneWithoutAttributes() {
        CoreDocument document = getDocument();
        NodeFactory factory = document.getNodeFactory();
        return factory.createElement(document, coreGetName(), true);
    }

    public final Node EntityReferenceImpl.cloneNode(boolean deep) {
        CoreDocument document = getDocument();
        return (Node)document.getNodeFactory().createEntityReference(document, coreGetName());
    }

    public final Node ProcessingInstructionImpl.cloneNode(boolean deep) {
        CoreDocument document = getDocument();
        return (Node)document.getNodeFactory().createProcessingInstruction(document, getTarget(), getData());
    }

    public final Node TextNodeImpl.cloneNode(boolean deep) {
        return createNewTextNode(getData());
    }
    
    public abstract DOMTextNode TextNodeImpl.createNewTextNode(String data);

    public final DOMTextNode TextImpl.createNewTextNode(String data) {
        CoreDocument document = getDocument();
        return (DOMTextNode)document.getNodeFactory().createText(document, data);
    }

    public final DOMTextNode CDATASectionImpl.createNewTextNode(String data) {
        CoreDocument document = getDocument();
        return (DOMTextNode)document.getNodeFactory().createCDATASection(document, data);
    }
}
