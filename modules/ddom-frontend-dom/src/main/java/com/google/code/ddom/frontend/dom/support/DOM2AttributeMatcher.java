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

import com.google.code.ddom.frontend.dom.intf.DOMAttribute;
import com.google.code.ddom.spi.model.AttributeMatcher;
import com.google.code.ddom.spi.model.CoreAttribute;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreTypedAttribute;
import com.google.code.ddom.spi.model.NodeFactory;

public class DOM2AttributeMatcher implements AttributeMatcher {
    public static final DOM2AttributeMatcher INSTANCE = new DOM2AttributeMatcher();
    
    private DOM2AttributeMatcher() {}
    
    public boolean matches(CoreAttribute _attr, String namespaceURI, String localName) {
        DOMAttribute attr = (DOMAttribute)_attr;
        return attr instanceof CoreTypedAttribute
                && (namespaceURI == null && attr.getNamespaceURI() == null
                        || namespaceURI != null && namespaceURI.equals(attr.getNamespaceURI()))
                && localName.equals(attr.getLocalName());
    }

    public CoreAttribute createAttribute(NodeFactory factory, CoreDocument document, String namespaceURI, String localName, String prefix, String value) {
        return factory.createAttribute(document, namespaceURI, localName, prefix, value, null);
    }
}
