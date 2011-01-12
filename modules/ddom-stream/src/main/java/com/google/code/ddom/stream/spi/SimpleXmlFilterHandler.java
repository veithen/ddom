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
package com.google.code.ddom.stream.spi;

class SimpleXmlFilterHandler implements XmlHandler {
    private final SimpleXmlFilter filter;

    SimpleXmlFilterHandler(SimpleXmlFilter filter) {
        this.filter = filter;
    }

    public void attributesCompleted() throws StreamException {
        filter.attributesCompleted();
    }

    public void completed() throws StreamException {
        filter.completed();
    }

    public void endAttribute() throws StreamException {
        filter.endAttribute();
    }

    public void endCDATASection() throws StreamException {
        filter.endCDATASection();
    }

    public void endElement() throws StreamException {
        filter.endElement();
    }

    public void processComment(String data) throws StreamException {
        filter.processComment(data);
    }

    public void processDocumentType(String rootName, String publicId, String systemId, String data) {
        filter.processDocumentType(rootName, publicId, systemId, data);
    }

    public void processEntityReference(String name) {
        filter.processEntityReference(name);
    }

    public void processProcessingInstruction(String piTarget, String data) throws StreamException {
        filter.processProcessingInstruction(piTarget, data);
    }

    public void processText(String data, boolean ignorable) throws StreamException {
        filter.processText(data, ignorable);
    }

    public void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding,
            boolean standalone) {
        filter.setDocumentInfo(xmlVersion, xmlEncoding, inputEncoding, standalone);
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type)
            throws StreamException {
        filter.startAttribute(namespaceURI, localName, prefix, type);
    }

    public void startAttribute(String name, String type) throws StreamException {
        filter.startAttribute(name, type);
    }

    public void startCDATASection() throws StreamException {
        filter.startCDATASection();
    }

    public void startElement(String namespaceURI, String localName, String prefix)
            throws StreamException {
        filter.startElement(namespaceURI, localName, prefix);
    }

    public void startElement(String tagName) throws StreamException {
        filter.startElement(tagName);
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        filter.startNamespaceDeclaration(prefix);
    }
}
