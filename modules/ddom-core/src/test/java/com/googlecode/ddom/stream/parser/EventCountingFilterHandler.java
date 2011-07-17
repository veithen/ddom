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
package com.googlecode.ddom.stream.parser;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

final class EventCountingFilterHandler implements XmlHandler {
    private final EventCountingFilter filter;
    private final XmlHandler parent;
    
    public EventCountingFilterHandler(EventCountingFilter filter, XmlHandler parent) {
        this.filter = filter;
        this.parent = parent;
    }

    public void startEntity(boolean fragment, String inputEncoding) throws StreamException {
        filter.increment("startEntity");
        parent.startEntity(fragment, inputEncoding);
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) throws StreamException {
        filter.increment("processXmlDeclaration");
        parent.processXmlDeclaration(version, encoding, standalone);
    }

    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws StreamException {
        filter.increment("startDocumentTypeDeclaration");
        parent.startDocumentTypeDeclaration(rootName, publicId, systemId);
    }

    public void endDocumentTypeDeclaration() throws StreamException {
        filter.increment("endDocumentTypeDeclaration");
        parent.endDocumentTypeDeclaration();
    }

    public void startElement(String tagName) throws StreamException {
        filter.increment("startElement");
        parent.startElement(tagName);
    }

    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        filter.increment("startElement");
        parent.startElement(namespaceURI, localName, prefix);
    }

    public void endElement() throws StreamException {
        filter.increment("endElement");
        parent.endElement();
    }

    public void startAttribute(String name, String type) throws StreamException {
        filter.increment("startAttribute");
        parent.startAttribute(name, type);
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        filter.increment("startAttribute");
        parent.startAttribute(namespaceURI, localName, prefix, type);
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        filter.increment("startNamespaceDeclaration");
        parent.startNamespaceDeclaration(prefix);
    }

    public void endAttribute() throws StreamException {
        filter.increment("endAttribute");
        parent.endAttribute();
    }

    public void attributesCompleted() throws StreamException {
        filter.increment("attributesCompleted");
        parent.attributesCompleted();
    }

    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        filter.increment("processCharacterData");
        parent.processCharacterData(data, ignorable);
    }

    public void startProcessingInstruction(String target) throws StreamException {
        filter.increment("startProcessingInstruction");
        parent.startProcessingInstruction(target);
    }

    public void endProcessingInstruction() throws StreamException {
        filter.increment("endProcessingInstruction");
        parent.endProcessingInstruction();
    }

    public void startComment() throws StreamException {
        filter.increment("startComment");
        parent.startComment();
    }

    public void endComment() throws StreamException {
        filter.increment("endComment");
        parent.endComment();
    }

    public void startCDATASection() throws StreamException {
        filter.increment("startCDATASection");
        parent.startCDATASection();
    }

    public void endCDATASection() throws StreamException {
        filter.increment("endCDATASection");
        parent.endCDATASection();
    }

    public void processEntityReference(String name) throws StreamException {
        filter.increment("processEntityReference");
        parent.processEntityReference(name);
    }

    public void completed() throws StreamException {
        filter.increment("completed");
        parent.completed();
    }
}
