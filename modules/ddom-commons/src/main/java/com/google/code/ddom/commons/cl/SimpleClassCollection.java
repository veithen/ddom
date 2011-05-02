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
package com.google.code.ddom.commons.cl;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class SimpleClassCollection extends AbstractClassCollection {
    private static class ClassRefIterator implements Iterator<ClassRef> {
        private final Iterator<Class<?>> parent;

        public ClassRefIterator(Iterator<Class<?>> parent) {
            this.parent = parent;
        }

        public boolean hasNext() {
            return parent.hasNext();
        }

        public ClassRef next() {
            return new ClassRef(parent.next());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    private final List<Class<?>> classes = new ArrayList<Class<?>>();

    public void add(Class<?> clazz) {
        classes.add(clazz);
    }
    
    public List<Class<?>> getClasses() {
        return classes;
    }

    public Collection<ClassRef> getClassRefs() {
        final Collection<Class<?>> classes = getClasses();
        return new AbstractCollection<ClassRef>() {
            @Override
            public Iterator<ClassRef> iterator() {
                return new ClassRefIterator(classes.iterator());
            }

            @Override
            public int size() {
                return classes.size();
            }
        };
    }
}
