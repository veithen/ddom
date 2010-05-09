/*
 * Copyright 2009-2010 Andreas Veithen
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An aggregate of different class collections, which is itself a class collection.
 * 
 * @author Andreas Veithen
 */
public final class ClassCollectionAggregate implements ClassCollection {
    private final List<ClassCollection> collections = new ArrayList<ClassCollection>();
    
    public void add(ClassCollection collection) {
        collections.add(collection);
    }

    public Collection<ClassRef> getClassRefs() {
        Collection<ClassRef> result = new ArrayList<ClassRef>();
        for (ClassCollection collection : collections) {
            result.addAll(collection.getClassRefs());
        }
        return result;
    }

    public Collection<Class<?>> getClasses() {
        Collection<Class<?>> result = new ArrayList<Class<?>>();
        for (ClassCollection collection : collections) {
            result.addAll(collection.getClasses());
        }
        return result;
    }
}
