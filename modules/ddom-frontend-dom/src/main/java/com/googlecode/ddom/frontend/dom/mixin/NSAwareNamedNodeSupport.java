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
package com.googlecode.ddom.frontend.dom.mixin;

import javax.xml.XMLConstants;

import org.w3c.dom.DOMException;

import com.googlecode.ddom.core.CoreNSAwareNamedNode;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.*;
import com.googlecode.ddom.frontend.dom.support.DOMExceptionUtil;
import com.googlecode.ddom.frontend.dom.support.NSUtil;

@Mixin(CoreNSAwareNamedNode.class)
public abstract class NSAwareNamedNodeSupport implements DOMNSAwareNamedNode {
    public final String getNamespaceURI() {
        String namespaceURI = coreGetNamespaceURI();
        return namespaceURI.length() == 0 ? null : namespaceURI;
    }
    
    public final String getPrefix() {
        String prefix = coreGetPrefix();
        return prefix.length() == 0 ? null : prefix;
    }
    
    public final void setPrefix(String prefix) throws DOMException {
        if (prefix == null || prefix.length() == 0) {
            coreSetPrefix("");
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