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
package com.googlecode.ddom.stream.filter.util;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

public class XmlHandlerWrapper implements XmlHandler {
    private final XmlHandler parent;

    public XmlHandlerWrapper(XmlHandler parent) {
        this.parent = parent;
    }

    protected final XmlHandler getParent() {
        return parent;
    }

    public void startEntity(boolean fragment, String inputEncoding) throws StreamException {
        parent.startEntity(fragment, inputEncoding);
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) throws StreamException {
        parent.processXmlDeclaration(version, encoding, standalone);
    }

    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws StreamException {
        parent.startDocumentTypeDeclaration(rootName, publicId, systemId);
    }

    public void endDocumentTypeDeclaration() throws StreamException {
        parent.endDocumentTypeDeclaration();
    }

    public void startElement(String tagName) throws StreamException {
        parent.startElement(tagName);
    }

    public void startElement(String namespaceURI, String localName, String prefix)
            throws StreamException {
        parent.startElement(namespaceURI, localName, prefix);
    }

    public void endElement() throws StreamException {
        parent.endElement();
    }

    public void startAttribute(String name, String type) throws StreamException {
        parent.startAttribute(name, type);
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type)
            throws StreamException {
        parent.startAttribute(namespaceURI, localName, prefix, type);
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        parent.startNamespaceDeclaration(prefix);
    }

    public void endAttribute() throws StreamException {
        parent.endAttribute();
    }

    public void attributesCompleted() throws StreamException {
        parent.attributesCompleted();
    }

    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        parent.processCharacterData(data, ignorable);
    }

    public void startProcessingInstruction(String target) throws StreamException {
        parent.startProcessingInstruction(target);
    }

    public void endProcessingInstruction() throws StreamException {
        parent.endProcessingInstruction();
    }

    public void startComment() throws StreamException {
        parent.startComment();
    }

    public void endComment() throws StreamException {
        parent.endComment();
    }

    public void startCDATASection() throws StreamException {
        parent.startCDATASection();
    }

    public void endCDATASection() throws StreamException {
        parent.endCDATASection();
    }

    public void processEntityReference(String name) throws StreamException {
        parent.processEntityReference(name);
    }

    public void completed() throws StreamException {
        parent.completed();
    }
}
