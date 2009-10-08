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
package com.google.code.ddom.stream.sax;

import java.io.IOException;

import javax.xml.transform.sax.SAXSource;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.google.code.ddom.spi.stream.Consumer;
import com.google.code.ddom.spi.stream.Producer;
import com.google.code.ddom.spi.stream.StreamException;

public class SAXSourceProducer implements Producer {
    private final SAXSource source;

    public SAXSourceProducer(SAXSource source) {
        this.source = source;
    }

    public void proceed(Consumer consumer) throws StreamException {
        XMLReader xmlReader = source.getXMLReader();
        xmlReader.setContentHandler(new ConsumerContentHandler(consumer));
        try {
            xmlReader.parse(source.getInputSource());
        } catch (IOException ex) {
            throw new StreamException(ex);
        } catch (SAXException ex) {
            throw new StreamException(ex);
        }
    }

    public void dispose() {
        // TODO: should we do something with the InputSource here?
    }
}
