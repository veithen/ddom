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
package com.googlecode.ddom.stream.dom.spi;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.googlecode.ddom.spi.Provider;
import com.googlecode.ddom.stream.OptionsTracker;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlOutput;
import com.googlecode.ddom.stream.XmlSource;
import com.googlecode.ddom.stream.dom.DOMOutput;
import com.googlecode.ddom.stream.dom.DOMSource;
import com.googlecode.ddom.stream.spi.StreamProvider;

@Provider(name="dom")
public class DOMStreamProvider implements StreamProvider {
    public XmlSource getSource(Object object, OptionsTracker options, boolean preserve) throws StreamException {
        if (object instanceof Node) {
            return new DOMSource((Node)object);
        } else if (object instanceof javax.xml.transform.dom.DOMSource) {
            return new DOMSource(((javax.xml.transform.dom.DOMSource)object).getNode());
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
