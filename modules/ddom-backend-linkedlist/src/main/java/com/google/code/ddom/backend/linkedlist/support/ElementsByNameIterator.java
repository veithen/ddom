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
package com.google.code.ddom.backend.linkedlist.support;

import org.apache.commons.lang.ObjectUtils;

import com.google.code.ddom.core.Axis;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.core.CoreParentNode;

public class ElementsByNameIterator extends AbstractNodeIterator<CoreNSAwareElement> {
    private final String namespaceURI;
    private final String localName;

    public ElementsByNameIterator(CoreParentNode startNode, Axis axis, String namespaceURI, String localName) {
        super(startNode, CoreNSAwareElement.class, axis);
        this.namespaceURI = namespaceURI;
        this.localName = localName;
    }

    @Override
    protected final boolean matches(CoreNSAwareElement node) {
        return ObjectUtils.equals(node.coreGetNamespaceURI(), namespaceURI) && node.coreGetLocalName().equals(localName);
    }
}
