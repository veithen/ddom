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

import com.google.code.ddom.backend.BuilderTarget;
import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreNode;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.DeferredParsingException;
import com.google.code.ddom.backend.NoParentException;

public final class ChildNodeHelper {
    private ChildNodeHelper() {}
    
    public static CoreChildNode coreGetNextSibling(CoreChildNode node) throws DeferredParsingException {
        CoreParentNode parent = node.coreGetParent();
        // TODO: try to avoid the cast here
        Document document = (Document)node.getDocument();
        if (parent instanceof BuilderTarget) {
            CoreChildNode nextSibling;
            while ((nextSibling = node.internalGetNextSibling()) == null && !((BuilderTarget)parent).coreIsComplete()) {
                document.next();
            }
            return nextSibling;
        } else {
            return node.internalGetNextSibling();
        }
    }
    
    public static CoreChildNode coreGetPreviousSibling(CoreChildNode node) throws DeferredParsingException {
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
    
    public static void coreInsertSiblingAfter(CoreChildNode node, CoreNode sibling) throws CoreModelException {
        CoreParentNode parent = node.coreGetParent();
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.coreInsertChildAfter(sibling, node);
        }
    }
    
    public static void coreInsertSiblingBefore(CoreChildNode node, CoreNode sibling) throws CoreModelException {
        CoreParentNode parent = node.coreGetParent();
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.coreInsertChildBefore(sibling, node);
        }
    }
    
    public static void coreDetach(CoreChildNode node) throws DeferredParsingException {
        CoreParentNode parent = node.coreGetParent();
        if (parent != null) {
            CoreChildNode previousSibling = node.coreGetPreviousSibling();
            // We have a builder of type 2; thus we don't need to build
            // the node being detached. Therefore we can use internalGetNextSibling
            // instead of coreGetNextSibling.
            CoreChildNode nextSibling = node.internalGetNextSibling();
            node.internalSetParent(null);
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
