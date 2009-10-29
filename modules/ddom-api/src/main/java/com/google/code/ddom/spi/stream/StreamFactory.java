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
package com.google.code.ddom.spi.stream;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import com.google.code.ddom.spi.ProviderFinder;
import com.google.code.ddom.spi.ProviderFinderException;

public final class StreamFactory {
    // TODO: WeakHashMap will probably not work as expected in this case, because the value may hold a strong reference to the key (see "Implementation note" of WeakHashMap)
    private static final Map<ClassLoader,StreamFactory> factories = Collections.synchronizedMap(new WeakHashMap<ClassLoader,StreamFactory>());
    
    private final Map<String,StreamProvider> providers;
    
    private StreamFactory(Map<String,StreamProvider> providers) {
        this.providers = providers;
    }
    
    public static StreamFactory getInstance(ClassLoader classLoader) throws ProviderFinderException {
        StreamFactory factory = factories.get(classLoader);
        if (factory == null) {
            factory = new StreamFactory(ProviderFinder.find(classLoader, StreamProvider.class));
            factories.put(classLoader, factory);
        }
        return factory;
    }
    
    
    public static StreamFactory getInstance() throws ProviderFinderException {
        return getInstance(AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        }));
    }
    
    public Producer getProducer(String providerName, Object source, Map<String,Object> properties, boolean preserve) throws StreamException {
        StreamProvider provider = providers.get(providerName);
        return provider == null ? null : provider.getProducer(source, properties, preserve);
    }
    
    public Producer getProducer(Object source, Map<String,Object> properties, boolean preserve) throws StreamException {
        for (StreamProvider provider : providers.values()) {
            Producer producer = provider.getProducer(source, properties, preserve);
            if (producer != null) {
                return producer;
            }
        }
        return null;
    }
    
    // TODO: similar methods for getConsumer, getSerializer, etc.
}
