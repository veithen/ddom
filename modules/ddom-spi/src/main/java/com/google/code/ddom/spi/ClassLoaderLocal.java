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
package com.google.code.ddom.spi;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Utility class that holds references to objects that must be local by class loader. It is thread
 * safe and makes sure that an instance linked to a class loader is garbage collected when that
 * class loader is no longer used.
 * 
 * @author Andreas Veithen
 */
public class ClassLoaderLocal<T> {
    // TODO: WeakHashMap will probably not work as expected in this case, because the value may hold a strong reference to the key (see "Implementation note" of WeakHashMap)
    private final Map<ClassLoader,T> map = Collections.synchronizedMap(new WeakHashMap<ClassLoader,T>());

    public T get(ClassLoader cl) {
        return map.get(cl);
    }

    public T put(ClassLoader cl, T value) {
        return map.put(cl, value);
    }
}
