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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

import com.google.code.ddom.Options;
import com.google.code.ddom.OptionsTracker;
import com.google.code.ddom.commons.cl.ClassLoaderLocal;
import com.googlecode.ddom.spi.ProviderFinder;
import com.googlecode.ddom.spi.ProviderFinderException;
import com.googlecode.ddom.stream.spi.StreamProvider;

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
     * Get a {@link XmlSource} from a given source object using a given provider.
     * 
     * @param providerName
     * @param object
     * @param options
     * @param preserve
     * @return the {@link XmlSource} instance that reads the data from the given <code>object</code>
     * @throws NoStreamProviderFoundException
     *             if the provider was not found or if the provider doesn't support the type of
     *             source object passed as argument
     * @throws StreamException
     */
    public XmlSource getSource(String providerName, Object object, OptionsTracker options, boolean preserve) throws StreamException {
        StreamProvider provider = providers.get(providerName);
        if (provider == null) {
            throw new NoStreamProviderFoundException("Provider '" + providerName + "' not found");
        } else {
            XmlSource source = provider.getSource(object, options, preserve);
            if (source == null) {
                throw new NoStreamProviderFoundException("Provider '" + providerName + "' doesn't support source objects of type " + object.getClass().getName());
            } else {
                return source;
            }
        }
    }
    
    public XmlSource getSource(String providerName, Object object, Options options, boolean preserve) throws StreamException {
        OptionsTracker tracker = options.createTracker();
        XmlSource source = getSource(providerName, object, tracker, preserve);
        // TODO: clean up producer if this fails
        tracker.finish();
        return source;
    }
    
    public XmlSource getSource(Object object, OptionsTracker options, boolean preserve) throws StreamException {
        for (StreamProvider provider : providers.values()) {
            XmlSource source = provider.getSource(object, options, preserve);
            if (source != null) {
                return source;
            }
        }
        throw new NoStreamProviderFoundException("No provider found for source objects of type " + object.getClass().getName());
    }
    
    public XmlSource getSource(Object object, Options options, boolean preserve) throws StreamException {
        OptionsTracker tracker = options.createTracker();
        XmlSource source = getSource(object, tracker, preserve);
        // TODO: clean up producer if this fails
        tracker.finish();
        return source;
    }
    
    public XmlOutput getOutput(Object destination, OptionsTracker options) throws StreamException {
        for (StreamProvider provider : providers.values()) {
            XmlOutput output = provider.getOutput(destination, options);
            if (output != null) {
                return output;
            }
        }
        throw new NoStreamProviderFoundException("No provider found for destination objects of type " + destination.getClass().getName());
    }
    
    public XmlOutput getOutput(Object destination, Options options) throws StreamException {
        OptionsTracker tracker = options.createTracker();
        XmlOutput output = getOutput(destination, tracker);
        // TODO: clean up producer if this fails
        tracker.finish();
        return output;
    }
    
    // TODO: similar methods for getConsumer, getSerializer, etc.
}
