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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.Options;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.frontend.dom.support.DOMImplementationImpl;
import com.google.code.ddom.model.ModelBuilder;
import com.google.code.ddom.spi.model.ModelLoaderException;
import com.google.code.ddom.spi.model.ModelLoaderRegistry;

public class DocumentBuilderImpl extends DocumentBuilder {
    private final Options options;
    private ErrorHandler errorHandler;

    DocumentBuilderImpl(Options options) {
        this.options = options;
    }
    
    @Override
    public DOMImplementation getDOMImplementation() {
        // TODO: check if this is consistent with the rest of the code
        try {
            return new DOMImplementationImpl(ModelLoaderRegistry.getInstance().getDocumentFactory(ModelBuilder.buildModelDefinition("dom")));
        } catch (ModelLoaderException ex) {
            throw new RuntimeException(ex); // TODO
        }
    }

    @Override
    public boolean isNamespaceAware() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isValidating() {
        // TODO
        return false;
//        return (Boolean)xmlInputFactoryProperties.get(XMLInputFactory.IS_VALIDATING);
    }

    @Override
    public Document newDocument() {
        // TODO: do this properly
        return (Document)DocumentHelper.newInstance().newDocument("dom");
    }

    @Override
    public Document parse(InputSource is) throws SAXException, IOException {
        // TODO: catch StreamException/DeferredParsingException and translate to SAXException
        DocumentHelper documentHelper = DocumentHelper.newInstance();
        CoreDocument document = (CoreDocument)documentHelper.parse("dom", is, options);
        documentHelper.buildDocument(document);
        // TODO: close the reader and the underlying stream
        return (Document)document;
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
