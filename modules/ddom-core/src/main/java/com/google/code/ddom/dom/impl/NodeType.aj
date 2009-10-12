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

import com.google.code.ddom.core.model.*;

/**
 * Aspect implementing {@link Node#getNodeType()}.
 * 
 * @author Andreas Veithen
 */
public aspect NodeType {
    public final short AttributeImpl.getNodeType() {
        return Node.ATTRIBUTE_NODE;
    }

    public final short CDATASectionImpl.getNodeType() {
        return Node.CDATA_SECTION_NODE;
    }

    public final short CommentImpl.getNodeType() {
        return Node.COMMENT_NODE;
    }

    public final short DocumentFragmentImpl.getNodeType() {
        return Node.DOCUMENT_FRAGMENT_NODE;
    }

    public final short DocumentImpl.getNodeType() {
        return Node.DOCUMENT_NODE;
    }

    public final short DocumentTypeImpl.getNodeType() {
        return Node.DOCUMENT_TYPE_NODE;
    }

    public final short ElementImpl.getNodeType() {
        return Node.ELEMENT_NODE;
    }

    public final short EntityReferenceImpl.getNodeType() {
        return Node.ENTITY_REFERENCE_NODE;
    }

    public final short ProcessingInstructionImpl.getNodeType() {
        return Node.PROCESSING_INSTRUCTION_NODE;
    }

    public final short TextImpl.getNodeType() {
        return Node.TEXT_NODE;
    }
}
