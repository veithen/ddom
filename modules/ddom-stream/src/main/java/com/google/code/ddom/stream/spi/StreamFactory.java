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
package com.google.code.ddom.stream.spi;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

import com.google.code.ddom.commons.cl.ClassLoaderLocal;
import com.google.code.ddom.spi.ProviderFinder;
import com.google.code.ddom.spi.ProviderFinderException;

public final class StreamFactory {
    private static final ClassLoaderLocal<StreamFactory> factories = new ClassLoaderLocal<StreamFactory>();
    
    private final Map<String,StreamProvider> providers;
    
    private StreamFactory(Map<String,StreamProvider> providers) {
        this.providers = providers;
    }
    
    /**
     * Get the {@link StreamFactory} instance for a given class loader.
     * 
     * @param classLoader
     *            the class loader from which to load the providers
     * @return the {@link StreamFactory} instance
     * @throws ProviderFinderException
     *             if there was an issue loading the providers from the class loader
     */
    public static StreamFactory getInstance(ClassLoader classLoader) throws ProviderFinderException {
        StreamFactory factory = factories.get(classLoader);
        if (factory == null) {
            factory = new StreamFactory(ProviderFinder.find(classLoader, StreamProvider.class));
            factories.put(classLoader, factory);
        }
        return factory;
    }
    
    /**
     * Get the {@link StreamFactory} instance for the current thread context class loader.
     * 
     * @return the {@link StreamFactory} instance for the current thread context class loader
     * @throws ProviderFinderException
     *             if there was an issue loading the providers from the class loader
     */
    public static StreamFactory getInstance() throws ProviderFinderException {
        return getInstance(AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        }));
    }

    /**
     * Get a {@link Producer} from a given source object using a given provider.
     * 
     * @param providerName
     * @param source
     * @param properties
     * @param preserve
     * @return the {@link Producer} instance that reads the data from the given <code>source</code>
     * @throws NoStreamProviderFoundException
     *             if the provider was not found or if the provider doesn't support the type of
     *             source object passed as argument
     * @throws StreamException
     */
    public Producer getProducer(String providerName, Object source, Map<String,Object> properties, boolean preserve) throws StreamException {
        StreamProvider provider = providers.get(providerName);
        if (provider == null) {
            throw new NoStreamProviderFoundException("Provider '" + providerName + "' not found");
        } else {
            Producer producer = provider.getProducer(source, properties, preserve);
            if (producer == null) {
                throw new NoStreamProviderFoundException("Provider '" + providerName + "' doesn't support source objects of type " + source.getClass().getName());
            } else {
                return producer;
            }
        }
    }
    
    public Producer getProducer(Object source, Map<String,Object> properties, boolean preserve) throws StreamException {
        for (StreamProvider provider : providers.values()) {
            Producer producer = provider.getProducer(source, properties, preserve);
            if (producer != null) {
                return producer;
            }
        }
        throw new NoStreamProviderFoundException("No provider found for source objects of type " + source.getClass().getName());
    }
    
    public Consumer getConsumer(Object destination, Map<String,Object> properties) throws StreamException {
        for (StreamProvider provider : providers.values()) {
            Consumer consumer = provider.getConsumer(destination, properties);
            if (consumer != null) {
                return consumer;
            }
        }
        throw new NoStreamProviderFoundException("No provider found for destination objects of type " + destination.getClass().getName());
    }
    
    // TODO: similar methods for getConsumer, getSerializer, etc.
}
