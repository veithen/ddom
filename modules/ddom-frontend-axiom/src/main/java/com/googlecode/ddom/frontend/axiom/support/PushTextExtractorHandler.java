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
package com.googlecode.ddom.frontend.axiom.support;

import java.io.IOException;
import java.io.Writer;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

final class PushTextExtractorHandler implements XmlHandler {
    private final Writer writer;
    private int depth;
    
    public PushTextExtractorHandler(Writer writer) {
        this.writer = writer;
    }

    public void startEntity(boolean fragment, String inputEncoding) {
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) {
    }

    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) {
    }

    public void endDocumentTypeDeclaration() throws StreamException {
    }

    public void startElement(String tagName) throws StreamException {
        depth++;
    }

    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        depth++;
    }

    public void endElement() throws StreamException {
        depth--;
    }

    public void startAttribute(String name, String type) throws StreamException {
        depth++;
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        depth++;
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        depth++;
    }

    public void endAttribute() throws StreamException {
        depth--;
    }

    public void attributesCompleted() {
    }

    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        if (depth == 1) {
            try {
                writer.write(data);
            } catch (IOException ex) {
                throw new StreamException(ex);
            }
        }
    }

    public void startProcessingInstruction(String target) throws StreamException {
        depth++;
    }

    public void endProcessingInstruction() throws StreamException {
        depth--;
    }

    public void startComment() throws StreamException {
        depth++;
    }

    public void endComment() throws StreamException {
        depth--;
    }

    public void startCDATASection() throws StreamException {
    }

    public void endCDATASection() throws StreamException {
    }

    public void processEntityReference(String name) {
    }
    
    public void completed() throws StreamException {
        // The OMElement#writeTextTo contract specifies that Writer#close is _not_ called
    }
}
