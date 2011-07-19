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
package com.googlecode.ddom.backend.linkedlist;

import com.googlecode.ddom.backend.Implementation;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.core.ClonePolicy;
import com.googlecode.ddom.core.CoreNamespaceDeclaration;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

// @Implementation
public class NamespaceDeclaration extends Attribute implements CoreNamespaceDeclaration {
    private String declaredPrefix;

    public NamespaceDeclaration(Document document, String prefix, String namespaceURI) {
        super(document, namespaceURI);
        this.declaredPrefix = prefix;
    }

    public NamespaceDeclaration(Document document, String prefix, boolean complete) {
        super(document, complete);
        this.declaredPrefix = prefix;
    }

    public final int coreGetNodeType() {
        return NAMESPACE_DECLARATION_NODE;
    }

    public final String coreGetDeclaredPrefix() {
        return declaredPrefix;
    }
    
    public final String coreGetDeclaredNamespaceURI() throws DeferredParsingException {
        return coreGetTextContent(TextCollectorPolicy.DEFAULT);
    }

    public final void internalGenerateStartEvent(XmlHandler handler) throws StreamException {
        handler.startNamespaceDeclaration(coreGetDeclaredPrefix());
    }

    @Override
    final LLParentNode shallowClone(ClonePolicy policy) {
        return new NamespaceDeclaration(null, declaredPrefix, true);
    }
}
