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
package com.google.code.ddom.stream.stax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.XmlOutput;

public class StAXOutput extends XmlOutput {
    private final XMLStreamWriter writer;

    public StAXOutput(XMLStreamWriter writer) {
        this.writer = writer;
    }

    @Override
    protected void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void processDocumentType(String rootName, String publicId, String systemId) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected void processElement(String tagName) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected void processElement(String namespaceURI, String localName, String prefix) throws StreamException {
        try {
            writer.writeStartElement(prefix, localName, namespaceURI);
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
    }

    @Override
    protected void processAttribute(String name, String value, String type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected void processAttribute(String namespaceURI, String localName, String prefix, String value, String type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected void processNamespaceDeclaration(String prefix, String namespaceURI) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected void attributesCompleted() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected void processProcessingInstruction(String target, String data) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected void processText(String data) throws StreamException {
        try {
            writer.writeCharacters(data);
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
    }

    @Override
    protected void processComment(String data) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected void processCDATASection() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected void processEntityReference(String name) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void nodeCompleted() throws StreamException {
        try {
            // TODO
            writer.writeEndElement();
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
    }
}
