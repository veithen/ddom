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
package com.google.code.ddom.stream.wstx;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.xml.sax.InputSource;

import com.ctc.wstx.api.ReaderConfig;
import com.ctc.wstx.io.InputBootstrapper;
import com.ctc.wstx.io.ReaderBootstrapper;
import com.ctc.wstx.io.StreamBootstrapper;
import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.util.URLUtil;
import com.google.code.ddom.CommentPolicy;
import com.google.code.ddom.NamespaceAwareness;
import com.google.code.ddom.OptionsProcessor;
import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.stream.spi.Consumer;
import com.google.code.ddom.stream.spi.Producer;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamProvider;
import com.google.code.ddom.stream.stax.CommentFilterStreamReader;
import com.google.code.ddom.stream.stax.StAXParser;

@Provider(name="woodstox")
public class WoodstoxStreamProvider implements StreamProvider {
    public Producer getProducer(Object source, OptionsProcessor options, boolean preserve) throws StreamException {
        // TODO: who actually closes the streams???
        InputStream byteStream = null;
        Reader characterStream = null;
        String systemId = null;
        String encoding = null;
        String publicId = null;
        if (source instanceof InputStream) {
            byteStream = (InputStream)source;
        } else if (source instanceof Reader) {
            characterStream = (Reader)source;
        } else if (source instanceof InputSource) {
            InputSource is = (InputSource)source;
            byteStream = is.getByteStream();
            characterStream = is.getCharacterStream();
            systemId = is.getSystemId();
            encoding = is.getEncoding();
            publicId = is.getPublicId();
            if (byteStream == null && characterStream == null) {
                if (systemId == null) {
                    throw new StreamException("Invalid InputSource object");
                } else {
                    try {
                        byteStream = URLUtil.inputStreamFromURL(URLUtil.urlFromSystemId(systemId));
                    } catch (IOException ex) {
                        throw new StreamException("Can't open stream for system ID " + systemId, ex);
                    }
                }
            }
        }
        // TODO: support for File, URL, StreamSource, DataSource (with charset encoding detection), DataHandler, etc.
        if (byteStream == null && characterStream == null) {
            return null;
        } else {
            InputBootstrapper bs;
            if (byteStream != null) {
                // TODO: where do we pass the encoding?!?
                bs = StreamBootstrapper.getInstance(publicId, systemId, byteStream);
            } else {
                bs = ReaderBootstrapper.getInstance(publicId, systemId, characterStream, encoding);
            }
            WstxInputFactory factory = new WstxInputFactory();
            ReaderConfig config = factory.createPrivateConfig();
            // TODO: allow to set the base configuration through configureForXXX
            config.doSupportNamespaces(options.getAndMarkAsProcessed(NamespaceAwareness.class) != NamespaceAwareness.DISABLE);
            XMLStreamReader reader;
            try {
                // The method actually returns a BasicStreamReader if that matters
                reader = factory.createSR(config, systemId, bs, false, false);
            } catch (XMLStreamException ex) {
                throw new StreamException(ex);
            }
            if (options.getAndMarkAsProcessed(CommentPolicy.class) == CommentPolicy.REMOVE) {
                reader = new CommentFilterStreamReader(reader);
            }
            return new StAXParser(reader);
        }
    }
    
    public Consumer getConsumer(Object destination, OptionsProcessor options) throws StreamException {
        // TODO
        return null;
    }

    public <T> T getSerializer(Class<T> serializerType, Consumer consumer, OptionsProcessor options) {
        // TODO
        return null;
    }
}
