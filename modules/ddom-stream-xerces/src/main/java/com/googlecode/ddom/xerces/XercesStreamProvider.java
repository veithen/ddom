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
package com.googlecode.ddom.xerces;

import java.io.IOException;
import java.io.InputStream;

import com.google.code.ddom.OptionsTracker;
import com.googlecode.ddom.spi.Provider;
import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlOutput;
import com.googlecode.ddom.stream.XmlSource;
import com.googlecode.ddom.stream.spi.StreamProvider;
import com.googlecode.ddom.xerces.xni.parser.XMLInputSource;

@Provider(name="xerces")
public class XercesStreamProvider implements StreamProvider {
    public XmlSource getSource(Object object, OptionsTracker options, boolean preserve) throws StreamException {
        try {
            if (object instanceof InputStream) {
                InputStream in = (InputStream)object;
                return new SimpleXmlSource(new XercesInput(new XMLInputSource(null, null, null, in, null)));
            } else {
                return null;
            }
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public XmlOutput getOutput(Object destination, OptionsTracker options) throws StreamException {
        return null;
    }

    public <T> T getSerializer(Class<T> serializerType, XmlOutput output, OptionsTracker options) {
        return null;
    }
}
