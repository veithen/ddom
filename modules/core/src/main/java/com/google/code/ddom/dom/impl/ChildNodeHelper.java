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

import com.google.code.ddom.dom.model.BuilderTarget;
import com.google.code.ddom.dom.model.ChildNode;
import com.google.code.ddom.dom.model.ParentNode;

public class ChildNodeHelper {
    public static ChildNode getNextSibling(ChildNode node) {
        ParentNode parent = node.getParentNode();
        DocumentImpl document = node.getDocument();
        if (parent instanceof BuilderTarget) {
            ChildNode nextSibling;
            while ((nextSibling = node.internalGetNextSibling()) == null && !((BuilderTarget)parent).isComplete()) {
                document.next();
            }
            return nextSibling;
        } else {
            return node.internalGetNextSibling();
        }
    }
    
    public static ChildNode getPreviousSibling(ChildNode node) {
        ParentNode parent = node.getParentNode();
        if (parent == null) {
            return null;
        } else {
            ChildNode previousSibling = null;
            ChildNode sibling = parent.getFirstChild();
            while (sibling != null && sibling != node) {
                previousSibling = sibling;
                sibling = sibling.internalGetNextSibling();
            }
            return sibling == null ? null : previousSibling;
        }
    }
}
