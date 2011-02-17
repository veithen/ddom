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
package com.googlecode.ddom.stream;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A type-safe set of options. The functionality provided by this class is similar to that of a map,
 * but with several enhancements that makes it suitable for storing options passed to some service
 * provider:
 * <ul>
 * <li>{@link Class} instances are used as keys, and values must be instances of the corresponding
 * classes. More precisely they must be assignable to these classes, i.e. the key may be an
 * interface and the value an instance of an implementation of this interface. Storing and
 * retrieving options are therefore always type-safe operations. On the other hand, this approach
 * also implies that an {@link Options} object is not suitable for storing simple types such as
 * {@link String}, {@link Boolean}, {@link Integer}, etc. This means that for every new type of
 * option, the developer must create a specific class for that option. If the value space of the
 * option is finite (e.g. enable/disable), then typically an {@link Enum} will be used to represent
 * the option.
 * <li>When adding an option, it is possible to specify whether the provider must understand the
 * option, or if the option is only a hint that the provider may ignore.
 * </ul>
 * 
 * Instances of this class are not thread safe.
 * 
 * @author Andreas Veithen
 */
public class Options {
    private class OptionsTrackerImpl implements OptionsTracker {
        private final Set<Class<?>> unprocessed;
        
        OptionsTrackerImpl() {
            unprocessed = new HashSet<Class<?>>(mustUnderstandOptions);
        }

        public <T> T get(Class<T> key) {
            return key.cast(map.get(key));
        }

        public <T> T getAndMarkAsProcessed(Class<T> key) {
            T value = get(key);
            if (value != null) {
                unprocessed.remove(key);
            }
            return value;
        }

        public void markProcessed(Class<?> key) {
            unprocessed.remove(key);
        }

        public void finish() {
            if (!unprocessed.isEmpty()) {
                Class<?> option = unprocessed.iterator().next();
                throw new UnprocessedOptionException("Unprocessed option: " + option);
            }
        }
    }
    
    final Map<Class<?>, Object> map = new HashMap<Class<?>,Object>();
    final Set<Class<?>> mustUnderstandOptions = new HashSet<Class<?>>();
    
    private void internalSet(Class<?> key, Object option, boolean mustUnderstand) {
        if (key == null || option == null) {
            throw new IllegalArgumentException();
        }
        map.put(key, option);
        if (mustUnderstand) {
            mustUnderstandOptions.add(key);
        } else {
            mustUnderstandOptions.remove(key);
        }
    }
    
    public <T> void set(Class<T> key, T option, boolean mustUnderstand) {
        internalSet(key, option, mustUnderstand);
    }
    
    public <T> void set(Class<T> key, T option) {
        internalSet(key, option, true);
    }
    
    public void set(Object option, boolean mustUnderstand) {
        internalSet(option.getClass(), option, mustUnderstand);
    }
    
    public void set(Object option) {
        internalSet(option.getClass(), option, true);
    }
    
    public <T> T get(Class<T> key) {
        return key.cast(map.get(key));
    }
    
    public OptionsTracker createTracker() {
        return new OptionsTrackerImpl();
    }
}
