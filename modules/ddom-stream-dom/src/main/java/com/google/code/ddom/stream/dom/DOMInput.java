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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.google.code.ddom.stream.spi.Input;
import com.google.code.ddom.stream.spi.Output;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.Symbols;

public class DOMInput implements Input {
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

    public boolean proceed(Output output) throws StreamException {
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
                    case Node.DOCUMENT_NODE:
                        output.nodeCompleted();
                        break loop;
                }
            } else {
                switch (nodeType) {
                    case Node.ELEMENT_NODE:
                        Element element = (Element)currentNode;
                        String localName = element.getLocalName();
                        if (localName == null) {
                            output.processElement(element.getTagName());
                        } else {
                            output.processElement(element.getNamespaceURI(), localName, element.getPrefix());
                        }
                        NamedNodeMap attributes = element.getAttributes();
                        for (int length=attributes.getLength(), i=0; i<length; i++) {
                            Attr attr = (Attr)attributes.item(i);
                            String attrLocalName = attr.getLocalName();
                            if (attrLocalName == null) {
                                // TODO: type information
                                output.processAttribute(attrLocalName, attr.getValue(), null);
                            } else {
                                // TODO: distinguish namespace declarations
                                output.processAttribute(attr.getNamespaceURI(), attrLocalName, attr.getPrefix(), attr.getValue(), null);
                            }
                        }
                        output.attributesCompleted();
                        break loop;
                    case Node.TEXT_NODE:
                        output.processText(currentNode.getNodeValue());
                        break loop;
                }
            }
        }
        this.currentNode = currentNode;
        this.visited = visited;
        // TODO
        return true;
    }

    public void dispose() {
        // TODO Auto-generated method stub
        
    }
}
