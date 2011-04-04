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

final class DelegatingXmlHandler implements XmlHandler{
    private XmlHandler delegate;
    private boolean complete;

    private XmlHandler getDelegate() {
        if (delegate == null) {
            throw new IllegalStateException("Delegate has not been set");
        }
        return delegate;
    }

    void setDelegate(XmlHandler delegate) {
        this.delegate = delegate;
    }
    
    boolean isComplete() {
        return complete;
    }

    public void startEntity(boolean fragment, String inputEncoding) {
        getDelegate().startEntity(fragment, inputEncoding);
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) {
        getDelegate().processXmlDeclaration(version, encoding, standalone);
    }

    public void processDocumentType(String rootName, String publicId, String systemId, String data) {
        getDelegate().processDocumentType(rootName, publicId, systemId, data);
    }

    public void startElement(String tagName) throws StreamException {
        getDelegate().startElement(tagName);
    }

    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        getDelegate().startElement(namespaceURI, localName, prefix);
    }

    public void endElement() throws StreamException {
        getDelegate().endElement();
    }

    public void startAttribute(String name, String type) throws StreamException {
        getDelegate().startAttribute(name, type);
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        getDelegate().startAttribute(namespaceURI, localName, prefix, type);
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        getDelegate().startNamespaceDeclaration(prefix);
    }
    
    public void endAttribute() throws StreamException {
        getDelegate().endAttribute();
    }

    public void attributesCompleted() throws StreamException {
        getDelegate().attributesCompleted();
    }

    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        getDelegate().processCharacterData(data, ignorable);
    }

    public void startProcessingInstruction(String target) throws StreamException {
        getDelegate().startProcessingInstruction(target);
    }

    public void endProcessingInstruction() throws StreamException {
        getDelegate().endProcessingInstruction();
    }

    public void startComment() throws StreamException {
        getDelegate().startComment();
    }

    public void endComment() throws StreamException {
        getDelegate().endComment();
    }

    public void startCDATASection() throws StreamException {
        getDelegate().startCDATASection();
    }

    public void endCDATASection() throws StreamException {
        getDelegate().endCDATASection();
    }

    public void processEntityReference(String name) {
        getDelegate().processEntityReference(name);
    }

    public void completed() throws StreamException {
        getDelegate().completed();
        complete = true;
    }
}
