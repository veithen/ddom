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
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractClassRefCollection extends AbstractClassCollection {
    private static class ClassLoadingIterator implements Iterator<Class<?>> {
        private final Iterator<ClassRef> parent;

        public ClassLoadingIterator(Iterator<ClassRef> parent) {
            this.parent = parent;
        }

        public boolean hasNext() {
            return parent.hasNext();
        }

        public Class<?> next() {
            try {
                ClassRef classRef = parent.next();
                return classRef.getClassLoader().loadClass(classRef.getClassName());
            } catch (ClassNotFoundException ex) {
                throw new ClassCollectionException(ex);
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    public final Collection<Class<?>> getClasses() {
        final Collection<ClassRef> classRefs = getClassRefs();
        return new AbstractCollection<Class<?>>() {
            @Override
            public Iterator<Class<?>> iterator() {
                return new ClassLoadingIterator(classRefs.iterator());
            }

            @Override
            public int size() {
                return classRefs.size();
            }
        };
    }
}
