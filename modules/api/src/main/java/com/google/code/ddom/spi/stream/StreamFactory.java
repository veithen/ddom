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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class StreamFactory {
    private static final Map<ClassLoader,StreamFactory> factories = Collections.synchronizedMap(new WeakHashMap<ClassLoader,StreamFactory>());
    
    private final Map<String,StreamProvider> providers = new LinkedHashMap<String,StreamProvider>();
    
    private StreamFactory() {}
    
    public static StreamFactory getInstance(ClassLoader classLoader) {
        StreamFactory factory = factories.get(classLoader);
        if (factory == null) {
            factory = new StreamFactory();
            
            // TODO: replace this by a service discovery algorithm
            try {
                factory.providers.put("stax", (StreamProvider)classLoader.loadClass("com.google.code.ddom.stream.stax.StAXStreamProvider").newInstance());
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            factories.put(classLoader, factory);
        }
        return factory;
    }
    
    
    public static StreamFactory getInstance() {
        return getInstance(AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        }));
    }
    
    public Producer getProducer(String providerName, Object source, Map<String,Object> properties) throws StreamException {
        StreamProvider provider = providers.get(providerName);
        return provider == null ? null : provider.getProducer(source, properties);
    }
    
    public Producer getProducer(Object source, Map<String,Object> properties) throws StreamException {
        for (StreamProvider provider : providers.values()) {
            Producer producer = provider.getProducer(source, properties);
            if (producer != null) {
                return producer;
            }
        }
        return null;
    }
}
