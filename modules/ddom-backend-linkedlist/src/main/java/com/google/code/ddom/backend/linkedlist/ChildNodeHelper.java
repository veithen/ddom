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
import com.google.code.ddom.backend.CoreDocumentFragment;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.DeferredParsingException;
import com.google.code.ddom.backend.NoParentException;
import com.google.code.ddom.backend.SelfRelationshipException;

public final class ChildNodeHelper {
    private ChildNodeHelper() {}
    
    public static CoreChildNode coreGetNextSibling(ChildNode node) throws DeferredParsingException {
        CoreParentNode parent = node.coreGetParent();
        if (parent == null) {
            return null;
        } else {
            // TODO: try to avoid the cast here
            Document document = ((Node)node).getDocument();
            CoreChildNode nextSibling = node.internalGetNextSibling();
            if (nextSibling == null && !parent.coreIsComplete()) {
                Builder builder = document.getBuilderFor(parent);
                do {
                    builder.next();
                    nextSibling = node.internalGetNextSibling();
                } while (nextSibling == null && !parent.coreIsComplete());
            }
            return nextSibling;
        }
    }
    
    public static CoreChildNode coreGetPreviousSibling(ChildNode node) throws DeferredParsingException {
        CoreParentNode parent = node.coreGetParent();
        if (parent == null) {
            return null;
        } else {
            CoreChildNode previousSibling = null;
            CoreChildNode sibling = parent.coreGetFirstChild();
            while (sibling != null && sibling != node) {
                previousSibling = sibling;
                sibling = sibling.internalGetNextSibling();
            }
            return sibling == null ? null : previousSibling;
        }
    }
    
    public static void coreInsertSiblingAfter(ChildNode node, CoreChildNode sibling_) throws CoreModelException {
        ChildNode sibling = (ChildNode)sibling_;
        if (sibling == node) {
            throw new SelfRelationshipException();
        }
        ParentNode parent = (ParentNode)node.coreGetParent();
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.prepareNewChild(sibling);
            sibling.coreDetach();
            sibling.internalSetNextSibling(node.coreGetNextSibling());
            node.internalSetNextSibling(sibling);
            sibling.setParent(parent);
            parent.notifyChildrenModified(1);
//            parent.coreInsertChildAfter(sibling, node);
        }
    }
    
    public static void coreInsertSiblingsAfter(ChildNode node, CoreDocumentFragment fragment) throws CoreModelException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public static void coreInsertSiblingBefore(ChildNode node, CoreChildNode sibling) throws CoreModelException {
        CoreParentNode parent = node.coreGetParent();
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.coreInsertChildBefore(sibling, node);
        }
    }
    
    public static void coreInsertSiblingsBefore(ChildNode node, CoreDocumentFragment fragment) throws CoreModelException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public static void coreDetach(ChildNode node) throws DeferredParsingException {
        CoreParentNode parent = node.coreGetParent();
        if (parent != null) {
            CoreChildNode previousSibling = node.coreGetPreviousSibling();
            // We have a builder of type 2; thus we don't need to build
            // the node being detached. Therefore we can use internalGetNextSibling
            // instead of coreGetNextSibling.
            CoreChildNode nextSibling = node.internalGetNextSibling();
            node.setParent(null);
            node.internalSetNextSibling(null);
            if (previousSibling == null) {
                parent.internalSetFirstChild(nextSibling);
            } else {
                previousSibling.internalSetNextSibling(nextSibling);
            }
            parent.notifyChildrenModified(-1);
        }
    }
}
