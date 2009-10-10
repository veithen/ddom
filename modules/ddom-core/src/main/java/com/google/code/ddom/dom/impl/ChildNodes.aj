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
import org.w3c.dom.NodeList;

import com.google.code.ddom.spi.model.CoreChildNode;

public aspect ChildNodes {
    declare parents: ParentNodeImpl implements NodeList;

    public final boolean ParentNodeImpl.hasChildNodes() {
        // TODO: not the best way if content is optimized
        return getFirstChild() != null;
    }
    
    public final NodeList ParentNodeImpl.getChildNodes() {
        return (NodeList)this; // TODO: cast necessary??
    }
    
    public final int ParentNodeImpl.getLength() {
        return coreGetChildCount();
    }
    
    public final Node ParentNodeImpl.item(int index) {
        // TODO: need unit test to check that this works when parsing is deferred
        // TODO: wrong result for negavite indexes
        CoreChildNode node = getFirstChild();
        for (int i=0; i<index && node != null; i++) {
            node = node.getNextSibling();
        }
        return node;
    }
}
