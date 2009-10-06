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
import org.w3c.dom.Node;

import com.google.code.ddom.spi.model.DOMDocument;
import com.google.code.ddom.spi.model.NamespaceDeclaration;

public class NSDecl extends DOMAttributeImpl implements NamespaceDeclaration {
    private String declaredPrefix;

    public NSDecl(DOMDocument document, String prefix, String namespaceURI) {
        super(document, namespaceURI);
        this.declaredPrefix = prefix;
    }

    public final String getDeclaredPrefix() {
        return declaredPrefix;
    }
    
    public final String getDeclaredNamespaceURI() {
        return getValue();
    }
    
    public final String getNamespaceURI() {
        return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
    }

    public final String getPrefix() {
        return declaredPrefix == null ? null : XMLConstants.XMLNS_ATTRIBUTE;
    }

    public void setPrefix(String prefix) throws DOMException {
        // Other DOM implementations allow changing the prefix, but this means that a namespace
        // declaration is transformed into a normal attribute. We don't support this.
        throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
    }

    public final String getLocalName() {
        return declaredPrefix == null ? XMLConstants.XMLNS_ATTRIBUTE : declaredPrefix;
    }

    public final String getName() {
        if (declaredPrefix == null) {
            return XMLConstants.XMLNS_ATTRIBUTE;
        } else {
            return XMLConstants.XMLNS_ATTRIBUTE + ":" + declaredPrefix;
        }
    }

    public final boolean isId() {
        return false;
    }

    @Override
    protected Node shallowClone() {
        // TODO Auto-generated method stub
        return null;
    }
}
