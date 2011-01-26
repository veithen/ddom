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
import com.googlecode.ddom.stream.SimpleXmlOutput;
import com.googlecode.ddom.stream.StreamException;

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
public class DOMOutput extends SimpleXmlOutput {
    private final Document document;
    private Node node;
    private boolean isCDATASection;
    private final StringAccumulator cdataSectionContent = new StringAccumulator();
    
    public DOMOutput(Document document) {
        this.document = document;
        node = document;
    }

    @Override
    protected void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone) {
        // TODO: process the remaining information
        if (xmlVersion != null) {
            document.setXmlVersion(xmlVersion);
        }
    }

    @Override
    protected void processDocumentType(String rootName, String publicId, String systemId, String data) {
        node.appendChild(document.getImplementation().createDocumentType(rootName, publicId, systemId));
    }

    private static String getQualifiedName(String localName, String prefix) {
        return prefix == null ? localName : prefix + ":" + localName;
    }
    
    @Override
    protected void startElement(String tagName) {
        Element element = document.createElement(tagName);
        node.appendChild(element);
        node = element;
    }

    @Override
    protected void startElement(String namespaceURI, String localName, String prefix) {
        Element element = document.createElementNS(namespaceURI, getQualifiedName(localName, prefix));
        node.appendChild(element);
        node = element;
    }

    @Override
    protected void endElement() throws StreamException {
        node = node.getParentNode();
    }

    @Override
    protected void startAttribute(String name, String type) {
        Attr attr = document.createAttribute(name);
        ((Element)node).setAttributeNode(attr);
        node = attr;
    }

    @Override
    protected void startAttribute(String namespaceURI, String localName, String prefix, String type) {
        Attr attr = document.createAttributeNS(namespaceURI, getQualifiedName(localName, prefix));
        ((Element)node).setAttributeNodeNS(attr);
        node = attr;
    }

    @Override
    protected void startNamespaceDeclaration(String prefix) {
        Attr attr = document.createAttributeNS(
                XMLConstants.XMLNS_ATTRIBUTE_NS_URI,
                prefix == null ? XMLConstants.XMLNS_ATTRIBUTE
                               : XMLConstants.XMLNS_ATTRIBUTE + ":" + prefix);
        ((Element)node).setAttributeNodeNS(attr);
        node = attr;
    }

    @Override
    protected void endAttribute() throws StreamException {
        node = ((Attr)node).getOwnerElement();
    }

    @Override
    protected void attributesCompleted() {
        // Nothing special to do here
    }

    @Override
    protected void completed() {
        // TODO: do we still need this?
        node = node.getParentNode();
    }

    @Override
    protected void startCDATASection() {
        isCDATASection = true;
    }

    @Override
    protected void endCDATASection() throws StreamException {
        node.appendChild(document.createCDATASection(cdataSectionContent.toString()));
        isCDATASection = false;
        cdataSectionContent.clear();
    }

    @Override
    protected void processText(String data, boolean ignorable) {
        if (isCDATASection) {
            cdataSectionContent.append(data);
        } else {
            // TODO: process ignorable?
            node.appendChild(document.createTextNode(data));
        }
    }

    @Override
    protected void processComment(String data) {
        node.appendChild(document.createComment(data));
    }

    @Override
    protected void processEntityReference(String name) {
        node.appendChild(document.createEntityReference(name));
    }

    @Override
    protected void processProcessingInstruction(String target, String data) {
        node.appendChild(document.createProcessingInstruction(target, data));
    }
}
