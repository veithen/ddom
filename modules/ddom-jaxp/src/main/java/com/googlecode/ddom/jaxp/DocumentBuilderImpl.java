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
package com.googlecode.ddom.jaxp;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.google.code.ddom.Options;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.stream.LocationAwareStreamException;
import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.StreamFactory;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;

public class DocumentBuilderImpl extends DocumentBuilder {
    private static final StreamFactory streamFactory = StreamFactory.getInstance(DocumentBuilderImpl.class.getClassLoader());
    
    private final Model model;
    private final Options options;
    private final boolean ignoringComments;
    private ErrorHandler errorHandler;

    DocumentBuilderImpl(Model model, Options options, boolean ignoringComments) {
        this.model = model;
        this.options = options;
        this.ignoringComments = ignoringComments;
    }
    
    @Override
    public DOMImplementation getDOMImplementation() {
        return (DOMImplementation)model.getAPIObjectFactory().getAPIObject(DOMImplementation.class);
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
        return (Document)model.getNodeFactory().createDocument();
    }

    @Override
    public Document parse(InputSource is) throws SAXException, IOException {
        // TODO: catch StreamException/DeferredParsingException and translate to SAXException
        XmlSource source;
        try {
            source = streamFactory.getSource(is, options, false);
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
        if (ignoringComments) {
            // We build the tree anyway, so we can't take advantage of the non-destructiveness anyway
            XmlInput input = source.getInput();
            input.addFilter(new CommentFilter());
            source = new SimpleXmlSource(input);
        }
        CoreDocument document = model.getNodeFactory().createDocument();
        document.coreSetContent(source);
        try {
            document.coreBuild();
        } catch (DeferredParsingException ex) {
            throw toSAXException(ex.getStreamException());
        }
        // TODO: close the reader and the underlying stream
        return (Document)document;
    }

    private SAXException toSAXException(StreamException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof SAXException) {
            return (SAXException)cause;
        } else if (ex instanceof LocationAwareStreamException) {
            LocationAwareStreamException ex2 = (LocationAwareStreamException)ex;
            return new SAXParseException(ex2.getMessage(), ex2.getPublicId(),
                    ex2.getSystemId(), ex2.getLineNumber(), ex2.getColumnNumber());
        } else {
            return new SAXException(ex);
        }
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
