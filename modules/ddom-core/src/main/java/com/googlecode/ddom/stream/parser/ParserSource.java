/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
package com.googlecode.ddom.stream.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.xml.sax.InputSource;

import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;

public final class ParserSource implements XmlSource {
    private final XmlSource delegate;
    
    public ParserSource(InputSource is) {
        this(is, true);
    }
    
    public ParserSource(InputSource is, final boolean namespaceAware) {
        InputStream byteStream = is.getByteStream();
        Reader characterStream = is.getCharacterStream();
        if (byteStream != null) {
            delegate = new SimpleXmlSource(new Parser(byteStream, is.getEncoding(), namespaceAware));
        } else if (characterStream != null) {
            delegate = new SimpleXmlSource(new Parser(characterStream, namespaceAware));
        } else {
            final String systemId = is.getSystemId();
            delegate = new XmlSource() {
                public XmlInput getInput(Hints hints) throws StreamException {
                    try {
                        URLConnection connection = new URL(systemId).openConnection();
                        // TODO: extract encoding from content type
                        return new Parser(connection.getInputStream(), null, namespaceAware);
                    } catch (IOException ex) {
                        throw new StreamException("Unable to open " + systemId, ex);
                    }
                }
                
                public boolean isDestructive() {
                    return false;
                }
            };
        }
    }

    public XmlInput getInput(Hints hints) throws StreamException {
        return delegate.getInput(hints);
    }

    public boolean isDestructive() {
        return delegate.isDestructive();
    }
}
