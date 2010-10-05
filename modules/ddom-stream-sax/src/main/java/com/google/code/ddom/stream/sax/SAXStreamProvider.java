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

import javax.xml.transform.sax.SAXSource;

import com.google.code.ddom.OptionsTracker;
import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.stream.spi.Output;
import com.google.code.ddom.stream.spi.Input;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamProvider;

@Provider(name="sax")
public class SAXStreamProvider implements StreamProvider {
    public Input getInput(Object source, OptionsTracker options, boolean preserve) throws StreamException {
        if (source instanceof SAXSource) {
            return new SAXInput((SAXSource)source);
        } else {
            return null;
        }
    }

    public Output getOutput(Object destination, OptionsTracker options) throws StreamException {
        // TODO build a Consumer that wraps a ContentHandler
        return null;
    }

    public <T> T getSerializer(Class<T> serializerType, Output output, OptionsTracker options) {
        if (serializerType.equals(ContentHandler.class)) {
            return serializerType.cast(new OutputContentHandler(output));
        } else {
            return null;
        }
    }
}
