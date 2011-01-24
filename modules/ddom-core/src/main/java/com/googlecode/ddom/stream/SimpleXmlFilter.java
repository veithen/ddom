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

    protected void attributesCompleted() throws StreamException {
        target.attributesCompleted();
    }

    protected void completed() throws StreamException {
        target.completed();
    }

    protected void endAttribute() throws StreamException {
        target.endAttribute();
    }

    protected void endCDATASection() throws StreamException {
        target.endCDATASection();
    }

    protected void endElement() throws StreamException {
        target.endElement();
    }

    protected void processComment(String data) throws StreamException {
        target.processComment(data);
    }

    protected void processDocumentType(String rootName, String publicId, String systemId, String data) {
        target.processDocumentType(rootName, publicId, systemId, data);
    }

    protected void processEntityReference(String name) {
        target.processEntityReference(name);
    }

    protected void processProcessingInstruction(String piTarget, String data) throws StreamException {
        target.processProcessingInstruction(piTarget, data);
    }

    protected void processText(String data, boolean ignorable) throws StreamException {
        target.processText(data, ignorable);
    }

    protected void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding,
            boolean standalone) {
        target.setDocumentInfo(xmlVersion, xmlEncoding, inputEncoding, standalone);
    }

    protected void startAttribute(String namespaceURI, String localName, String prefix, String type)
            throws StreamException {
        target.startAttribute(namespaceURI, localName, prefix, type);
    }

    protected void startAttribute(String name, String type) throws StreamException {
        target.startAttribute(name, type);
    }

    protected void startCDATASection() throws StreamException {
        target.startCDATASection();
    }

    protected void startElement(String namespaceURI, String localName, String prefix)
            throws StreamException {
        target.startElement(namespaceURI, localName, prefix);
    }

    protected void startElement(String tagName) throws StreamException {
        target.startElement(tagName);
    }

    protected void startNamespaceDeclaration(String prefix) throws StreamException {
        target.startNamespaceDeclaration(prefix);
    }

}
