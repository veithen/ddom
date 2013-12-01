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

import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;

import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlReader;

public class DOMInput extends XmlInput {
    private final Node rootNode;
    private final boolean expandEntityReferences;
    
    // TODO: should disappear
    public DOMInput(Node rootNode) {
        this(rootNode, false);
    }

    /**
     * Constructor.
     * 
     * @param rootNode
     *            The root node of the tree from which events will be generated.
     * @param expandEntityReferences
     *            Determines how {@link EntityReference} nodes are handled by this instance. When
     *            set to <code>false</code>, a single
     *            {@link XmlHandler#processEntityReference(String)} event will be emitted for each
     *            {@link EntityReference}. When set to <code>true</code>, no
     *            {@link XmlHandler#processEntityReference(String)} events are generated. Instead,
     *            the implementation will traverse the descendants of the {@link EntityReference}
     *            nodes (which effectively expands these entity references).
     */
    public DOMInput(Node rootNode, boolean expandEntityReferences) {
        this.rootNode = rootNode;
        this.expandEntityReferences = expandEntityReferences;
    }

    @Override
    protected XmlReader createReader(XmlHandler handler) {
        return new DOMReader(handler, rootNode, expandEntityReferences);
    }

    public void dispose() {
        // TODO Auto-generated method stub
        
    }
}
