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
package com.google.code.ddom.utils.dom.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Abstract iterator that returns matching items from another iterator.
 */
// TODO: this overlaps with FilteredIterator in ddom-collections
public abstract class FilterIterator<T> implements Iterator<T> {
    private final Iterator<T> parent;
    private T nextItem;
    private boolean noMoreItems;
    
    public FilterIterator(Iterator<T> parent) {
        this.parent = parent;
    }

    /**
     * Determine whether the given item matches the filter criteria.
     * 
     * @param item
     *            the item to test
     * @return true if the item matches, i.e. if it should be returned by a call to {@link #next()}
     */
    protected abstract boolean matches(T item);

    public boolean hasNext() {
        if (noMoreItems) {
            return false;
        } else if (nextItem != null) {
            return true;
        } else {
            while (parent.hasNext()) {
                T item = parent.next();
                if (matches(item)) {
                    nextItem = item;
                    return true;
                }
            }
            noMoreItems = true;
            return false;
        }
    }

    public T next() {
        if (hasNext()) {
            T result = nextItem;
            nextItem = null;
            return result;
        } else {
            throw new NoSuchElementException();
        }
    }

    public void remove() {
        parent.remove();
    }
}
