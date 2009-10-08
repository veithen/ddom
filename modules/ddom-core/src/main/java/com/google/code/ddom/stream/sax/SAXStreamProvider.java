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

import java.net.ContentHandler;
import java.util.Map;

import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.spi.stream.Consumer;
import com.google.code.ddom.spi.stream.Producer;
import com.google.code.ddom.spi.stream.StreamException;
import com.google.code.ddom.spi.stream.StreamProvider;

@Provider(name="sax")
public class SAXStreamProvider implements StreamProvider {
    public Producer getProducer(Object source, Map<String, Object> properties, boolean preserve) throws StreamException {
        // TODO build Producer for SAXSource here
        return null;
    }

    public Consumer getConsumer(Object destination, Map<String, Object> properties) throws StreamException {
        // TODO build a Consumer that wraps a ContentHandler
        return null;
    }

    public <T> T getSerializer(Class<T> serializerType, Consumer consumer, Map<String, Object> properties) {
        if (serializerType.equals(ContentHandler.class)) {
            return serializerType.cast(new ConsumerContentHandler(consumer));
        } else {
            return null;
        }
    }
}
