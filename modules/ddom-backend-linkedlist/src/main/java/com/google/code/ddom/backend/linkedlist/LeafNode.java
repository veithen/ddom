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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocumentFragment;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreLeafNode;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.DeferredParsingException;

public abstract class LeafNode extends Node implements LLChildNode, CoreLeafNode {
    private final Document document;
    private LLParentNode parent;
    private LLChildNode nextSibling;
    
    public LeafNode(Document document) {
        this.document = document;
    }

    public final LLParentNode internalGetParent() {
        return parent;
    }
    
    public final void internalSetParent(LLParentNode parent) {
        this.parent = parent;
    }
    
    public final CoreParentNode coreGetParent() {
        return parent;
    }

    public final boolean coreHasParent() {
        return parent != null;
    }

    public final CoreElement coreGetParentElement() {
        return parent instanceof CoreElement ? (CoreElement)parent : null;
    }

    public final LLChildNode internalGetNextSiblingIfMaterialized() {
        return nextSibling;
    }

    public final void internalSetNextSibling(LLChildNode nextSibling) {
        this.nextSibling = nextSibling;
    }
    
    public final CoreChildNode coreGetNextSibling() throws DeferredParsingException {
        return internalGetNextSibling();
    }
    
    public final CoreChildNode coreGetPreviousSibling() {
        return internalGetPreviousSibling();
    }
    
    public final LLDocument internalGetDocument() {
        return document;
    }

    public final LLChildNode internalGetNextSibling() throws DeferredParsingException {
        return LLChildNodeHelper.internalGetNextSibling(this);
    }

    public final LLChildNode internalGetPreviousSibling() {
        return LLChildNodeHelper.internalGetPreviousSibling(this);
    }

    public final void coreInsertSiblingAfter(CoreChildNode sibling) throws CoreModelException {
        LLChildNodeHelper.coreInsertSiblingAfter(this, sibling);
    }

    public final void coreInsertSiblingsAfter(CoreDocumentFragment fragment) throws CoreModelException {
        LLChildNodeHelper.coreInsertSiblingsAfter(this, fragment);
    }

    public final void coreInsertSiblingBefore(CoreChildNode sibling) throws CoreModelException {
        LLChildNodeHelper.coreInsertSiblingBefore(this, sibling);
    }

    public final void coreInsertSiblingsBefore(CoreDocumentFragment fragment) throws CoreModelException {
        LLChildNodeHelper.coreInsertSiblingsBefore(this, fragment);
    }

    public final void coreDetach() throws DeferredParsingException {
        LLChildNodeHelper.coreDetach(this);
    }

    public final void coreReplaceWith(CoreChildNode newNode) throws CoreModelException {
        LLChildNodeHelper.coreReplaceWith(this, newNode);
    }

    public final void coreReplaceWith(CoreDocumentFragment newNodes) throws CoreModelException {
        LLChildNodeHelper.coreReplaceWith(this, newNodes);
    }
}
