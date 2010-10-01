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
package com.google.code.ddom.weaver.reactor;

import java.util.HashMap;
import java.util.Map;

import com.google.code.ddom.weaver.realm.ClassInfo;

/**
 * Describes a weavable class. A weavable class is simply a class submitted to the weaver and that
 * may be woven. The weaver will decide whether the class actually needs to be woven or not.
 */
public final class WeavableClassInfo extends ClassInfo {
    private final ClassDefinitionSource classDefinitionSource;
    private final Map<Class<?>,Object> properties = new HashMap<Class<?>,Object>();
    
    public WeavableClassInfo(String name, boolean isInterface, ClassInfo superclass, ClassInfo[] interfaces, ClassDefinitionSource classDefinitionSource) {
        super(name, isInterface, superclass, interfaces);
        this.classDefinitionSource = classDefinitionSource;
    }

    public ClassDefinitionSource getClassDefinitionSource() {
        return classDefinitionSource;
    }

    public void set(Class<?> key, Object object) {
        if (properties.containsKey(key)) {
            throw new IllegalStateException("A property for " + key.getName() + " is already present");
        }
        properties.put(key, object);
    }
    
    public void set(Object object) {
        set(object.getClass(), object);
    }
    
    public <T> T get(Class<T> key) {
        return key.cast(properties.get(key));
    }
}
