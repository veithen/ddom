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

final class XmlOutputHandler implements XmlHandler{
    private final XmlOutput output;

    XmlOutputHandler(XmlOutput delegate) {
        this.output = delegate;
    }

    public void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone) {
        output.setDocumentInfo(xmlVersion, xmlEncoding, inputEncoding, standalone);
    }

    public void processDocumentType(String rootName, String publicId, String systemId) {
        output.processDocumentType(rootName, publicId, systemId);
    }

    public void startElement(String tagName) throws StreamException {
        output.startElement(tagName);
    }

    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        output.startElement(namespaceURI, localName, prefix);
    }

    public void endElement() throws StreamException {
        output.endElement();
    }

    public void startAttribute(String name, String type) throws StreamException {
        output.startAttribute(name, type);
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        output.startAttribute(namespaceURI, localName, prefix, type);
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        output.startNamespaceDeclaration(prefix);
    }
    
    public void endAttribute() throws StreamException {
        output.endAttribute();
    }

    public void attributesCompleted() {
        output.attributesCompleted();
    }

    public void processProcessingInstruction(String target, String data) throws StreamException {
        output.processProcessingInstruction(target, data);
    }

    public void processText(String data) throws StreamException {
        output.processText(data);
    }

    public void processComment(String data) throws StreamException {
        output.processComment(data);
    }

    public void startCDATASection() throws StreamException {
        output.startCDATASection();
    }

    public void endCDATASection() throws StreamException {
        output.endCDATASection();
    }

    public void processEntityReference(String name) {
        output.processEntityReference(name);
    }

    public void completed() throws StreamException {
        output.completed();
    }
}
