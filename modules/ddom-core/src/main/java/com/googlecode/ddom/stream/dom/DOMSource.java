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
package com.googlecode.ddom.stream.dom;

import org.w3c.dom.Node;

import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;

public final class DOMSource implements XmlSource {
    private final Node node;
    private final boolean expandEntityReferences;

    // TODO: should disappear
    public DOMSource(Node node) {
        this(node, false);
    }
    
    public DOMSource(Node node, boolean expandEntityReferences) {
        this.node = node;
        this.expandEntityReferences = expandEntityReferences;
    }

    public XmlInput getInput(Hints hints) {
        return new DOMInput(node, expandEntityReferences);
    }

    public boolean isDestructive() {
        return false;
    }
}
