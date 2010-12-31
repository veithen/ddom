/*
 * Copyright 2009-2010 Andreas Veithen
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

final class DelegatingXmlHandler implements XmlHandler{
    private XmlHandler delegate;

    private XmlHandler getDelegate() {
        if (delegate == null) {
            throw new IllegalStateException("Delegate has not been set");
        }
        return delegate;
    }

    void setDelegate(XmlHandler delegate) {
        this.delegate = delegate;
    }

    public void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone) {
        getDelegate().setDocumentInfo(xmlVersion, xmlEncoding, inputEncoding, standalone);
    }

    public void processDocumentType(String rootName, String publicId, String systemId) {
        getDelegate().processDocumentType(rootName, publicId, systemId);
    }

    public void processElement(String tagName) {
        getDelegate().processElement(tagName);
    }

    public void processElement(String namespaceURI, String localName, String prefix) throws StreamException {
        getDelegate().processElement(namespaceURI, localName, prefix);
    }

    public void processAttribute(String name, String value, String type) {
        getDelegate().processAttribute(name, value, type);
    }

    public void processAttribute(String namespaceURI, String localName, String prefix, String value, String type) {
        getDelegate().processAttribute(namespaceURI, localName, prefix, value, type);
    }

    public void processNamespaceDeclaration(String prefix, String namespaceURI) {
        getDelegate().processNamespaceDeclaration(prefix, namespaceURI);
    }
    
    public void attributesCompleted() {
        getDelegate().attributesCompleted();
    }

    public void processProcessingInstruction(String target, String data) {
        getDelegate().processProcessingInstruction(target, data);
    }

    public void processText(String data) throws StreamException {
        getDelegate().processText(data);
    }

    public void processComment(String data) {
        getDelegate().processComment(data);
    }

    public void processCDATASection() {
        getDelegate().processCDATASection();
    }

    public void processEntityReference(String name) {
        getDelegate().processEntityReference(name);
    }

    public void nodeCompleted() throws StreamException {
        getDelegate().nodeCompleted();
    }
}
