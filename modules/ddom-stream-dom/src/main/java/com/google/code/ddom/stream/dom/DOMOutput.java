/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.google.code.ddom.stream.dom;

import javax.xml.XMLConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.code.ddom.stream.spi.Output;

/**
 * 
 * 
 * Limitations:
 * <ul>
 *   <li>Information about attribute types is lost.
 * </ul>
 * 
 * @author Andreas Veithen
 */
// TODO: what about ID attributes???
public class DOMOutput implements Output {
    private final Document document;
    private Node node;
    
    public DOMOutput(Document document) {
        this.document = document;
        node = document;
    }

    public void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone) {
        // TODO: process the remaining information
        if (xmlVersion != null) {
            document.setXmlVersion(xmlVersion);
        }
    }

    public void processDocumentType(String rootName, String publicId, String systemId) {
        node.appendChild(document.getImplementation().createDocumentType(rootName, publicId, systemId));
    }

    private static String getQualifiedName(String localName, String prefix) {
        return prefix == null ? localName : prefix + ":" + localName;
    }
    
    public void processElement(String tagName) {
        Element element = document.createElement(tagName);
        node.appendChild(element);
        node = element;
    }

    public void processElement(String namespaceURI, String localName, String prefix) {
        Element element = document.createElementNS(namespaceURI, getQualifiedName(localName, prefix));
        node.appendChild(element);
        node = element;
    }

    public void processAttribute(String name, String value, String type) {
        ((Element)node).setAttribute(name, value);
    }

    public void processAttribute(String namespaceURI, String localName, String prefix, String value, String type) {
        ((Element)node).setAttributeNS(namespaceURI, getQualifiedName(localName, prefix), value);
    }

    public void processNamespaceDeclaration(String prefix, String namespaceURI) {
        ((Element)node).setAttributeNS(
                XMLConstants.XMLNS_ATTRIBUTE_NS_URI,
                prefix == null ? XMLConstants.XMLNS_ATTRIBUTE
                               : XMLConstants.XMLNS_ATTRIBUTE + ":" + prefix,
                namespaceURI);
    }

    public void attributesCompleted() {
        // Nothing special to do here
    }

    public void nodeCompleted() {
        node = node.getParentNode();
    }

    public void processCDATASection(String data) {
        node.appendChild(document.createCDATASection(data));
    }

    public void processText(String data) {
        node.appendChild(document.createTextNode(data));
    }

    public void processComment(String data) {
        node.appendChild(document.createComment(data));
    }

    public void processEntityReference(String name) {
        node.appendChild(document.createEntityReference(name));
    }

    public void processProcessingInstruction(String target, String data) {
        node.appendChild(document.createProcessingInstruction(target, data));
    }
}
