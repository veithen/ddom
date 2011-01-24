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

import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.CoreParentNode;

public class ElementsByNamespaceIterator extends AbstractNodeIterator<CoreNSAwareElement> {
    private final String namespaceURI;

    public ElementsByNamespaceIterator(CoreParentNode startNode, Axis axis, String namespaceURI) {
        super(startNode, CoreNSAwareElement.class, axis);
        this.namespaceURI = namespaceURI;
    }

    @Override
    protected final boolean matches(CoreNSAwareElement node) {
        return ObjectUtils.equals(node.coreGetNamespaceURI(), namespaceURI);
    }
}
