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
