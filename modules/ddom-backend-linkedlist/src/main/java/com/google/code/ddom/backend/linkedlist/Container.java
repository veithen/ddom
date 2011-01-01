/*
 * Copyright 2009-2011 Andreas Veithen
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

import com.google.code.ddom.backend.linkedlist.intf.LLChildNode;
import com.google.code.ddom.backend.linkedlist.intf.LLDocument;
import com.google.code.ddom.backend.linkedlist.intf.LLParentNode;
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreDocumentFragment;
import com.google.code.ddom.core.CoreElement;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreParentNode;
import com.google.code.ddom.core.DeferredParsingException;
import com.google.code.ddom.core.HierarchyException;
import com.google.code.ddom.core.NodeMigrationException;

public abstract class Container extends ParentNode implements LLChildNode {
    private LLParentNode owner;
    private LLChildNode nextSibling;
    private int children;

    public Container(Document document, Object content) {
        super(content);
        owner = document;
    }

    public Container(Document document, boolean complete) {
        super(complete);
        owner = document;
    }
    
    public final LLParentNode internalGetOwner() {
        return owner;
    }

    public final void internalSetOwner(LLParentNode owner) {
        this.owner = owner;
    }

    public final void internalNotifyChildrenModified(int delta) {
        children += delta;
    }

    public final void internalNotifyChildrenCleared() {
        children = 0;
    }

    public final int coreGetChildCount() throws DeferredParsingException {
        coreBuild();
        return children;
    }

    public final CoreParentNode coreGetParent() {
        return internalGetParent();
    }

    public final boolean coreHasParent() {
        return internalGetFlag(Flags.HAS_PARENT);
    }

    public final CoreElement coreGetParentElement() {
        LLParentNode parent = internalGetParent();
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
    
    public final LLDocument internalGetOwnerDocument() {
        return LLChildNodeHelper.internalGetOwnerDocument(this);
    }

    public final LLParentNode internalGetParent() {
        return LLChildNodeHelper.internalGetParent(this);
    }
    
    public final void internalSetParent(LLParentNode parent) {
        LLChildNodeHelper.internalSetParent(this, parent);
    }
    
    public final void internalUnsetParent(LLDocument newOwnerDocument) {
        LLChildNodeHelper.internalUnsetParent(this, newOwnerDocument);
    }
    
    public final LLChildNode internalGetNextSibling() throws DeferredParsingException {
        return LLChildNodeHelper.internalGetNextSibling(this);
    }

    public final LLChildNode internalGetPreviousSibling() {
        return LLChildNodeHelper.internalGetPreviousSibling(this);
    }

    public final void coreInsertSiblingAfter(CoreChildNode sibling) throws HierarchyException, NodeMigrationException, DeferredParsingException {
        LLChildNodeHelper.coreInsertSiblingAfter(this, sibling);
    }

    public final void coreInsertSiblingsAfter(CoreDocumentFragment fragment) throws HierarchyException, NodeMigrationException, DeferredParsingException {
        LLChildNodeHelper.coreInsertSiblingsAfter(this, fragment);
    }

    public final void coreInsertSiblingBefore(CoreChildNode sibling) throws HierarchyException, NodeMigrationException, DeferredParsingException {
        LLChildNodeHelper.coreInsertSiblingBefore(this, sibling);
    }

    public final void coreInsertSiblingsBefore(CoreDocumentFragment fragment) throws HierarchyException, NodeMigrationException, DeferredParsingException {
        LLChildNodeHelper.coreInsertSiblingsBefore(this, fragment);
    }

    public final void coreDetach() throws DeferredParsingException {
        LLChildNodeHelper.coreDetach(this);
    }

    public final void coreDetach(CoreDocument document) throws DeferredParsingException {
        LLChildNodeHelper.coreDetach(this, document);
    }

    public final void internalDetach() {
        LLChildNodeHelper.internalDetach(this);
    }

    public final void coreReplaceWith(CoreChildNode newNode) throws CoreModelException {
        LLChildNodeHelper.coreReplaceWith(this, newNode);
    }

    public final void coreReplaceWith(CoreDocumentFragment newNodes) throws CoreModelException {
        LLChildNodeHelper.coreReplaceWith(this, newNodes);
    }
}
