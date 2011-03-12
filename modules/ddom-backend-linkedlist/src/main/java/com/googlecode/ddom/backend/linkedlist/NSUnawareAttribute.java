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
import com.googlecode.ddom.core.CoreNSUnawareAttribute;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

// @Implementation
public class NSUnawareAttribute extends TypedAttribute implements CoreNSUnawareAttribute {
    private final String name;

    public NSUnawareAttribute(Document document, String name, String value, String type) {
        super(document, value, type);
        this.name = name;
    }

    public NSUnawareAttribute(Document document, String name, String type, boolean complete) {
        super(document, type, complete);
        this.name = name;
    }

    public final int coreGetNodeType() {
        return NS_UNAWARE_ATTRIBUTE_NODE;
    }

    public final String coreGetName() {
        return name;
    }

    public final void internalGenerateStartEvent(XmlHandler handler) throws StreamException {
        handler.startAttribute(name, coreGetType());
    }
}
