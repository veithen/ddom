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

public final class NullXmlHandler implements XmlHandler {
    public static final NullXmlHandler INSTANCE = new NullXmlHandler();
    
    private NullXmlHandler() {}
    
    public void startEntity(boolean fragment, String inputEncoding) {
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) {
    }

    public void processDocumentType(String rootName, String publicId, String systemId, String data) {
    }

    public void startElement(String tagName) throws StreamException {
    }
    
    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
    }
    
    public void endElement() throws StreamException {
    }
    
    public void startAttribute(String name, String type) throws StreamException {
    }
    
    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
    }
    
    public void startNamespaceDeclaration(String prefix) throws StreamException {
    }
    
    public void endAttribute() throws StreamException {
    }
    
    public void attributesCompleted() throws StreamException {
    }
    
    public void processCharacterData(String data, boolean ignorable) throws StreamException {
    }
    
    public void startProcessingInstruction(String target) throws StreamException {
    }
    
    public void endProcessingInstruction() throws StreamException {
    }
    
    public void startComment() throws StreamException {
    }
    
    public void endComment() throws StreamException {
    }
    
    public void startCDATASection() throws StreamException {
    }
    
    public void endCDATASection() throws StreamException {
    }
    
    public void processEntityReference(String name) {
    }
    
    public void completed() throws StreamException {
    }
}
