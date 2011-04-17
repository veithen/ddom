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
package com.googlecode.ddom.jaxp;

import java.util.SortedMap;
import java.util.TreeMap;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.filter.util.Attribute;
import com.googlecode.ddom.stream.filter.util.AttributeBuffer;
import com.googlecode.ddom.stream.filter.util.XmlHandlerWrapper;

final class AttributeReorderingFilterHandler extends XmlHandlerWrapper {
    private final AttributeBuffer attributes = new AttributeBuffer();
    private final SortedMap<String,Attribute> sortedAttributes = new TreeMap<String,Attribute>();
    
    AttributeReorderingFilterHandler(XmlHandler parent) {
        super(parent);
    }

    @Override
    public void startAttribute(String name, String type) throws StreamException {
        sortedAttributes.put(name, attributes.startAttribute(name, type));
    }

    @Override
    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        sortedAttributes.put(prefix.length() == 0 ? localName : (prefix + ":" + localName),
                attributes.startAttribute(namespaceURI, localName, prefix, type));
    }

    @Override
    public void startNamespaceDeclaration(String prefix) throws StreamException {
        sortedAttributes.put(prefix.length() == 0 ? "xmlns" : ("xmlns:" + prefix),
                attributes.startNamespaceDeclaration(prefix));
    }

    @Override
    public void endAttribute() throws StreamException {
        attributes.endAttribute();
    }

    @Override
    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        if (attributes.isInAttribute()) {
            attributes.processCharacterData(data);
        } else {
            super.processCharacterData(data, ignorable);
        }
    }

    @Override
    public void attributesCompleted() throws StreamException {
        for (Attribute attribute : sortedAttributes.values()) {
            attribute.serialize(getParent());
        }
        attributes.clear();
        sortedAttributes.clear();
        super.attributesCompleted();
    }
}
