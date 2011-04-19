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
package com.googlecode.ddom.backend.linkedlist.support;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.Mapper;

public abstract class AbstractAttributeIterator<T extends CoreAttribute,S> implements Iterator<S> {
    private final CoreElement element;
    private final Class<T> type;
    private final Mapper<T,S> mapper;
    private CoreAttribute attribute;
    private boolean hasNext;
    
    public AbstractAttributeIterator(CoreElement element, Class<T> type, Mapper<T,S> mapper) {
        this.element = element;
        this.type = type;
        this.mapper = mapper;
    }
    
    protected abstract boolean matches(T attribute);

    public final boolean hasNext() {
        if (!hasNext) {
            CoreAttribute attribute = this.attribute;
            do {
                try {
                    if (attribute == null) {
                        attribute = element.coreGetFirstAttribute();
                    } else {
                        attribute = attribute.coreGetNextAttribute();
                    }
                } catch (DeferredParsingException ex) {
                    throw new RuntimeException(ex); // TODO
                }
            } while (attribute != null && (!type.isInstance(attribute) || !matches(type.cast(attribute))));
            this.attribute = attribute;
            hasNext = true;
        }
        return attribute != null;
    }

    public final S next() {
        if (hasNext()) {
            hasNext = false;
            return mapper.map(type.cast(attribute));
        } else {
            throw new NoSuchElementException();
        }
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
