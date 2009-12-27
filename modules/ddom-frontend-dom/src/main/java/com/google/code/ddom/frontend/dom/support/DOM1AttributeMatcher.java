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
package com.google.code.ddom.frontend.dom.support;

import com.google.code.ddom.backend.AttributeMatcher;
import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.frontend.dom.intf.DOMAttribute;

/**
 * {@link AttributeMatcher} implementation that matches attributes based on the qualified name.
 * Parameters are defined as follows:
 * <dl>
 * <dt><code>namespaceURI</code>
 * <dd>Not used.
 * <dt><code>name</code>
 * <dd>The qualified name of the attribute. This value may be in the form
 * <code>prefix:localName</code>.
 * <dt><code>value</code>
 * <dd>The attribute value.
 * <dt><code>prefix</code>
 * <dd>Not used.
 * </dl>
 * 
 * @author Andreas Veithen
 */
public class DOM1AttributeMatcher implements AttributeMatcher {
    public static final DOM1AttributeMatcher INSTANCE = new DOM1AttributeMatcher();
    
    private DOM1AttributeMatcher() {}
    
    public boolean matches(CoreAttribute attr, String namespaceURI, String name) {
        // Note: a lookup using DOM 1 methods may return any kind of attribute, including NSDecl
        return name.equals(((DOMAttribute)attr).getName());
    }

    public CoreAttribute createAttribute(NodeFactory factory, CoreDocument document, String namespaceURI, String name, String prefix, String value) {
        return factory.createAttribute(document, name, value, null);
    }

    public void update(CoreAttribute attr, String prefix, String value) {
        attr.coreSetValue(value);
    }
}