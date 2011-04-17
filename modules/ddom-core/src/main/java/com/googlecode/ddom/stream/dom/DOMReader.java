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
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlReader;

final class DOMReader implements XmlReader {
    private final XmlHandler handler;
    private final Node rootNode;
    private Node currentNode;
    private boolean visited;
    
    DOMReader(XmlHandler handler, Node rootNode) {
        this.handler = handler;
        this.rootNode = rootNode;
    }

    private static String nullToEmptyString(String s) {
        return s == null ? "" : s;
    }
    
    public void proceed(boolean flush) throws StreamException {
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
                    case Node.DOCUMENT_NODE:
                        if (currentNode != null) {
                            Document document = (Document)currentNode;
                            handler.startEntity(false, document.getInputEncoding());
                            handler.processXmlDeclaration(document.getXmlVersion(), document.getXmlEncoding(), document.getXmlStandalone());
                        }
                        break;
                    case Node.DOCUMENT_TYPE_NODE:
                        DocumentType docType = (DocumentType)currentNode;
                        handler.processDocumentType(docType.getName(), docType.getPublicId(), docType.getSystemId(), null);
                        break loop;
                    case Node.ELEMENT_NODE:
                        Element element = (Element)currentNode;
                        String localName = element.getLocalName();
                        if (localName == null) {
                            handler.startElement(element.getTagName());
                        } else {
                            handler.startElement(nullToEmptyString(element.getNamespaceURI()), localName, nullToEmptyString(element.getPrefix()));
                        }
                        NamedNodeMap attributes = element.getAttributes();
                        // TODO: we should not push all attributes at once
                        for (int length=attributes.getLength(), i=0; i<length; i++) {
                            Attr attr = (Attr)attributes.item(i);
                            String attrLocalName = attr.getLocalName();
                            if (attrLocalName == null) {
                                // TODO: type information
                                handler.startAttribute(attr.getName(), null);
                            } else {
                                String namespaceURI = attr.getNamespaceURI();
                                if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
                                    handler.startNamespaceDeclaration(attrLocalName.equals(XMLConstants.XMLNS_ATTRIBUTE) ? "" : attrLocalName);
                                } else {
                                    handler.startAttribute(nullToEmptyString(namespaceURI), attrLocalName, nullToEmptyString(attr.getPrefix()), null);
                                }
                            }
                            handler.processCharacterData(attr.getValue(), false);
                            handler.endAttribute();
                        }
                        handler.attributesCompleted();
                        break loop;
                    case Node.TEXT_NODE:
                        handler.processCharacterData(currentNode.getNodeValue(), false); // TODO: ignorable?
                        break loop;
                    case Node.CDATA_SECTION_NODE:
                        handler.startCDATASection();
                        handler.processCharacterData(currentNode.getNodeValue(), false);
                        handler.endCDATASection();
                        break loop;
                    case Node.COMMENT_NODE:
                        handler.startComment();
                        handler.processCharacterData(currentNode.getNodeValue(), false);
                        handler.endComment();
                        break loop;
                    case Node.PROCESSING_INSTRUCTION_NODE:
                        ProcessingInstruction pi = (ProcessingInstruction)currentNode;
                        handler.startProcessingInstruction(pi.getTarget());
                        handler.processCharacterData(pi.getData(), false);
                        handler.endProcessingInstruction();
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
}
