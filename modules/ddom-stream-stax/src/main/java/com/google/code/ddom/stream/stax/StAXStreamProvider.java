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
package com.google.code.ddom.stream.stax;

import javax.xml.stream.XMLStreamReader;

import com.google.code.ddom.OptionsTracker;
import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.stream.options.CommentPolicy;
import com.google.code.ddom.stream.spi.StreamProvider;
import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlOutput;
import com.googlecode.ddom.stream.XmlSource;

@Provider(name="stax")
public class StAXStreamProvider implements StreamProvider {
    public XmlSource getSource(Object object, OptionsTracker options, boolean preserve) throws StreamException {
        if (object instanceof XMLStreamReader) {
            XMLStreamReader reader = (XMLStreamReader)object;
            if (options.getAndMarkAsProcessed(CommentPolicy.class) == CommentPolicy.REMOVE) {
                reader = new CommentFilterStreamReader(reader);
            }
            // TODO: implement canonicalization!
            return new SimpleXmlSource(new StAXInput(reader, null));
        } else {
            return null;
        }
    }
    
    public XmlOutput getOutput(Object destination, OptionsTracker options) throws StreamException {
        // TODO construct Consumer wrapping an XMLStreamWriter
        return null;
    }

    public <T> T getSerializer(Class<T> serializerType, XmlOutput output, OptionsTracker options) {
        // TODO support wrapping the consumer in an XMLStreamWriter
        return null;
    }
}
