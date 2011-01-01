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
package com.google.code.ddom.stream.stax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.XmlOutput;

public class StAXOutput extends XmlOutput {
    private static final int ATT_NS_AWARE = 1;
    private static final int ATT_NS_UNAWARE = 2;
    private static final int ATT_NAMESPACE_DECLARATION = 3;
    
    private final XMLStreamWriter writer;
    private boolean coalescing;
    private final StringAccumulator buffer = new StringAccumulator();
    private int attType;
    private String attNamespaceURI;
    private String attName;
    private String attPrefix;

    public StAXOutput(XMLStreamWriter writer) {
        this.writer = writer;
    }
    
    private void startCoalescing() {
        coalescing = true;
    }
    
    private String endCoalescing() {
        coalescing = false;
        String data = buffer.toString();
        buffer.clear();
        return data;
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
    protected void startElement(String tagName) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        try {
            writer.writeStartElement(prefix, localName, namespaceURI);
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
    }

    @Override
    protected void endElement() throws StreamException {
        try {
            writer.writeEndElement();
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
    }

    @Override
    protected void startAttribute(String name, String type) throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        attType = ATT_NS_AWARE;
        attNamespaceURI = namespaceURI;
        attName = localName;
        attPrefix = prefix;
        startCoalescing();
    }

    @Override
    protected void startNamespaceDeclaration(String prefix) throws StreamException {
        attType = ATT_NAMESPACE_DECLARATION;
        attPrefix = prefix;
        startCoalescing();
    }

    @Override
    protected void endAttribute() throws StreamException {
        try {
            switch (attType) {
                case ATT_NS_AWARE:
                    writer.writeAttribute(attPrefix, attNamespaceURI, attName, endCoalescing());
                    break;
                case ATT_NAMESPACE_DECLARATION:
                    writer.writeNamespace(attPrefix, endCoalescing());
                    break;
            }
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
    }

    @Override
    protected void attributesCompleted() {
        // Nothing special to do here
    }

    @Override
    protected void processProcessingInstruction(String target, String data) throws StreamException {
        try {
            writer.writeProcessingInstruction(target, data);
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
    }

    @Override
    protected void processText(String data) throws StreamException {
        if (coalescing) {
            buffer.append(data);
        } else {
            try {
                writer.writeCharacters(data);
            } catch (XMLStreamException ex) {
                throw new StreamException(ex);
            }
        }
    }

    @Override
    protected void processComment(String data) throws StreamException {
        try {
            writer.writeComment(data);
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
    }

    @Override
    protected void startCDATASection() throws StreamException {
        startCoalescing();
    }

    @Override
    protected void endCDATASection() throws StreamException {
        try {
            writer.writeCData(endCoalescing());
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
    }

    @Override
    protected void processEntityReference(String name) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void completed() throws StreamException {
        try {
            writer.writeEndDocument();
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
    }
}
