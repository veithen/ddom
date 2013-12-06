/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
package com.googlecode.ddom.backend.linkedlist;

import com.googlecode.ddom.backend.linkedlist.intf.LLChildNode;
import com.googlecode.ddom.backend.linkedlist.intf.LLDocument;
import com.googlecode.ddom.backend.linkedlist.intf.LLLeafNode;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.HierarchyException;
import com.googlecode.ddom.core.NodeMigrationException;
import com.googlecode.ddom.core.NodeMigrationPolicy;

public abstract class LeafNode extends Node implements LLLeafNode {
    private LLParentNode owner;
    private LLChildNode nextSibling;
    
    public LeafNode(Document document) {
        owner = document;
    }

    public final LLParentNode internalGetOwner() {
        return owner;
    }

    public final void internalSetOwner(LLParentNode owner) {
        this.owner = owner;
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
    
    public final CoreChildNode coreGetNextSibling() throws DeferredBuildingException {
        return internalGetNextSibling();
    }
    
    public final CoreChildNode coreGetPreviousSibling() {
        return internalGetPreviousSibling();
    }
    
    public final LLDocument internalGetOwnerDocument(boolean create) {
        return LLChildNodeHelper.internalGetOwnerDocument(this, create);
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
    
    public final LLChildNode internalGetNextSibling() throws DeferredBuildingException {
        return LLChildNodeHelper.internalGetNextSibling(this);
    }

    public final LLChildNode internalGetPreviousSibling() {
        return LLChildNodeHelper.internalGetPreviousSibling(this);
    }

    public final void coreInsertSiblingAfter(CoreChildNode sibling, NodeMigrationPolicy policy) throws HierarchyException, NodeMigrationException, DeferredParsingException {
        LLChildNodeHelper.coreInsertSiblingAfter(this, sibling, policy);
    }

    public final void coreInsertSiblingsAfter(CoreDocumentFragment fragment) throws HierarchyException, NodeMigrationException, DeferredParsingException {
        LLChildNodeHelper.coreInsertSiblingsAfter(this, fragment);
    }

    public final void coreInsertSiblingBefore(CoreChildNode sibling, NodeMigrationPolicy policy) throws HierarchyException, NodeMigrationException, DeferredParsingException {
        LLChildNodeHelper.coreInsertSiblingBefore(this, sibling, policy);
    }

    public final void coreInsertSiblingsBefore(CoreDocumentFragment fragment) throws HierarchyException, NodeMigrationException, DeferredBuildingException {
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
