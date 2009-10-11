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
package com.google.code.ddom.dom.impl;

import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.code.ddom.core.model.*;
import com.google.code.ddom.utils.dom.iterator.DescendantsIterator;
import com.google.code.ddom.utils.dom.iterator.ElementLocalNameFilterIterator;
import com.google.code.ddom.utils.dom.iterator.ElementNameFilterIterator;
import com.google.code.ddom.utils.dom.iterator.ElementNamespaceFilterIterator;
import com.google.code.ddom.utils.dom.iterator.FilterIterator;

public aspect GetElementsBy {
    public final int DocumentImpl.getStructureVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    public final NodeList ParentNodeImpl.getElementsByTagName(final String tagname) {
        return new ElementsBy(getDocument()) {
            @Override
            protected Iterator<Element> createIterator() {
                Iterator<Element> iterator = new DescendantsIterator<Element>(Element.class, ParentNodeImpl.this);
                if (tagname.equals("*")) {
                    return iterator;
                } else {
                    return new FilterIterator<Element>(iterator) {
                        @Override
                        protected boolean matches(Element element) {
                            return tagname.equals(element.getTagName());
                        }
                    };
                }
            }
        };
    }

    public final NodeList ParentNodeImpl.getElementsByTagNameNS(final String namespaceURI, final String localName) {
        return new ElementsBy(getDocument()) {
            @Override
            protected Iterator<Element> createIterator() {
                boolean nsWildcard = "*".equals(namespaceURI);
                boolean localNameWildcard = localName.equals("*");
                Iterator<Element> iterator = new DescendantsIterator<Element>(Element.class, ParentNodeImpl.this);
                if (nsWildcard && localNameWildcard) {
                    return iterator;
                } else if (nsWildcard) {
                    return new ElementLocalNameFilterIterator(iterator, localName);
                } else if (localNameWildcard) {
                    return new ElementNamespaceFilterIterator(iterator, namespaceURI);
                } else {
                    return new ElementNameFilterIterator(iterator, namespaceURI, localName);
                }
            }
        };
    }
}
