/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
package com.googlecode.ddom.stream.dom;

import javax.xml.XMLConstants;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

// TODO: what about ID attributes???
final class DOMOutputHandler implements XmlHandler {
    private final Document document;
    private Node elementParent;
    private Element element;
    private String unresolvedElementPrefix;
    private String unresolvedElementLocalName;
    private Node node;
    private Attr[] attributes = new Attr[16];
    private int attributeCount;
    private boolean coalescing;
    private final StringAccumulator buffer = new StringAccumulator();
    private String piTarget;
    
    DOMOutputHandler(Document document) {
        this.document = document;
        node = document;
    }
    
    private String endCoalescing() {
        String content = buffer.toString();
        coalescing = false;
        buffer.clear();
        return content;
    }

    public void startEntity(boolean fragment, String inputEncoding) {
        // TODO
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) {
        // TODO: process the remaining information
        if (version != null) {
            document.setXmlVersion(version);
        }
    }

    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) {
        node.appendChild(document.getImplementation().createDocumentType(rootName, publicId, systemId));
    }

    public void endDocumentTypeDeclaration() throws StreamException {
        // Do nothing
    }

    private static String getQualifiedName(String localName, String prefix) {
        return prefix.length() == 0 ? localName : prefix + ":" + localName;
    }
    
    private static String emptyStringToNull(String s) {
        return s.length() == 0 ? null : s;
    }
    
    public void startElement(String tagName) {
        elementParent = node;
        element = document.createElement(tagName);
        node.appendChild(element);
        node = element;
    }

    public void startElement(String namespaceURI, String localName, String prefix) {
        elementParent = node;
        if (namespaceURI != null) {
            element = document.createElementNS(emptyStringToNull(namespaceURI), getQualifiedName(localName, prefix));
            elementParent.appendChild(element);
        } else {
            unresolvedElementPrefix = prefix;
            unresolvedElementLocalName = localName;
        }
        node = null;
    }

    public void endElement() {
        node = node.getParentNode();
    }

    private void addAttribute(Attr attr) {
        if (attributeCount == attributes.length) {
            Attr[] newAttributes = new Attr[attributes.length*2];
            System.arraycopy(attributes, 0, newAttributes, 0, attributes.length);
            attributes = newAttributes;
        }
        attributes[attributeCount++] = attr;
        node = attr;
    }
    
    public void startAttribute(String name, String type) {
        addAttribute(document.createAttribute(name));
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) {
        addAttribute(document.createAttributeNS(emptyStringToNull(namespaceURI), getQualifiedName(localName, prefix)));
    }

    public void startNamespaceDeclaration(String prefix) {
        addAttribute(document.createAttributeNS(
                XMLConstants.XMLNS_ATTRIBUTE_NS_URI,
                prefix.length() == 0 ? XMLConstants.XMLNS_ATTRIBUTE
                                     : XMLConstants.XMLNS_ATTRIBUTE + ":" + prefix));
    }

    public void endAttribute() {
        node = null;
    }

    public void resolveElementNamespace(String namespaceURI) throws StreamException {
        element = document.createElementNS(emptyStringToNull(namespaceURI), getQualifiedName(unresolvedElementLocalName, unresolvedElementPrefix));
        elementParent.appendChild(element);
    }

    public void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException {
        Attr attr = attributes[index];
        String prefix = attr.getPrefix();
        String localName = attr.getLocalName();
        attributes[index] = (Attr)document.renameNode(attr, emptyStringToNull(namespaceURI), prefix == null ? localName : prefix + ":" + localName);
    }

    public void attributesCompleted() {
        for (int i=0; i<attributeCount; i++) {
            element.setAttributeNodeNS(attributes[i]);
            attributes[i] = null;
        }
        node = element;
        element = null;
        elementParent = null;
        attributeCount = 0;
    }

    public void completed() {
        // TODO: do we still need this?
        node = node.getParentNode();
    }

    public void startCDATASection() {
        coalescing = true;
    }

    public void endCDATASection() {
        node.appendChild(document.createCDATASection(endCoalescing()));
    }

    public void processCharacterData(String data, boolean ignorable) {
        if (coalescing) {
            buffer.append(data);
        } else if (node != document) { // DOM doesn't allow Text nodes in a Document
            // TODO: process ignorable?
            node.appendChild(document.createTextNode(data));
        }
    }

    public void startComment() {
        coalescing = true;
    }

    public void endComment() {
        node.appendChild(document.createComment(endCoalescing()));
    }

    public void processEntityReference(String name) {
        node.appendChild(document.createEntityReference(name));
    }

    public void startProcessingInstruction(String target) {
        coalescing = true;
        piTarget = target;
    }

    public void endProcessingInstruction() {
        node.appendChild(document.createProcessingInstruction(piTarget, endCoalescing()));
        piTarget = null;
    }
}
