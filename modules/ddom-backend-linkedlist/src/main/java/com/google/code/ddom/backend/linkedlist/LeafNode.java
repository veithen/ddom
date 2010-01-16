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

import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreLeafNode;
import com.google.code.ddom.backend.Implementation;

@Implementation
public abstract class LeafNode extends Node implements ChildNode, CoreLeafNode {
    private Document document;
    
    public LeafNode(Document document) {
        this.document = document;
    }

    public final void internalSetDocument(CoreDocument document) {
        this.document = (Document)document; // TODO: get rid of cast
    }
    
    public final Document internalGetDocument() {
        return document;
    }
}
