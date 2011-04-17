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
package com.googlecode.ddom.stream.dom;

import javax.xml.XMLConstants;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.googlecode.ddom.stream.XmlHandler;

// TODO: what about ID attributes???
final class DOMOutputHandler implements XmlHandler {
    private final Document document;
    private Node node;
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

    public void processDocumentType(String rootName, String publicId, String systemId, String data) {
        node.appendChild(document.getImplementation().createDocumentType(rootName, publicId, systemId));
    }

    private static String getQualifiedName(String localName, String prefix) {
        return prefix.length() == 0 ? localName : prefix + ":" + localName;
    }
    
    private static String emptyStringToNull(String s) {
        return s.length() == 0 ? null : s;
    }
    
    public void startElement(String tagName) {
        Element element = document.createElement(tagName);
        node.appendChild(element);
        node = element;
    }

    public void startElement(String namespaceURI, String localName, String prefix) {
        Element element = document.createElementNS(emptyStringToNull(namespaceURI), getQualifiedName(localName, prefix));
        node.appendChild(element);
        node = element;
    }

    public void endElement() {
        node = node.getParentNode();
    }

    public void startAttribute(String name, String type) {
        Attr attr = document.createAttribute(name);
        ((Element)node).setAttributeNode(attr);
        node = attr;
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) {
        Attr attr = document.createAttributeNS(emptyStringToNull(namespaceURI), getQualifiedName(localName, prefix));
        ((Element)node).setAttributeNodeNS(attr);
        node = attr;
    }

    public void startNamespaceDeclaration(String prefix) {
        Attr attr = document.createAttributeNS(
                XMLConstants.XMLNS_ATTRIBUTE_NS_URI,
                prefix.length() == 0 ? XMLConstants.XMLNS_ATTRIBUTE
                                     : XMLConstants.XMLNS_ATTRIBUTE + ":" + prefix);
        ((Element)node).setAttributeNodeNS(attr);
        node = attr;
    }

    public void endAttribute() {
        node = ((Attr)node).getOwnerElement();
    }

    public void attributesCompleted() {
        // Nothing special to do here
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
        } else {
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
