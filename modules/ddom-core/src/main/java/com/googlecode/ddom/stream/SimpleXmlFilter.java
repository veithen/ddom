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

public class SimpleXmlFilter extends XmlFilter {
    private XmlHandler target;
    
    @Override
    protected final XmlHandler createXmlHandler(XmlHandler target) {
        this.target = target;
        return new SimpleXmlFilterHandler(this);
    }

    protected void startEntity(boolean fragment, String inputEncoding) {
        target.startEntity(fragment, inputEncoding);
    }

    protected void processXmlDeclaration(String version, String encoding, Boolean standalone) {
        target.processXmlDeclaration(version, encoding, standalone);
    }

    protected void processDocumentType(String rootName, String publicId, String systemId, String data) {
        target.processDocumentType(rootName, publicId, systemId, data);
    }

    protected void startElement(String tagName) throws StreamException {
        target.startElement(tagName);
    }

    protected void startElement(String namespaceURI, String localName, String prefix)
            throws StreamException {
        target.startElement(namespaceURI, localName, prefix);
    }

    protected void endElement() throws StreamException {
        target.endElement();
    }

    protected void startAttribute(String name, String type) throws StreamException {
        target.startAttribute(name, type);
    }

    protected void startAttribute(String namespaceURI, String localName, String prefix, String type)
            throws StreamException {
        target.startAttribute(namespaceURI, localName, prefix, type);
    }

    protected void startNamespaceDeclaration(String prefix) throws StreamException {
        target.startNamespaceDeclaration(prefix);
    }

    protected void endAttribute() throws StreamException {
        target.endAttribute();
    }

    protected void attributesCompleted() throws StreamException {
        target.attributesCompleted();
    }

    protected void processCharacterData(String data, boolean ignorable) throws StreamException {
        target.processCharacterData(data, ignorable);
    }

    protected void startProcessingInstruction(String piTarget) throws StreamException {
        target.startProcessingInstruction(piTarget);
    }

    protected void endProcessingInstruction() throws StreamException {
        target.endProcessingInstruction();
    }

    protected void startComment() throws StreamException {
        target.startComment();
    }

    protected void endComment() throws StreamException {
        target.endComment();
    }

    protected void startCDATASection() throws StreamException {
        target.startCDATASection();
    }

    protected void endCDATASection() throws StreamException {
        target.endCDATASection();
    }

    protected void processEntityReference(String name) {
        target.processEntityReference(name);
    }

    protected void completed() throws StreamException {
        target.completed();
    }

}
