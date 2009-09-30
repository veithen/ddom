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

public class DOM2ElementImpl extends ElementImpl implements DOM2NamedNode {
    private final String namespaceURI;
    private final String localName;
    private String prefix;

    public DOM2ElementImpl(DocumentImpl document, String namespaceURI, String localName, String prefix, boolean complete) {
        super(document, complete);
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.prefix = prefix;
    }

    public final String getNamespaceURI() {
        return namespaceURI;
    }

    public final String getPrefix() {
        return prefix;
    }

    public final void internalSetPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public final void setPrefix(String prefix) {
        DOM2NamedNodeHelper.setPrefix(this, prefix);
    }

    public final String getLocalName() {
        return localName;
    }

    public final String getTagName() {
        return DOM2NamedNodeHelper.getName(this);
    }

    @Override
    protected final ElementImpl shallowCloneWithoutAttributes() {
        DocumentImpl document = getDocument();
        NodeFactory factory = document.getNodeFactory();
        return factory.createElement(document, namespaceURI, localName, prefix, true);
    }
}