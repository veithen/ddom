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
package com.google.code.ddom.backend.linkedlist;

import javax.xml.namespace.QName;

import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreNSAwareElement;
import com.google.code.ddom.backend.Implementation;

@Implementation
public class NSAwareElement extends Element implements CoreNSAwareElement {
    private final String namespaceURI;
    private String localName;
    private String prefix;

    public NSAwareElement(CoreDocument document, String namespaceURI, String localName, String prefix, boolean complete) {
        super(document, complete);
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.prefix = prefix;
    }

    public final String coreGetNamespaceURI() {
        return namespaceURI;
    }

    public final String coreGetPrefix() {
        return prefix;
    }

    public final void coreSetPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public final String coreGetLocalName() {
        return localName;
    }
    
    public final void coreSetLocalName(String localName) {
        this.localName = localName;
    }

    public final QName coreGetQName() {
        return NSAwareNamedNodeHelper.coreGetQName(this);
    }

    @Override
    protected String getImplicitNamespaceURI(String prefix) {
        if (prefix == null) {
            return this.prefix == null ? namespaceURI : null;
        } else {
            return prefix.equals(this.prefix) ? namespaceURI : null;
        }
    }
}
