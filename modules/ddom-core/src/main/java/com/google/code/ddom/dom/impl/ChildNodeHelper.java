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

import com.google.code.ddom.spi.model.BuilderTarget;
import com.google.code.ddom.spi.model.CoreChildNode;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreParentNode;

public class ChildNodeHelper {
    public static CoreChildNode coreGetNextSibling(CoreChildNode node) {
        CoreParentNode parent = node.coreGetParent();
        CoreDocument document = node.getDocument();
        if (parent instanceof BuilderTarget) {
            CoreChildNode nextSibling;
            while ((nextSibling = node.internalGetNextSibling()) == null && !((BuilderTarget)parent).isComplete()) {
                document.next();
            }
            return nextSibling;
        } else {
            return node.internalGetNextSibling();
        }
    }
    
    public static CoreChildNode coreGetPreviousSibling(CoreChildNode node) {
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
}