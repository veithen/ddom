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
package com.google.code.ddom.frontend.dom.aspects;

import org.w3c.dom.Node;

import com.google.code.ddom.frontend.dom.intf.*;

/**
 * Aspect implementing {@link Node#getNodeType()}.
 * 
 * @author Andreas Veithen
 */
public aspect NodeType {
    public final short DOMAttribute.getNodeType() {
        return Node.ATTRIBUTE_NODE;
    }

    public final short DOMCDATASection.getNodeType() {
        return Node.CDATA_SECTION_NODE;
    }

    public final short DOMComment.getNodeType() {
        return Node.COMMENT_NODE;
    }

    public final short DOMDocumentFragment.getNodeType() {
        return Node.DOCUMENT_FRAGMENT_NODE;
    }

    public final short DOMDocument.getNodeType() {
        return Node.DOCUMENT_NODE;
    }

    public final short DOMDocumentType.getNodeType() {
        return Node.DOCUMENT_TYPE_NODE;
    }

    public final short DOMElement.getNodeType() {
        return Node.ELEMENT_NODE;
    }

    public final short DOMEntityReference.getNodeType() {
        return Node.ENTITY_REFERENCE_NODE;
    }

    public final short DOMProcessingInstruction.getNodeType() {
        return Node.PROCESSING_INSTRUCTION_NODE;
    }

    public final short DOMText.getNodeType() {
        return Node.TEXT_NODE;
    }
}
