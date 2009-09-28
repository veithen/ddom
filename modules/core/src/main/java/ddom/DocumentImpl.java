package ddom;

import java.util.Iterator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.stax2.DTDInfo;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import ddom.utils.dom.iterator.DescendantsIterator;

// TODO: need some unit test (or AspectJ) to check that all nodes are created using a NodeFactory
public class DocumentImpl extends ParentNodeImpl implements Document, BuilderTarget {
    private final NodeFactory nodeFactory = new NodeFactory();
    private final XMLStreamReader reader;
    private ChildNode firstChild;
    private boolean complete;
    private int children;
    private String inputEncoding;
    private String xmlEncoding;
    private String documentURI;
    private BuilderTarget parent;
    private ChildNode lastSibling;

    public DocumentImpl(XMLStreamReader reader) {
        this.reader = reader;
        parent = this;
        complete = reader == null;
    }

    private String emptyToNull(String value) {
        return value == null || value.length() == 0 ? null : value;
    }
    
    public final boolean next() {
        // TODO: need to take into account namespace unaware parsing
        try {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    ElementImpl element = nodeFactory.createElement(this, emptyToNull(reader.getNamespaceURI()), reader.getLocalName(), emptyToNull(reader.getPrefix()), false);
                    appendNode(element);
                    parent = element;
                    lastSibling = null;
                    AbstractAttrImpl firstAttr = null;
                    AbstractAttrImpl previousAttr = null;
                    for (int i=0; i<reader.getAttributeCount(); i++) {
                        AttrImpl attr = nodeFactory.createAttribute(this, emptyToNull(reader.getAttributeNamespace(i)), reader.getAttributeLocalName(i), emptyToNull(reader.getAttributePrefix(i)), reader.getAttributeValue(i), reader.getAttributeType(i));
                        if (firstAttr == null) {
                            firstAttr = attr;
                        } else {
                            previousAttr.internalSetNextAttribute(attr);
                        }
                        previousAttr = attr;
                        attr.internalSetOwnerElement(element);
                    }
                    for (int i=0; i<reader.getNamespaceCount(); i++) {
                        NSDecl attr = nodeFactory.createNSDecl(this, emptyToNull(reader.getNamespacePrefix(i)), reader.getNamespaceURI(i));
                        if (firstAttr == null) {
                            firstAttr = attr;
                        } else {
                            previousAttr.internalSetNextAttribute(attr);
                        }
                        previousAttr = attr;
                        attr.internalSetOwnerElement(element);
                    }
                    if (firstAttr != null) {
                        element.internalSetFirstAttribute(firstAttr);
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    lastSibling = (ChildNode)parent;
                    // Fall through
                case XMLStreamReader.END_DOCUMENT:
                    parent.internalSetComplete();
                    // TODO: get rid of cast here
                    parent = (BuilderTarget)parent.getParentNode();
                    break;
                case XMLStreamReader.PROCESSING_INSTRUCTION:
                    appendNode(nodeFactory.createProcessingInstruction(this, reader.getPITarget(), reader.getPIData()));
                    break;
                case XMLStreamReader.DTD:
                    // TODO: this should use a factory method
                    DocumentTypeImpl docType = new DocumentTypeImpl(this);
                    appendNode(docType);
                    if (reader instanceof DTDInfo) {
                        DTDInfo dtdInfo = (DTDInfo)reader;
                        docType.setName(dtdInfo.getDTDRootName());
                        docType.setPublicId(dtdInfo.getDTDPublicId());
                        docType.setSystemId(dtdInfo.getDTDSystemId());
                    }
                    break;
                case XMLStreamReader.COMMENT:
                    appendNode(nodeFactory.createComment(this, reader.getText()));
                    break;
                case XMLStreamReader.SPACE:
                case XMLStreamReader.CHARACTERS:
                    // TODO: optimize if possible
                    appendNode(nodeFactory.createText(this, reader.getText()));
                    break;
                case XMLStreamReader.CDATA:
                    appendNode(nodeFactory.createCDATASection(this, reader.getText()));
                    break;
                case XMLStreamReader.ENTITY_REFERENCE:
                    appendNode(nodeFactory.createEntityReference(this, reader.getText()));
                    break;
                default:
                    throw new RuntimeException("Unexpected event " + reader.getEventType()); // TODO
            }
            return reader.hasNext();
        } catch (XMLStreamException ex) {
            throw new DOMException(DOMException.INVALID_STATE_ERR, ""); // TODO
        }
    }
    
