/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.googlecode.ddom.xerces;

import java.io.IOException;
import java.io.InputStream;

import com.google.code.ddom.OptionsTracker;
import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.stream.spi.Input;
import com.google.code.ddom.stream.spi.Output;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamProvider;
import com.googlecode.ddom.xerces.xni.parser.XMLInputSource;

@Provider(name="xerces")
public class XercesStreamProvider implements StreamProvider {
    public Input getInput(Object source, OptionsTracker options, boolean preserve) throws StreamException {
        try {
            if (source instanceof InputStream) {
                InputStream in = (InputStream)source;
                return new XercesInput(new XMLInputSource(null, null, null, in, null));
            } else {
                return null;
            }
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public Output getOutput(Object destination, OptionsTracker options) throws StreamException {
        return null;
    }

    public <T> T getSerializer(Class<T> serializerType, Output output, OptionsTracker options) {
        return null;
    }
}
