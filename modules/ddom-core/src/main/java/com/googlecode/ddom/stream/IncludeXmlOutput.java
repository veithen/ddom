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
package com.googlecode.ddom.stream;

/**
 * {@link XmlOutput} implementation that allows to include the events produced by one {@link XmlInput}
 * into the sequence of events produced by another {@link XmlInput}.
 * 
 * @author Andreas Veithen
 */
public class IncludeXmlOutput extends SimpleXmlOutput {
    private final XmlHandler handler;

    public IncludeXmlOutput(XmlHandler handler) {
        this.handler = handler;
    }

    @Override
    protected void startEntity(boolean fragment, String inputEncoding) {
        // Do nothing.
    }

    @Override
    protected void processXmlDeclaration(String version, String encoding, Boolean standalone) {
        // Do nothing.
    }

    @Override
    protected void processDocumentType(String rootName, String publicId, String systemId, String data) {
        handler.processDocumentType(rootName, publicId, systemId, data);
    }

    @Override
    protected void startElement(String tagName) throws StreamException {
        handler.startElement(tagName);
    }

    @Override
    protected void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        handler.startElement(namespaceURI, localName, prefix);
    }

    @Override
    protected void endElement() throws StreamException {
        handler.endElement();
    }

    @Override
    protected void startAttribute(String name, String type) throws StreamException {
        handler.startAttribute(name, type);
    }

    @Override
    protected void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        handler.startAttribute(namespaceURI, localName, prefix, type);
    }

    @Override
    protected void startNamespaceDeclaration(String prefix) throws StreamException {
        handler.startNamespaceDeclaration(prefix);
    }

    @Override
    protected void endAttribute() throws StreamException {
        handler.endAttribute();
    }

    @Override
    protected void attributesCompleted() throws StreamException {
        handler.attributesCompleted();
    }

    @Override
    protected void processCharacterData(String data, boolean ignorable) throws StreamException {
        handler.processCharacterData(data, ignorable);
    }

    @Override
    protected void startProcessingInstruction(String target) throws StreamException {
        handler.startProcessingInstruction(target);
    }

    @Override
    protected void endProcessingInstruction() throws StreamException {
        handler.endProcessingInstruction();
    }

    @Override
    protected void startComment() throws StreamException {
        handler.startComment();
    }

    @Override
    protected void endComment() throws StreamException {
        handler.endComment();
    }

    @Override
    protected void startCDATASection() throws StreamException {
        handler.startCDATASection();
    }

    @Override
    protected void endCDATASection() throws StreamException {
        handler.endCDATASection();
    }

    @Override
    protected void processEntityReference(String name) {
        handler.processEntityReference(name);
    }

    @Override
    protected void completed() throws StreamException {
        // Do nothing.
    }
}
