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
package com.google.code.ddom.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

// TODO: probably doesn't work correctly for collections that may contain null items
public class FilteredIterator<T> implements Iterator<T> {
    private final Iterator<T> parent;
    private final Filter<? super T> filter;
    private T next;
    
    public FilteredIterator(Iterator<T> parent, Filter<? super T> filter) {
        this.parent = parent;
        this.filter = filter;
    }

    public boolean hasNext() {
        if (next != null) {
            return true;
        } else {
            while (parent.hasNext()) {
                T candidate = parent.next();
                if (filter.accept(candidate)) {
                    next = candidate;
                    break;
                }
            }
            return next != null;
        }
    }

    public T next() {
        if (hasNext()) {
            T result = next;
            next = null;
            return result;
        } else {
            throw new NoSuchElementException();
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
