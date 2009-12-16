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
package com.google.code.ddom.stream.dom;

import javax.xml.XMLConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.code.ddom.spi.stream.AttributeData;
import com.google.code.ddom.spi.stream.AttributeMode;
import com.google.code.ddom.spi.stream.CharacterData;
import com.google.code.ddom.spi.stream.StreamException;
import com.google.code.ddom.stream.util.CallbackConsumer;

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
// TODO: this is a strange name: it is indeed an event consumer, but a DOM producer... Maybe we should use StreamSink and StreamSource?
// TODO: what about ID attributes???
public class DOMConsumer extends CallbackConsumer {
    private final Document document;
    private Node node;
    
    public DOMConsumer(Document document) {
        super(AttributeMode.EVENT);
        this.document = document;
        node = document;
    }

    public void processDocumentType(String rootName, String publicId, String systemId) {
        node.appendChild(document.getImplementation().createDocumentType(rootName, publicId, systemId));
    }

    private static String getQualifiedName(String localName, String prefix) {
        return prefix == null ? localName : prefix + ":" + localName;
    }
    
    public void processElement(String tagName, AttributeData attributes) {
        Element element = document.createElement(tagName);
        node.appendChild(element);
        node = element;
    }

    public void processElement(String namespaceURI, String localName, String prefix, AttributeData attributes) {
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

    public void processNSDecl(String prefix, String namespaceURI) {
        if (prefix == null) {
            // TODO: check this
            ((Element)node).setAttributeNS("", XMLConstants.XMLNS_ATTRIBUTE, namespaceURI);
        } else {
            ((Element)node).setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE + ":" + prefix, namespaceURI);
        }
    }

    public void attributesCompleted() {
        // Nothing special to do here
    }

    public void nodeCompleted() {
        node = node.getParentNode();
    }

    public void processCDATASection(CharacterData data) {
        try {
            node.appendChild(document.createCDATASection(data.getString()));
        } catch (StreamException ex) {
            throw new RuntimeException(ex); // TODO
        }
    }

    public void processText(CharacterData data) {
        try {
            node.appendChild(document.createTextNode(data.getString()));
        } catch (StreamException ex) {
            throw new RuntimeException(ex); // TODO
        }
    }

    public void processComment(CharacterData data) {
        try {
            node.appendChild(document.createComment(data.getString()));
        } catch (StreamException ex) {
            throw new RuntimeException(ex); // TODO
        }
    }

    public void processEntityReference(String name) {
        node.appendChild(document.createEntityReference(name));
    }

    public void processProcessingInstruction(String target, CharacterData data) {
        try {
            node.appendChild(document.createProcessingInstruction(target, data.getString()));
        } catch (StreamException ex) {
            throw new RuntimeException(ex); // TODO
        }
    }
}
