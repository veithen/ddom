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

import com.google.code.ddom.spi.model.CoreChildNode;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreOptimizedParentNode;

public class OptimizedParentNodeHelper {
    public static CoreChildNode getFirstChild(CoreOptimizedParentNode node) {
        Object content = node.getContent();
        CoreChildNode firstChild;
        if (content instanceof String) {
            CoreDocument document = node.getDocument();
            firstChild = document.getNodeFactory().createText(document, (String)content);
            // TODO: need to set parent
            node.internalSetFirstChild(firstChild);
        } else {
            firstChild = (CoreChildNode)content;
        }
        return firstChild;
    }
}