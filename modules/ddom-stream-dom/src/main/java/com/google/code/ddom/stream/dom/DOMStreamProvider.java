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
package com.google.code.ddom.stream.dom;

import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.code.ddom.OptionsTracker;
import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamProvider;
import com.google.code.ddom.stream.spi.XmlInput;
import com.google.code.ddom.stream.spi.XmlOutput;

@Provider(name="dom")
public class DOMStreamProvider implements StreamProvider {
    public XmlInput getInput(Object source, OptionsTracker options, boolean preserve) throws StreamException {
        if (source instanceof Node) {
            return new DOMInput((Node)source);
        } else if (source instanceof DOMSource) {
            return new DOMInput(((DOMSource)source).getNode());
        } else {
            return null;
        }
    }

    public XmlOutput getOutput(Object destination, OptionsTracker options) throws StreamException {
        // TODO: support DOMResult objects (and DocumentFragment and Element objects)
        if (destination instanceof Document) {
            return new DOMOutput((Document)destination);
        } else {
            return null;
        }
    }

    public <T> T getSerializer(Class<T> serializerType, XmlOutput output, OptionsTracker options) {
        // DOM doesn't define any serializers
        return null;
    }
}
