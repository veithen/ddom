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

import com.google.code.ddom.spi.model.AttributeMatcher;
import com.google.code.ddom.spi.model.CoreAttribute;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreNamespaceDeclaration;
import com.google.code.ddom.spi.model.NodeFactory;

public class DOMNamespaceDeclarationMatcher implements AttributeMatcher {
    public static final DOMNamespaceDeclarationMatcher INSTANCE = new DOMNamespaceDeclarationMatcher();
    
    private DOMNamespaceDeclarationMatcher() {}
    
    public boolean matches(CoreAttribute attr, String namespaceURI, String localName) {
        if (attr instanceof CoreNamespaceDeclaration) {
            String prefix = ((CoreNamespaceDeclaration)attr).getDeclaredPrefix();
            return localName == null && prefix == null || localName != null && localName.equals(prefix);
        } else {
            return false;
        }
    }

    public CoreAttribute createAttribute(NodeFactory factory, CoreDocument document, String namespaceURI, String localName, String prefix, String value) {
        // TODO: documentation here (localName instead of prefix is not a mistake...)
        return factory.createNSDecl(document, localName, value);
    }
}
