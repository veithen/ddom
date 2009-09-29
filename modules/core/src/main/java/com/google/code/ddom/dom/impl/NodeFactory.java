package com.google.code.ddom.dom.impl;

public class NodeFactory {
    public DOM1ElementImpl createElement(DocumentImpl document, String tagName, boolean complete) {
        return new DOM1ElementImpl(document, tagName, complete);
    }
    
    public DOM2ElementImpl createElement(DocumentImpl document, String namespaceURI, String localName, String prefix, boolean complete) {
        return new DOM2ElementImpl(document, namespaceURI, localName, prefix, complete);
    }
    
    public DOM1AttrImpl createAttribute(DocumentImpl document, String name, String value, String type) {
        return new DOM1AttrImpl(document, name, value, type);
    }
    
    public DOM2AttrImpl createAttribute(DocumentImpl document, String namespaceURI, String localName, String prefix, String value, String type) {
        return new DOM2AttrImpl(document, namespaceURI, localName, prefix, value, type);
    }
    
    public NSDecl createNSDecl(DocumentImpl document, String prefix, String namespaceURI) {
        return new NSDecl(document, prefix, namespaceURI);
    }
    
    public ProcessingInstructionImpl createProcessingInstruction(DocumentImpl document, String target, String data) {
        return new ProcessingInstructionImpl(document, target, data);
    }
    
    public DocumentFragmentImpl createDocumentFragment(DocumentImpl document) {
        return new DocumentFragmentImpl(document);
    }

    public TextImpl createText(DocumentImpl document, String data) {
        return new TextImpl(document, data);
    }

    public CommentImpl createComment(DocumentImpl document, String data) {
        return new CommentImpl(document, data);
    }

    public CDATASectionImpl createCDATASection(DocumentImpl document, String data) {
        return new CDATASectionImpl(document, data);
    }

    public EntityReferenceImpl createEntityReference(DocumentImpl document, String name) {
        return new EntityReferenceImpl(document, name);
    }
}
