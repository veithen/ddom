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

import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.core.CoreNSAwareAttribute;
import com.google.code.ddom.core.DeferredParsingException;
import com.google.code.ddom.stream.spi.XmlHandler;

// @Implementation
public class NSAwareAttribute extends TypedAttribute implements CoreNSAwareAttribute {
    private String namespaceURI;
    private String localName;
    private String prefix;

    public NSAwareAttribute(Document document, String namespaceURI, String localName, String prefix, String value, String type) {
        super(document, value, type);
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.prefix = prefix;
    }

    public final String coreGetNamespaceURI() {
        return namespaceURI;
    }

    public final void coreSetNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
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

    public final void internalGenerateEvents(XmlHandler handler) {
        try {
            handler.processAttribute(namespaceURI, localName, prefix, coreGetTextContent(), coreGetType());
        } catch (DeferredParsingException ex) {
            // TODO: this should never happen if the event model is consistent with the core model
            throw new RuntimeException(ex);
        }
    }
}
