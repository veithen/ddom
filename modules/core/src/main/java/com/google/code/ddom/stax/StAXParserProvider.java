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
package com.google.code.ddom.stax;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.xml.sax.InputSource;

import com.google.code.ddom.spi.parser.ParseException;
import com.google.code.ddom.spi.parser.Parser;
import com.google.code.ddom.spi.parser.ParserProvider;

public class StAXParserProvider implements ParserProvider {
    public Parser getParser(Object source, Map<String,Object> properties) throws ParseException {
        XMLStreamReader reader;
        try {
            if (source instanceof XMLStreamReader) {
                // TODO: we shouldn't allow this because we don't have control over the properties of the XMLStreamReader
                reader = (XMLStreamReader)source;
            } else if (source instanceof InputSource) {
                XMLInputFactory factory = getFactory(properties);
                InputSource is = (InputSource)source;
                if (is.getByteStream() != null) {
                    // TODO: recover system ID
                    reader = factory.createXMLStreamReader(is.getByteStream(), is.getEncoding());
                } else if (is.getCharacterStream() != null) {
                    // TODO: recover system ID
                    reader = factory.createXMLStreamReader(is.getCharacterStream());
                } else {
                    // TODO
                    throw new UnsupportedOperationException();
                }
            } else if (source instanceof InputStream) {
                reader = getFactory(properties).createXMLStreamReader((InputStream)source);
            } else if (source instanceof Reader) {
                reader = getFactory(properties).createXMLStreamReader((Reader)source);
            } else {
                // TODO: support for File, URL, StreamSource, DataSource (with charset encoding detection), DataHandler, etc.
                reader = null;
            }
        } catch (XMLStreamException ex) {
            throw new ParseException(ex);
        }
        if (reader != null) {
            return new StAXParser(reader);
        } else {
            return null;
        }
    }
    
    private XMLInputFactory getFactory(Map<String,Object> properties) {
        // TODO: we should have something to distinguish properties that we must understand from properties that are provider specific
        XMLInputFactory factory = XMLInputFactory.newInstance();
        
        // TODO: apply properties
        
        return factory;
    }
}
