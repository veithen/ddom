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

import com.googlecode.ddom.core.CoreTypedAttribute;

public abstract class TypedAttribute extends Attribute implements CoreTypedAttribute {
    private String type;

    public TypedAttribute(Document document, String value, String type) {
        super(document, value);
        this.type = type;
    }

    public TypedAttribute(Document document, String type, boolean complete) {
        super(document, complete);
        this.type = type;
    }

    public final String coreGetType() {
        return type;
    }

    public final void coreSetType(String type) {
        this.type = type;
    }
}