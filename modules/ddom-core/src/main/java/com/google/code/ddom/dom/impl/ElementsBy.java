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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.code.ddom.spi.model.CoreDocument;

public abstract class ElementsBy implements NodeList {
    private final CoreDocument document;
    private Iterator<? extends Node> iterator;
    private final List<Node> nodes = new ArrayList<Node>();
    private boolean complete;
    private int structureVersion;
    
    public ElementsBy(CoreDocument document) {
        this.document = document;
    }
    
    protected abstract Iterator<? extends Node> createIterator();

    private void init() {
        int currentStructureVersion = document.getStructureVersion();
        if (iterator == null && !complete || structureVersion != currentStructureVersion) {
            iterator = createIterator();
            nodes.clear();
            complete = false;
            structureVersion = currentStructureVersion;
        }
    }
    
    public int getLength() {
        init();
        if (!complete) {
            while (iterator.hasNext()) {
                nodes.add(iterator.next());
            }
            iterator = null;
            complete = true;
        }
        return nodes.size();
    }

    public Node item(int index) {
        init();
        if (index < 0) {
            return null;
        } else if (index < nodes.size()) {
            return nodes.get(index);
        } else if (!complete) {
            Node node = null;
            while (index >= nodes.size()) {
                if (iterator.hasNext()) {
                    node = iterator.next();
                    nodes.add(node);
                } else {
                    iterator = null;
                    complete = true;
                    return null;
                }
            }
            return node;
        } else {
            return null;
        }
    }
}
