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
package com.google.code.ddom.commons.cl;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public abstract class ClassCollection {
    private static class ClassLoadingIterator implements Iterator<Class<?>> {
        private final ClassLoader classLoader;
        private final Iterator<String> parent;

        public ClassLoadingIterator(ClassLoader classLoader, Iterator<String> parent) {
            this.classLoader = classLoader;
            this.parent = parent;
        }

        public boolean hasNext() {
            return parent.hasNext();
        }

        public Class<?> next() {
            try {
                return classLoader.loadClass(parent.next());
            } catch (ClassNotFoundException ex) {
                throw new ClassCollectionException(ex);
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    final ClassLoader classLoader;
    
    public ClassCollection(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public abstract Collection<String> getClassNames();
    
    public Collection<Class<?>> getClasses() {
        final Collection<String> classNames = getClassNames();
        return new AbstractCollection<Class<?>>() {
            @Override
            public Iterator<Class<?>> iterator() {
                return new ClassLoadingIterator(classLoader, classNames.iterator());
            }

            @Override
            public int size() {
                return classNames.size();
            }
        };
    }
}
