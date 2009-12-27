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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreLeafNode;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreNode;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.Implementation;

@Implementation
public abstract class LeafNode extends Node implements CoreLeafNode {
    private CoreDocument document;
    private CoreParentNode parent;
    private CoreChildNode nextSibling;
    
    public LeafNode(CoreDocument document) {
        this.document = document;
    }

    public final void internalSetParent(CoreParentNode parent) {
        this.parent = parent;
    }
    
    public final void internalSetDocument(CoreDocument document) {
        this.document = document;
    }
    
    public final CoreChildNode internalGetNextSibling() {
        return nextSibling;
    }

    public final void internalSetNextSibling(CoreChildNode nextSibling) {
        this.nextSibling = nextSibling;
    }
    
    public final CoreDocument getDocument() {
        return document;
    }

    public final CoreParentNode coreGetParent() {
        return parent;
    }
    
    public final CoreChildNode coreGetNextSibling() {
        return ChildNodeHelper.coreGetNextSibling(this);
    }

    public final CoreChildNode coreGetPreviousSibling() {
        return ChildNodeHelper.coreGetPreviousSibling(this);
    }

    public void coreInsertSiblingAfter(CoreNode sibling) throws CoreModelException {
        ChildNodeHelper.coreInsertSiblingAfter(this, sibling);
    }

    public void coreInsertSiblingBefore(CoreNode sibling) throws CoreModelException {
        ChildNodeHelper.coreInsertSiblingBefore(this, sibling);
    }
    
    public void coreDetach() {
        ChildNodeHelper.coreDetach(this);
    }
}
