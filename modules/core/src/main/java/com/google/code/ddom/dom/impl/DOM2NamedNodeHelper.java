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

import javax.xml.XMLConstants;

import org.w3c.dom.DOMException;

import com.google.code.ddom.dom.model.DOM2NamedNode;

public class DOM2NamedNodeHelper {
    public static void setPrefix(DOM2NamedNode node, String prefix) throws DOMException {
        String namespaceURI = node.getNamespaceURI();
        if (namespaceURI == null) {
            throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
        } else {
            NSUtil.validatePrefix(prefix);
            if (XMLConstants.XML_NS_PREFIX.equals(prefix) && !XMLConstants.XML_NS_URI.equals(namespaceURI)) {
                throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
            }
            if (XMLConstants.XMLNS_ATTRIBUTE.equals(prefix) && !XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
                throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
            }
            node.internalSetPrefix(prefix);
        }
    }
    
    public static String getName(DOM2NamedNode node) {
        String prefix = node.getPrefix();
        String localName = node.getLocalName();
        if (prefix == null) {
            return localName;
        } else {
            return prefix + ":" + localName;
        }
    }
}
