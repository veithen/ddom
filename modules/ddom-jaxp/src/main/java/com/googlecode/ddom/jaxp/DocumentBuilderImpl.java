/*
 * Copyright 2009-2013 Andreas Veithen
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
import javax.xml.validation.Schema;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.frontend.dom.intf.DOMNodeFactory;
import com.googlecode.ddom.stream.LocationAwareStreamException;
import com.googlecode.ddom.stream.Options;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.StreamFactory;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;
import com.googlecode.ddom.stream.filter.CommentFilter;
import com.googlecode.ddom.stream.filter.ValidationFilter;

public class DocumentBuilderImpl extends DocumentBuilder {
    private static final StreamFactory streamFactory = StreamFactory.getInstance(DocumentBuilderImpl.class.getClassLoader());
    
    private final DOMNodeFactory nodeFactory;
    private final Options options;
    private final boolean ignoringComments;
    private final boolean sortAttributes;
    private final Schema schema;
    private ErrorHandler errorHandler;

    DocumentBuilderImpl(DOMNodeFactory nodeFactory, Options options, boolean ignoringComments, boolean sortAttributes, Schema schema) {
        this.nodeFactory = nodeFactory;
        this.options = options;
        this.ignoringComments = ignoringComments;
        this.sortAttributes = sortAttributes;
        this.schema = schema;
    }
    
    @Override
    public void reset() {
        errorHandler = null;
    }

    @Override
    public DOMImplementation getDOMImplementation() {
        return nodeFactory.getDOMImplementation();
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
    public Schema getSchema() {
        return schema;
    }

    @Override
    public void setEntityResolver(EntityResolver er) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public Document newDocument() {
        return (Document)nodeFactory.createDocument();
    }
    
    void applyFilters(XmlInput input) {
        if (ignoringComments) {
            input.addFilter(new CommentFilter());
        }
        if (schema != null) {
            ValidationFilter validationFilter = new ValidationFilter(schema);
            validationFilter.setErrorHandler(errorHandler);
            input.addFilter(validationFilter);
        }
        if (sortAttributes) {
            input.addFilter(new AttributeReorderingFilter());
        }
    }
    
    @Override
    public Document parse(InputSource is) throws SAXException, IOException {
        final XmlSource source;
        try {
            source = streamFactory.getSource(is, options, false);
        } catch (StreamException ex) {
            throw toSAXException(ex);
        }
        CoreDocument document = nodeFactory.createDocument();
        document.coreSetContent(new XmlSource() {
            public XmlInput getInput(Hints hints) throws StreamException {
                XmlInput input = source.getInput(hints);
                applyFilters(input);
                return input;
            }
            
            public boolean isDestructive() {
                return source.isDestructive();
            }
        });
        try {
            document.coreBuild();
        } catch (DeferredParsingException ex) {
            throw toSAXException(ex.getStreamException());
        } catch (DeferredBuildingException ex) {
            throw new SAXException(ex);
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
}
