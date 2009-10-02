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

import com.google.code.ddom.dom.model.DOM1Element;
import com.google.code.ddom.dom.model.DOMDocument;
import com.google.code.ddom.dom.model.DOMElement;

public class DOM1ElementImpl extends ElementImpl implements DOM1Element {
    private final String tagName;

    public DOM1ElementImpl(DOMDocument document, String tagName, boolean complete) {
        super(document, complete);
        this.tagName = tagName;
    }

    public final String getNamespaceURI() {
        return null;
    }

    public final String getPrefix() {
        return null;
    }

    public final void setPrefix(String prefix) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
    }

    public final String getLocalName() {
        return null;
    }

    public final String getTagName() {
        return tagName;
    }

    @Override
    protected final DOMElement shallowCloneWithoutAttributes() {
        DOMDocument document = getDocument();
        NodeFactory factory = document.getNodeFactory();
        return factory.createElement(document, tagName, true);
    }
}
