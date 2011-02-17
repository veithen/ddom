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
package com.google.code.ddom.stream.wstx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;

import com.ctc.wstx.api.ReaderConfig;
import com.ctc.wstx.io.DefaultInputResolver;
import com.ctc.wstx.io.InputBootstrapper;
import com.ctc.wstx.io.ReaderBootstrapper;
import com.ctc.wstx.io.StreamBootstrapper;
import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.ctc.wstx.util.URLUtil;
import com.google.code.ddom.stream.stax.StAXInput;
import com.google.code.ddom.stream.stax.StAXOutput;
import com.googlecode.ddom.spi.Provider;
import com.googlecode.ddom.stream.OptionsTracker;
import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlOutput;
import com.googlecode.ddom.stream.XmlSource;
import com.googlecode.ddom.stream.options.CoalescingFeature;
import com.googlecode.ddom.stream.options.EntityReferencePolicy;
import com.googlecode.ddom.stream.options.NamespaceAwareness;
import com.googlecode.ddom.stream.options.ValidationPolicy;
import com.googlecode.ddom.stream.spi.StreamProvider;

@Provider(name="woodstox")
public class WoodstoxStreamProvider implements StreamProvider {
    public XmlSource getSource(Object object, OptionsTracker options, boolean preserve) throws StreamException {
        // TODO: who actually closes the streams???
        InputStream byteStream;
        Reader characterStream;
        String systemId;
        URL url;
        String encoding;
        String publicId;
        if (object instanceof InputStream) {
            byteStream = (InputStream)object;
            characterStream = null;
            systemId = null;
            url = null;
            encoding = null;
            publicId = null;
        } else if (object instanceof Reader) {
            characterStream = (Reader)object;
            byteStream = null;
            systemId = null;
            url = null;
            encoding = null;
            publicId = null;
        } else if (object instanceof InputSource) {
            InputSource is = (InputSource)object;
            byteStream = is.getByteStream();
            characterStream = is.getCharacterStream();
            systemId = is.getSystemId();
            encoding = is.getEncoding();
            publicId = is.getPublicId();
            url = null;
            if (byteStream == null && characterStream == null && systemId == null) {
                throw new StreamException("Invalid InputSource object");
            }
        } else if (object instanceof URL) {
            byteStream = null;
            characterStream = null;
            systemId = null;
            url = (URL)object;
            encoding = null;
            publicId = null;
        } else if (object instanceof StreamSource) {
            StreamSource source = (StreamSource)object;
            byteStream = source.getInputStream();
            characterStream = source.getReader();
            systemId = source.getSystemId();
            url = null;
            encoding = null;
            publicId = source.getPublicId();
        } else {
            return null;
        }
        // TODO: support for File, StreamSource, DataSource (with charset encoding detection), DataHandler, etc.
        
        if (url == null && systemId != null) {
            try {
                url = URLUtil.urlFromSystemId(systemId);
            } catch (IOException ex) {
                throw new StreamException("Invalid system ID " + systemId, ex);
            }
        } else if (systemId == null && url != null) {
            systemId = url.toExternalForm();
        }
        if (byteStream == null && characterStream == null && url != null) {
            try {
                byteStream = URLUtil.inputStreamFromURL(url);
            } catch (IOException ex) {
                throw new StreamException("Can't open stream for URL " + url, ex);
            }
        }
        
        WstxInputFactory factory = new WstxInputFactory();
        ReaderConfig config = factory.createPrivateConfig();
        // TODO: allow to set the base configuration through configureForXXX
        config.doSupportNamespaces(options.getAndMarkAsProcessed(NamespaceAwareness.class) != NamespaceAwareness.DISABLE);
        config.doCoalesceText(options.getAndMarkAsProcessed(CoalescingFeature.class) == CoalescingFeature.ENABLE);
        config.doReplaceEntityRefs(options.getAndMarkAsProcessed(EntityReferencePolicy.class) != EntityReferencePolicy.DONT_EXPAND);
        config.doValidateWithDTD(options.getAndMarkAsProcessed(ValidationPolicy.class) == ValidationPolicy.ENABLE);
        InputBootstrapper bs;
        if (characterStream == null && encoding != null) {
            // Switch to reader since we know the encoding
            try {
                // See WstxInputFactory for more information about this piece of code
                characterStream = DefaultInputResolver.constructOptimizedReader(config, byteStream, false, encoding);
            } catch (XMLStreamException ex) {
                // This should only occur if the encoding is unsupported
                throw new StreamException(ex.getCause());
            }
        }
        if (characterStream != null) {
            bs = ReaderBootstrapper.getInstance(publicId, systemId, characterStream, encoding);
        } else {
            bs = StreamBootstrapper.getInstance(publicId, systemId, byteStream);
        }
        XMLStreamReader reader;
        try {
            // The method actually returns a BasicStreamReader if that matters
            reader = factory.createSR(config, systemId, bs, false, false);
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
        return new SimpleXmlSource(new StAXInput(reader, config.getSymbols()));
    }
    
    public XmlOutput getOutput(Object destination, OptionsTracker options) throws StreamException {
        try {
            if (destination instanceof OutputStream) {
                WstxOutputFactory factory = new WstxOutputFactory();
                return new StAXOutput(factory.createXMLStreamWriter((OutputStream)destination));
            } else if (destination instanceof Writer) {
                WstxOutputFactory factory = new WstxOutputFactory();
                return new StAXOutput(factory.createXMLStreamWriter((Writer)destination));
            } else {
                return null;
            }
        } catch (XMLStreamException ex) {
            throw new StreamException(ex);
        }
    }

    public <T> T getSerializer(Class<T> serializerType, XmlOutput output, OptionsTracker options) {
        // TODO
        return null;
    }
}