    private void appendNode(ChildNode node) {
        if (lastSibling == null) {
            parent.internalSetFirstChild(node);
        } else {
            lastSibling.internalSetNextSibling(node);
        }
        parent.notifyChildrenModified(1);
        node.internalSetParent(parent);
        lastSibling = node;
    }
    
    public NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    public final DOMImplementation getImplementation() {
        return DOMImplementationImpl.INSTANCE;
    }

    public final Element createElement(String tagName) throws DOMException {
        NSUtil.validateName(tagName);
        return nodeFactory.createElement(this, tagName, true);
    }
    
    public final Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = null;
            localName = qualifiedName;
        } else {
            prefix = qualifiedName.substring(0, i);
            localName = qualifiedName.substring(i+1);
        }
        NSUtil.validateNamespace(namespaceURI, prefix);
        return nodeFactory.createElement(this, namespaceURI, localName, prefix, true);
    }
    
    public final Attr createAttribute(String name) throws DOMException {
        NSUtil.validateName(name);
        return nodeFactory.createAttribute(this, name, null, null);
    }

    public final Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = null;
            localName = qualifiedName;
        } else {
            prefix = qualifiedName.substring(0, i);
            localName = qualifiedName.substring(i+1);
        }
        NSUtil.validateAttributeName(namespaceURI, localName, prefix);
        // TODO: this might also be an NSDecl
        return nodeFactory.createAttribute(this, namespaceURI, localName, prefix, null, null);
    }

    public final ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        NSUtil.validateName(target);
        return nodeFactory.createProcessingInstruction(this, target, data);
    }
    
    public final DocumentFragment createDocumentFragment() {
        return nodeFactory.createDocumentFragment(this);
    }

    public final Text createTextNode(String data) {
        return nodeFactory.createText(this, data);
    }

    public final Comment createComment(String data) {
        return nodeFactory.createComment(this, data);
    }

    public final CDATASection createCDATASection(String data) throws DOMException {
        return nodeFactory.createCDATASection(this, data);
    }

    public final EntityReference createEntityReference(String name) throws DOMException {
        return nodeFactory.createEntityReference(this, name);
    }

    public final DocumentImpl getDocument() {
        return this;
    }

    public final Document getOwnerDocument() {
        return null;
    }

    public final boolean isComplete() {
        return complete;
    }
    
    public final void build() {
        BuilderTargetHelper.build(this);
    }
    
    public final void internalSetComplete() {
        complete = true;
    }
    
    public final void internalSetFirstChild(ChildNode child) {
        firstChild = child;
    }

    public final ChildNode getFirstChild() {
        if (firstChild == null && !complete) {
            next();
        }
        return firstChild;
    }

    public final void notifyChildrenModified(int delta) {
        children += delta;
    }

    @Override
    protected void validateChildType(ChildNode newChild) {
        // TODO
    }

    public final int getLength() {
        build();
        return children;
    }

    public final short getNodeType() {
        return DOCUMENT_NODE;
    }

    public final String getNodeName() {
        return "#document";
    }

    public final String getNodeValue() throws DOMException {
        return null;
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String getInputEncoding() {
        return inputEncoding;
    }

    public final void setInputEncoding(String inputEncoding) {
        this.inputEncoding = inputEncoding;
    }

    public final String getXmlEncoding() {
        return xmlEncoding;
    }

    public final void setXmlEncoding(String xmlEncoding) {
        this.xmlEncoding = xmlEncoding;
    }

    // TODO: need test for this
    public final String getDocumentURI() {
        return documentURI;
    }

    public final void setDocumentURI(String documentURI) {
        this.documentURI = documentURI;
    }

    public final int getStructureVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    public final Node getParentNode() {
        return null;
    }

    public final Node getNextSibling() {
        return null;
    }

    public final Node getPreviousSibling() {
        return null;
    }

    public final Element getDocumentElement() {
        Node child = getFirstChild();
        while (child != null && !(child instanceof Element)) {
            child = child.getNextSibling();
        }
        return (Element)child;
    }

    public final DocumentType getDoctype() {
        Node child = getFirstChild();
        while (child != null && !(child instanceof DocumentType)) {
            child = child.getNextSibling();
        }
        return (DocumentType)child;
    }

    public final boolean hasAttributes() {
        return false;
    }

    public final NamedNodeMap getAttributes() {
        return null;
    }

    public final String getNamespaceURI() {
        return null;
    }

    public final String getPrefix() {
        return null;
    }

    public final void setPrefix(String prefix) throws DOMException {
        // Ignored
    }

    public final String getLocalName() {
        return null;
    }
    
    public final Node importNode(Node node, boolean deep) throws DOMException {
        Node importedNode;
        boolean importChildren;
        switch (node.getNodeType()) {
            case ELEMENT_NODE:
                Element element = (Element)node;
                // TODO: detect DOM 1 elements (as with attributes)
                importedNode = nodeFactory.createElement(this, element.getNamespaceURI(), element.getLocalName(), element.getPrefix(), true);
                importChildren = deep;
                break;
            case ATTRIBUTE_NODE:
                Attr attr = (Attr)node;
                String localName = attr.getLocalName();
                if (localName == null) {
                    importedNode = nodeFactory.createAttribute(this, attr.getName(), null, null);
                } else {
                    importedNode = nodeFactory.createAttribute(this, attr.getNamespaceURI(), localName, attr.getPrefix(), null, null);
                }
                importChildren = true;
                break;
            case COMMENT_NODE:
                importedNode = nodeFactory.createComment(this, node.getNodeValue());
                importChildren = false;
                break;
            case TEXT_NODE:
                importedNode = nodeFactory.createText(this, node.getNodeValue());
                importChildren = false;
                break;
            case CDATA_SECTION_NODE:
                importedNode = nodeFactory.createCDATASection(this, node.getNodeValue());
                importChildren = false;
                break;
            case PROCESSING_INSTRUCTION_NODE:
                ProcessingInstruction pi = (ProcessingInstruction)node;
                importedNode = nodeFactory.createProcessingInstruction(this, pi.getTarget(), pi.getData());
                importChildren = false;
                break;
            case DOCUMENT_FRAGMENT_NODE:
                importedNode = nodeFactory.createDocumentFragment(this);
                importChildren = deep;
                break;
            case ENTITY_REFERENCE_NODE:
                importedNode = nodeFactory.createEntityReference(this, node.getNodeName());
                importChildren = false;
                break;
            default:
                throw DOMExceptionUtil.newDOMException(DOMException.NOT_SUPPORTED_ERR);
        }
        if (importChildren) {
            for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                importedNode.appendChild(importNode(child, true));
            }
        }
        return importedNode;
    }

    public final Element getElementById(String elementId) {
        for (Iterator<ElementImpl> it = new DescendantsIterator<ElementImpl>(ElementImpl.class, this); it.hasNext(); ) {
            ElementImpl element = it.next();
            for (AbstractAttrImpl attr = element.internalGetFirstAttribute(); attr != null; attr = attr.internalGetNextAttribute()) {
                if (attr.isId() && elementId.equals(attr.getValue())) {
                    return element;
                }
            }
        }
        return null;
    }

    @Override
    protected final Node shallowClone() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node adoptNode(Node source) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final DOMConfiguration getDomConfig() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean getStrictErrorChecking() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean getXmlStandalone() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String getXmlVersion() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void normalizeDocument() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setStrictErrorChecking(boolean strictErrorChecking) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setXmlVersion(String xmlVersion) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node cloneNode(boolean deep) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
