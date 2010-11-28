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
package com.googlecode.ddom.weaver.reactor;

import java.util.HashMap;
import java.util.Map;

public class Extensions implements Extensible {
    private final Map<Class<?>,Object> properties = new HashMap<Class<?>,Object>();

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