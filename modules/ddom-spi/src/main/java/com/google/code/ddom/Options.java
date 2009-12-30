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
package com.google.code.ddom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Options {
    private class OptionsProcessorImpl implements OptionsProcessor {
        private final Set<Class<?>> unprocessed;
        
        OptionsProcessorImpl() {
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
    
    public OptionsProcessor createOptionsProcessor() {
        return new OptionsProcessorImpl();
    }
}
