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

final class SimpleXmlFilterHandler implements XmlHandler {
    private final SimpleXmlFilter filter;

    SimpleXmlFilterHandler(SimpleXmlFilter filter) {
        this.filter = filter;
    }

    public void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone) {
        filter.setDocumentInfo(xmlVersion, xmlEncoding, inputEncoding, standalone);
    }

    public void processDocumentType(String rootName, String publicId, String systemId, String data) {
        filter.processDocumentType(rootName, publicId, systemId, data);
    }

    public void startElement(String tagName) throws StreamException {
        filter.startElement(tagName);
    }

    public void startElement(String namespaceURI, String localName, String prefix)
            throws StreamException {
        filter.startElement(namespaceURI, localName, prefix);
    }

    public void endElement() throws StreamException {
        filter.endElement();
    }

    public void startAttribute(String name, String type) throws StreamException {
        filter.startAttribute(name, type);
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type)
            throws StreamException {
        filter.startAttribute(namespaceURI, localName, prefix, type);
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        filter.startNamespaceDeclaration(prefix);
    }

    public void endAttribute() throws StreamException {
        filter.endAttribute();
    }

    public void attributesCompleted() throws StreamException {
        filter.attributesCompleted();
    }

    public void processText(String data, boolean ignorable) throws StreamException {
        filter.processText(data, ignorable);
    }

    public void startProcessingInstruction(String target) throws StreamException {
        filter.startProcessingInstruction(target);
    }

    public void endProcessingInstruction() throws StreamException {
        filter.endProcessingInstruction();
    }

    public void startComment() throws StreamException {
        filter.startComment();
    }

    public void endComment() throws StreamException {
        filter.endComment();
    }

    public void startCDATASection() throws StreamException {
        filter.startCDATASection();
    }

    public void endCDATASection() throws StreamException {
        filter.endCDATASection();
    }

    public void processEntityReference(String name) {
        filter.processEntityReference(name);
    }

    public void completed() throws StreamException {
        filter.completed();
    }
}
