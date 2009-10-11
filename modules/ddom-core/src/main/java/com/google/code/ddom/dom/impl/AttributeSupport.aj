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

public aspect AttributeSupport {
    declare parents: AttributeImpl implements DOMAttribute;
    
    public final String AttributeImpl.getValue() {
        return coreGetValue();
    }
    
    public final void AttributeImpl.setValue(String value) throws DOMException {
        coreSetValue(value);
    }

    public final Element AttributeImpl.getOwnerElement() {
        return (Element)coreGetOwnerElement();
    }

    public final boolean AttributeImpl.getSpecified() {
        // TODO
        return true;
    }

    public final boolean TypedAttributeImpl.isId() {
        return "ID".equals(getType());
    }

    public final boolean NSDecl.isId() {
        return false;
    }
}