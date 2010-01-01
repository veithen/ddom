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
package com.google.code.ddom.stream.stax;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.xml.sax.InputSource;

import com.google.code.ddom.CommentPolicy;
import com.google.code.ddom.NamespaceAwareness;
import com.google.code.ddom.OptionsProcessor;
import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.stream.spi.Consumer;
import com.google.code.ddom.stream.spi.Producer;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamProvider;

@Provider(name="stax")
public class StAXStreamProvider implements StreamProvider {
    public Producer getProducer(Object source, OptionsProcessor options, boolean preserve) throws StreamException {
        XMLStreamReader reader;
        try {
            // TODO: most of the source types should be handled by a StreamProvider based on Woodstox's proprietary API
            // TODO: who actually closes the streams???
            if (source instanceof XMLStreamReader) {
                // TODO: we shouldn't allow this because we don't have control over the properties of the XMLStreamReader
                reader = (XMLStreamReader)source;
            } else if (source instanceof InputSource) {
                XMLInputFactory factory = getFactory(options);
                InputSource is = (InputSource)source;
                if (is.getByteStream() != null) {
                    // TODO: recover system ID
                    reader = factory.createXMLStreamReader(is.getByteStream(), is.getEncoding());
                } else if (is.getCharacterStream() != null) {
                    // TODO: recover system ID
                    reader = factory.createXMLStreamReader(is.getCharacterStream());
                } else {
                    String systemId = is.getSystemId();
                    URL url;
                    try {
                        url = new URL(systemId);
                    } catch (MalformedURLException ex) {
                        throw new StreamException(ex);
                    }
                    InputStream in;
                    try {
                        in = url.openStream();
                    } catch (IOException ex) {
                        throw new StreamException(ex);
                    }
                    reader = factory.createXMLStreamReader(systemId, in);
                }
            } else if (source instanceof InputStream) {
                reader = getFactory(options).createXMLStreamReader((InputStream)source);
            } else if (source instanceof Reader) {
                reader = getFactory(options).createXMLStreamReader((Reader)source);
            } else {
                // TODO: support for File, URL, StreamSource, DataSource (with charset encoding detection), DataHandler, etc.
                reader = null;
            }
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
        if (reader != null) {
            if (options.getAndMarkAsProcessed(CommentPolicy.class) == CommentPolicy.REMOVE) {
                reader = new CommentFilterStreamReader(reader);
            }
            return new StAXParser(reader);
        } else {
            return null;
        }
    }
    
    private XMLInputFactory getFactory(OptionsProcessor options) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE,
                options.getAndMarkAsProcessed(NamespaceAwareness.class) != NamespaceAwareness.DISABLE);
        return factory;
    }
    
    public Consumer getConsumer(Object destination, OptionsProcessor options) throws StreamException {
        // TODO construct Consumer wrapping an XMLStreamWriter
        return null;
    }

    public <T> T getSerializer(Class<T> serializerType, Consumer consumer, OptionsProcessor options) {
        // TODO support wrapping the consumer in an XMLStreamWriter
        return null;
    }
}
