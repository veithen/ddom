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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

// TODO: clarify if the filtered collection is modifiable
public class FilteredCollection<T> extends AbstractCollection<T> {
    private final Collection<T> parent;
    private final Filter<? super T> filter;
    
    public FilteredCollection(Collection<T> parent, Filter<? super T> filter) {
        this.parent = parent;
        this.filter = filter;
    }

    @Override
    public Iterator<T> iterator() {
        return new FilteredIterator<T>(parent.iterator(), filter);
    }

    @Override
    public int size() {
        int size = 0;
        for (T item : parent) {
            if (filter.accept(item)) {
                size++;
            }
        }
        return size;
    }
}
