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
import org.w3c.dom.Node;

import com.google.code.ddom.core.CoreNamespaceDeclaration;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.dom.intf.DOMNamespaceDeclaration;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;

@Mixin(CoreNamespaceDeclaration.class)
public abstract class NamespaceDeclarationSupport implements DOMNamespaceDeclaration {
    public final boolean isId() {
        return false;
    }

    public final Node shallowClone() {
        return (Node)coreGetDocument().coreCreateNamespaceDeclaration(coreGetDeclaredPrefix(), null);
    }

    public final String getNamespaceURI() {
        return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
    }

    public final String getPrefix() {
        return coreGetDeclaredPrefix() == null ? null : XMLConstants.XMLNS_ATTRIBUTE;
    }

    public final void setPrefix(String prefix) throws DOMException {
        // Other DOM implementations allow changing the prefix, but this means that a namespace
        // declaration is transformed into a normal attribute. We don't support this.
        throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
    }

    public final String getLocalName() {
        String declaredPrefix = coreGetDeclaredPrefix();
        return declaredPrefix == null ? XMLConstants.XMLNS_ATTRIBUTE : declaredPrefix;
    }

    public final String getName() {
        String declaredPrefix = coreGetDeclaredPrefix();
        if (declaredPrefix == null) {
            return XMLConstants.XMLNS_ATTRIBUTE;
        } else {
            return XMLConstants.XMLNS_ATTRIBUTE + ":" + declaredPrefix;
        }
    }
}
