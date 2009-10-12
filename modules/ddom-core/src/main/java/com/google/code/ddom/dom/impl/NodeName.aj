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

import org.w3c.dom.*;

import com.google.code.ddom.core.model.*;

/**
 * Aspect implementing {@link Node#getNodeName()}.
 * 
 * @author Andreas Veithen
 */
public aspect NodeName {
    public final String AttributeImpl.getNodeName() {
        return ((Attr)this).getName(); // TODO
    }

    public final String CDATASectionImpl.getNodeName() {
        return "#cdata-section";
    }

    public final String CommentImpl.getNodeName() {
        return "#comment";
    }

    public final String DocumentFragmentImpl.getNodeName() {
        return "#document-fragment";
    }

    public final String DocumentImpl.getNodeName() {
        return "#document";
    }

    public final String DocumentTypeImpl.getNodeName() {
        return ((DocumentType)this).getName(); // TODO
    }

    public final String ElementImpl.getNodeName() {
        return ((Element)this).getTagName(); // TODO
    }

    public final String EntityReferenceImpl.getNodeName() {
        return coreGetName();
    }

    public final String ProcessingInstructionImpl.getNodeName() {
        return getTarget();
    }

    public final String TextImpl.getNodeName() {
        return "#text";
    }
}
