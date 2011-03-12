/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.backend.linkedlist;

import com.googlecode.ddom.backend.Implementation;
import com.googlecode.ddom.core.CoreEntityReference;
import com.googlecode.ddom.stream.XmlHandler;

// @Implementation
public class EntityReference extends LeafNode implements CoreEntityReference {
    private String name;
    
    public EntityReference(Document document, String name) {
        super(document);
        this.name = name;
    }

    public final int coreGetNodeType() {
        return ENTITY_REFERENCE_NODE;
    }

    public final String coreGetName() {
        return name;
    }

    public final void internalGenerateEvents(XmlHandler handler) {
        // TODO
    }
}