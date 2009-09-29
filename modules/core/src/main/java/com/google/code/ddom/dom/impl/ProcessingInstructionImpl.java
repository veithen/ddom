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

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public class ProcessingInstructionImpl extends LeafNode implements ProcessingInstruction {
    private final String target;
    private String data;

    public ProcessingInstructionImpl(DocumentImpl document, String target, String data) {
        super(document);
        this.target = target;
        this.data = data;
    }

    public final short getNodeType() {
        return PROCESSING_INSTRUCTION_NODE;
    }

    public final String getNodeName() {
        return getTarget();
    }

    public final String getNodeValue() throws DOMException {
        return getData();
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        setData(nodeValue);
    }

    public final String getData() {
        return data;
    }

    public final void setData(String data) throws DOMException {
        this.data = data;
    }

    public final String getTarget() {
        return target;
    }

    public final CharSequence collectTextContent(CharSequence appendTo) {
        return appendTo;
    }

    public final Node cloneNode(boolean deep) {
        DocumentImpl document = getDocument();
        return document.getNodeFactory().createProcessingInstruction(document, target, data);
    }
}
