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

import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.core.CoreEntityReference;
import com.google.code.ddom.core.DeferredParsingException;
import com.google.code.ddom.stream.spi.XmlHandler;

// @Implementation
public class EntityReference extends LeafNode implements CoreEntityReference {
    private String name;
    
    public EntityReference(Document document, String name) {
        super(document);
        this.name = name;
    }

    public final String coreGetName() {
        return name;
    }

    @Override
    final CharSequence internalCollectTextContent(CharSequence appendTo) throws DeferredParsingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void internalGenerateEvents(XmlHandler handler) {
        // TODO
    }
}
