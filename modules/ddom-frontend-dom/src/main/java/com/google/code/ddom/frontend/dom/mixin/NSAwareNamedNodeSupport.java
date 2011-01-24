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
package com.google.code.ddom.frontend.dom.mixin;

import javax.xml.XMLConstants;

import org.w3c.dom.DOMException;

import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.dom.intf.*;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;
import com.google.code.ddom.frontend.dom.support.NSUtil;
import com.googlecode.ddom.core.CoreNSAwareNamedNode;

@Mixin(CoreNSAwareNamedNode.class)
public abstract class NSAwareNamedNodeSupport implements DOMNSAwareNamedNode {
    public final String getNamespaceURI() {
        return coreGetNamespaceURI();
    }
    
    public final String getPrefix() {
        return coreGetPrefix();
    }
    
    public final void setPrefix(String prefix) throws DOMException {
        if (prefix == null || prefix.length() == 0) {
            coreSetPrefix(null);
        } else {
            String namespaceURI = getNamespaceURI();
            if (namespaceURI == null) {
                // The null namespace can't be bound to a prefix
                throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
            } else {
                NSUtil.validatePrefix(prefix);
                if (XMLConstants.XML_NS_PREFIX.equals(prefix) && !XMLConstants.XML_NS_URI.equals(namespaceURI)) {
                    throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
                }
                if (XMLConstants.XMLNS_ATTRIBUTE.equals(prefix) && !XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
                    throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
                }
                coreSetPrefix(prefix);
            }
        }
    }
    
    public final String getLocalName() {
        return coreGetLocalName();
    }

    public String internalGetName() {
        String prefix = getPrefix();
        String localName = getLocalName();
        if (prefix == null) {
            return localName;
        } else {
            return prefix + ":" + localName;
        }
    }
}
