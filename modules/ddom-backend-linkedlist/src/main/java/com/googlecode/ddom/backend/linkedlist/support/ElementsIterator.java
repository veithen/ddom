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
package com.googlecode.ddom.backend.linkedlist.support;

import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreNode;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.ElementMatcher;

public class ElementsIterator<T extends CoreElement> extends AbstractNodeIterator<T> {
    private final ElementMatcher<? super T> matcher;
    private final String namespaceURI;
    private final String name;

    public ElementsIterator(CoreParentNode startNode, Axis axis, Class<T> type, ElementMatcher<? super T> matcher, String namespaceURI, String name) {
        super(startNode, axis, type);
        this.matcher = matcher;
        this.namespaceURI = namespaceURI;
        this.name = name;
    }

    @Override
    protected final boolean matches(CoreNode node) {
        try {
            // TODO: unchecked cast
            return node instanceof CoreElement && matcher.matches((T)node, namespaceURI, name);
        } catch (DeferredParsingException ex) {
            // TODO
            throw new RuntimeException(ex);
        }
    }
}
