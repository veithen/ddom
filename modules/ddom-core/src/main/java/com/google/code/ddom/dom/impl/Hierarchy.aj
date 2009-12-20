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

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.code.ddom.core.model.*;

/**
 * Aspect implementing {@link Node#getOwnerDocument()} and {@link Node#getParentNode()}.
 * 
 * @author Andreas Veithen
 */
public aspect Hierarchy {
    declare parents: ParentNodeImpl implements DOMParentNode;

    public final Document DOMDocument.getOwnerDocument() {
        return null;
    }

    public final Document DOMAttribute.getOwnerDocument() {
        return (Document)getDocument();
    }
    
    public final Document DOMDocumentFragment.getOwnerDocument() {
        return (Document)getDocument();
    }

    public final Document DOMElement.getOwnerDocument() {
        return (Document)getDocument();
    }

    public final Document LeafNode.getOwnerDocument() {
        return (Document)getDocument();
    }
    
    public final Node DOMAttribute.getParentNode() {
        return null;
    }

    // TODO: should be possible to combine LeafNode and ElementImpl into a single case
    public final Node LeafNode.getParentNode() {
        return (Node)coreGetParent();
    }

    public final Node DOMElement.getParentNode() {
        return (Node)coreGetParent();
    }

    public final Node DOMDocumentFragment.getParentNode() {
        return null;
    }
    
    public final Node DOMDocument.getParentNode() {
        return null;
    }
}
