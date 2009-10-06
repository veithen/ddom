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
package com.google.code.ddom.utils.dom.iterator;

import java.util.Iterator;

import org.w3c.dom.Element;

public class ElementNameFilterIterator extends FilterIterator<Element> {
    private final String namespaceURI;
    private final String localName;

    public ElementNameFilterIterator(Iterator<Element> parent, String namespaceURI, String localName) {
        super(parent);
        if (localName == null) {
            throw new IllegalArgumentException("localName can't be null");
        }
        this.namespaceURI = namespaceURI;
        this.localName = localName;
    }

    @Override
    protected boolean matches(Element element) {
        return (namespaceURI == null && element.getNamespaceURI() == null
                    || namespaceURI != null && namespaceURI.equals(element.getNamespaceURI()))
                && localName.equals(element.getLocalName());
    }
}
