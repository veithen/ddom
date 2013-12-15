/*
 * Copyright 2009-2011,2013 Andreas Veithen
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

public final class NullXmlHandler implements XmlHandler {
    public static final NullXmlHandler INSTANCE = new NullXmlHandler();
    
    private NullXmlHandler() {}
    
    public void startEntity(boolean fragment, String inputEncoding) {
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) {
    }

    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) {
    }

    public void endDocumentTypeDeclaration() {
    }

    public void startElement(String tagName) {
    }
    
    public void startElement(String namespaceURI, String localName, String prefix) {
    }
    
    public void endElement() {
    }
    
    public void startAttribute(String name, String type) {
    }
    
    public void startAttribute(String namespaceURI, String localName, String prefix, String type) {
    }
    
    public void startNamespaceDeclaration(String prefix) {
    }
    
    public void endAttribute() {
    }
    
    public void resolveElementNamespace(String namespaceURI) throws StreamException {
    }

    public void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException {
    }

    public void attributesCompleted() {
    }
    
    public void processCharacterData(String data, boolean ignorable) {
    }
    
    public void startProcessingInstruction(String target) {
    }
    
    public void endProcessingInstruction() {
    }
    
    public void startComment() {
    }
    
    public void endComment() {
    }
    
    public void startCDATASection() {
    }
    
    public void endCDATASection() {
    }
    
    public void processEntityReference(String name) {
    }
    
    public void completed() {
    }
}
