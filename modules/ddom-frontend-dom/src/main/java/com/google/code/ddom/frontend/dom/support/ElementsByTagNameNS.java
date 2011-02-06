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
package com.google.code.ddom.frontend.dom.support;

import java.util.Iterator;

import org.w3c.dom.Element;

import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.google.code.ddom.frontend.dom.intf.DOMElement;
import com.google.code.ddom.frontend.dom.intf.DOMParentNode;
import com.googlecode.ddom.core.Axis;

// TODO: clean this up
public class ElementsByTagNameNS extends ElementsBy {
    private final DOMParentNode node;
    private final String namespaceURI;
    private final String localName;
    
    public ElementsByTagNameNS(DOMDocument document, DOMParentNode node, String namespaceURI,
            String localName) {
        super(document);
        this.node = node;
        this.namespaceURI = namespaceURI == null ? "" : namespaceURI;
        this.localName = localName;
    }

    @Override
    protected Iterator<Element> createIterator() {
        boolean nsWildcard = "*".equals(namespaceURI);
        boolean localNameWildcard = localName.equals("*");
        if (nsWildcard && localNameWildcard) {
            // TODO: there seems to be no unit test checking whether the iterator should return DOM1 elements!
            return (Iterator)node.coreGetChildrenByType(Axis.DESCENDANTS, DOMElement.class);
        } else if (nsWildcard) {
            return (Iterator)node.coreGetElementsByLocalName(Axis.DESCENDANTS, localName);
        } else if (localNameWildcard) {
            return (Iterator)node.coreGetElementsByNamespace(Axis.DESCENDANTS, namespaceURI);
        } else {
            // TODO: handle the cast problem properly
            return (Iterator)node.coreGetElementsByName(Axis.DESCENDANTS, namespaceURI, localName);
//            return new ElementNameFilterIterator(iterator, namespaceURI, localName);
        }
    }
}
