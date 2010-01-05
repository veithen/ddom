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

import javax.xml.stream.XMLStreamReader;

import com.google.code.ddom.CommentPolicy;
import com.google.code.ddom.OptionsProcessor;
import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.stream.spi.Consumer;
import com.google.code.ddom.stream.spi.Producer;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamProvider;

@Provider(name="stax")
public class StAXStreamProvider implements StreamProvider {
    public Producer getProducer(Object source, OptionsProcessor options, boolean preserve) throws StreamException {
        if (source instanceof XMLStreamReader) {
            XMLStreamReader reader = (XMLStreamReader)source;
            if (options.getAndMarkAsProcessed(CommentPolicy.class) == CommentPolicy.REMOVE) {
                reader = new CommentFilterStreamReader(reader);
            }
            // TODO: implement canonicalization!
            return new StAXParser(reader, null);
        } else {
            return null;
        }
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
