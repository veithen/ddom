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

import org.w3c.dom.Node;

import com.google.code.ddom.core.model.ElementImpl;
import com.google.code.ddom.core.model.LeafNodeImpl;

public aspect Sibling {
    declare parents: (LeafNodeImpl || ElementImpl) implements DOMChildNode;
    
    public final Node DOMAttribute.getNextSibling() {
        return null;
    }

    public final Node DOMAttribute.getPreviousSibling() {
        return null;
    }

    public final Node DOMDocument.getNextSibling() {
        return null;
    }

    public final Node DOMDocument.getPreviousSibling() {
        return null;
    }

    public final Node DOMDocumentFragment.getNextSibling() {
        return null;
    }

    public final Node DOMDocumentFragment.getPreviousSibling() {
        return null;
    }

    public final Node DOMChildNode.getNextSibling() {
        return (Node)coreGetNextSibling();
    }

    public final Node DOMChildNode.getPreviousSibling() {
        return (Node)coreGetPreviousSibling();
    }
}
