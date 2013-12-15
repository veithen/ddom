/*
 * Copyright 2013 Andreas Veithen
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
package com.googlecode.ddom.backend.linkedlist;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

final class BuilderHandler implements XmlHandler {
    private BuilderHandlerDelegate delegate;
    
    BuilderHandler(BuilderHandlerDelegate rootDelegate) {
        delegate = rootDelegate;
    }

    public void startEntity(boolean fragment, String inputEncoding) {
        delegate.startEntity(fragment, inputEncoding);
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) {
        delegate.processXmlDeclaration(version, encoding, standalone);
    }

    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws StreamException {
        delegate.startDocumentTypeDeclaration(rootName, publicId, systemId);
    }
    
    public void endDocumentTypeDeclaration() throws StreamException {
        delegate = delegate.endDocumentTypeDeclaration();
    }

    public void startElement(String tagName) throws StreamException {
        delegate = delegate.startElement(tagName);
    }
    
    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        delegate = delegate.startElement(namespaceURI, localName, prefix);
    }
    
    public void endElement() throws StreamException {
        delegate = delegate.endElement();
    }

    public void startAttribute(String name, String type) throws StreamException {
        delegate = delegate.startAttribute(name, type);
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        delegate = delegate.startAttribute(namespaceURI, localName, prefix, type);
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        delegate = delegate.startNamespaceDeclaration(prefix);
    }

    public void endAttribute() throws StreamException {
        delegate = delegate.endAttribute();
    }

    public void resolveElementNamespace(String namespaceURI) throws StreamException {
        delegate.resolveElementNamespace(namespaceURI);
    }

    public void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException {
        delegate.resolveAttributeNamespace(index, namespaceURI);
    }

    public void attributesCompleted() throws StreamException {
        delegate.attributesCompleted();
    }

    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        delegate.processCharacterData(data, ignorable);
    }
    
    public void startProcessingInstruction(String target) throws StreamException {
        delegate = delegate.startProcessingInstruction(target);
    }
    
    public void endProcessingInstruction() throws StreamException {
        delegate = delegate.endProcessingInstruction();
    }

    public void startComment() throws StreamException {
        delegate = delegate.startComment();
    }
    
    public void endComment() throws StreamException {
        delegate = delegate.endComment();
    }
    
    public void startCDATASection() throws StreamException {
        delegate = delegate.startCDATASection();
    }
    
    public void endCDATASection() throws StreamException {
        delegate = delegate.endCDATASection();
    }

    public void processEntityReference(String name) throws StreamException {
        delegate.processEntityReference(name);
    }
    
    public void completed() throws StreamException {
        delegate = delegate.completed();
    }
}