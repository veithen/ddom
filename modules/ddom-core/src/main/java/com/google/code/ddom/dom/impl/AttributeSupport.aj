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

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import com.google.code.ddom.core.model.AttributeImpl;
import com.google.code.ddom.core.model.NamespaceDeclarationImpl;
import com.google.code.ddom.core.model.TypedAttributeImpl;

public aspect AttributeSupport {
    declare parents: AttributeImpl implements DOMAttribute;
    declare parents: TypedAttributeImpl implements DOMTypedAttribute;
    declare parents: NamespaceDeclarationImpl implements DOMNamespaceDeclaration;
    
    public final String DOMAttribute.getValue() {
        return coreGetValue();
    }
    
    public final void DOMAttribute.setValue(String value) throws DOMException {
        coreSetValue(value);
    }

    public final Element DOMAttribute.getOwnerElement() {
        return (Element)coreGetOwnerElement();
    }

    public final boolean DOMAttribute.getSpecified() {
        // TODO
        return true;
    }

    public final boolean DOMTypedAttribute.isId() {
        return "ID".equals(getType());
    }

    public final boolean DOMNamespaceDeclaration.isId() {
        return false;
    }
}
