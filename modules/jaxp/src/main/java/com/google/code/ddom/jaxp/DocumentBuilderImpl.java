/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.ddom.jaxp;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.google.code.ddom.dom.impl.CommentFilterStreamReader;
import com.google.code.ddom.dom.impl.DOMImplementationImpl;
import com.google.code.ddom.dom.impl.DOMNodeFactory;
import com.google.code.ddom.dom.impl.DocumentImpl;

public class DocumentBuilderImpl extends DocumentBuilder {
    private final boolean ignoreComments;
    private final Map<String,Object> xmlInputFactoryProperties;
    private ErrorHandler errorHandler;

    DocumentBuilderImpl(boolean ignoreComments, Map<String,Object> xmlInputFactoryProperties) {
        this.ignoreComments = ignoreComments;
        this.xmlInputFactoryProperties = xmlInputFactoryProperties;
    }
    
    @Override
    public DOMImplementation getDOMImplementation() {
        // TODO: check if this is consistent with the rest of the code
        return new DOMImplementationImpl(new DOMNodeFactory());
    }

    @Override
    public boolean isNamespaceAware() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isValidating() {
        return (Boolean)xmlInputFactoryProperties.get(XMLInputFactory.IS_VALIDATING);
    }

    @Override
    public Document newDocument() {
        return new DocumentImpl(null);
    }

    @Override
    public Document parse(InputSource is) throws SAXException, IOException {
        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            for (Map.Entry<String,Object> prop : xmlInputFactoryProperties.entrySet()) {
                xmlInputFactory.setProperty(prop.getKey(), prop.getValue());
            }
            XMLStreamReader reader;
            boolean isByteStream;
            if (is.getByteStream() != null) {
                reader = xmlInputFactory.createXMLStreamReader(is.getSystemId(), is.getByteStream());
                isByteStream = true;
            } else if (is.getCharacterStream() != null) {
                reader = xmlInputFactory.createXMLStreamReader(is.getSystemId(), is.getCharacterStream());
                isByteStream = false;
            } else {
                reader = xmlInputFactory.createXMLStreamReader(new StreamSource(is.getSystemId()));
                isByteStream = true;
            }
            if (ignoreComments) {
                reader = new CommentFilterStreamReader(reader);
            }
            String encoding = reader.getEncoding();
            String xmlEncoding = reader.getCharacterEncodingScheme();
            DocumentImpl document = new DocumentImpl(reader);
//            DocumentImpl document = new DocumentImpl(domImplementation, omDocument/*, namespaceAware */);
            document.setDocumentURI(is.getSystemId());
            String inputEncoding = is.getEncoding();
            if (isByteStream && inputEncoding == null) {
                if (encoding.equals("UTF-16BE") || encoding.equals("UTF-16LE")) {
                    inputEncoding = encoding;
                } else {
                    inputEncoding = "UTF-8";
                }
            }
            document.setInputEncoding(inputEncoding);
            document.setXmlEncoding(xmlEncoding);
            // TODO: build the document and close the reader
            return document;
        } catch (XMLStreamException ex) {
            throw toSAXParseException(ex);
        }
    }

    private SAXParseException toSAXParseException(XMLStreamException ex) {
        Location location = ex.getLocation();
        return new SAXParseException(ex.getMessage(), location.getPublicId(),
                location.getSystemId(), location.getLineNumber(), location.getColumnNumber());
    }
    
    @Override
    public void setEntityResolver(EntityResolver er) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
