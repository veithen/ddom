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
package com.google.code.ddom.stream.dom;

import javax.xml.XMLConstants;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.Symbols;
import com.google.code.ddom.stream.spi.XmlHandler;
import com.google.code.ddom.stream.spi.XmlInput;

public class DOMInput extends XmlInput {
    private final Node rootNode;
    private Node currentNode;
    private boolean visited;
    
    public DOMInput(Node rootNode) {
        this.rootNode = rootNode;
        if (rootNode instanceof Document) {
            currentNode = rootNode;
        }
    }

    public Symbols getSymbols() {
        // TODO Auto-generated method stub
        return null;
    }

    protected void proceed() throws StreamException {
        XmlHandler handler = getHandler();
        Node currentNode = this.currentNode;
        boolean visited = this.visited;
        loop: while (true) {
            if (visited) {
                if (currentNode == null || currentNode instanceof Document) {
                    throw new IllegalStateException();
                } else {
                    Node node = currentNode.getNextSibling();
                    if (node == null) {
                        if (currentNode == rootNode) {
                            currentNode = null;
                        } else {
                            currentNode = currentNode.getParentNode();
                        }
                        visited = true;
                    } else {
                        currentNode = node;
                        visited = false;
                    }
                }
            } else {
                if (currentNode == null) {
                    currentNode = rootNode;
                } else {
                    Node node = currentNode.getFirstChild();
                    if (node == null) {
                        visited = true;
                    } else {
                        currentNode = node;
                    }
                }
            }
            int nodeType = currentNode == null ? Node.DOCUMENT_NODE : currentNode.getNodeType();
            if (visited) {
                // In the future, there may be other node types that generate events here
                switch (nodeType) {
                    case Node.ELEMENT_NODE:
                        handler.endElement();
                        break loop;
                    case Node.DOCUMENT_NODE:
                        handler.completed(); // TODO
                        break loop;
                }
            } else {
                switch (nodeType) {
                    case Node.ELEMENT_NODE:
                        Element element = (Element)currentNode;
                        String localName = element.getLocalName();
                        if (localName == null) {
                            handler.startElement(element.getTagName());
                        } else {
                            handler.startElement(element.getNamespaceURI(), localName, element.getPrefix());
                        }
                        NamedNodeMap attributes = element.getAttributes();
                        for (int length=attributes.getLength(), i=0; i<length; i++) {
                            Attr attr = (Attr)attributes.item(i);
                            String attrLocalName = attr.getLocalName();
                            if (attrLocalName == null) {
                                // TODO: type information
                                // TODO
//                                handler.processAttribute(attrLocalName, attr.getValue(), null);
                            } else {
                                String namespaceURI = attr.getNamespaceURI();
                                if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
                                    // TODO
//                                    handler.processNamespaceDeclaration(attrLocalName.equals(XMLConstants.XMLNS_ATTRIBUTE) ? null : attrLocalName, attr.getValue());
                                } else {
                                    // TODO
//                                    handler.processAttribute(namespaceURI, attrLocalName, attr.getPrefix(), attr.getValue(), null);
                                }
                            }
                        }
                        handler.attributesCompleted();
                        break loop;
                    case Node.TEXT_NODE:
                        handler.processText(currentNode.getNodeValue(), false); // TODO: ignorable?
                        break loop;
                    default:
                        // TODO
                        throw new UnsupportedOperationException("Unsupported node type " + nodeType);
                }
            }
        }
        this.currentNode = currentNode;
        this.visited = visited;
    }

    public void dispose() {
        // TODO Auto-generated method stub
        
    }
}
