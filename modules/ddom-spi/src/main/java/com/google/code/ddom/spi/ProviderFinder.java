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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Locates provider implementations.
 * 
 * @see Provider
 * 
 * @author Andreas Veithen
 */
public class ProviderFinder {
    private ProviderFinder() {}
    
    /**
     * Find all provider implementations visible to a given class loader. The method uses the
     * standard service discovery mechanism (based on <tt>META-INF/services</tt>), but in addition
     * expects all implementations to have a {@link Provider} annotation.
     * 
     * @param <T>
     *            the provider type
     * @param classLoader
     *            the class loader to load provider implementations from
     * @param providerType
     *            the provider type (interface)
     * @return A map containing the discovered provider implementation instances. The key is the
     *         provider name as specified by {@link Provider#name()}. If no providers are found, an
     *         empty map is returned.
     * @throws ProviderFinderException
     *             if one of the following occurs:
     *             <ul>
     *             <li>A resource found in <tt>META-INF/services/</tt> referred to a non existing
     *             class, to a class that doesn't implement the provider interface or to a class
     *             that doesn't have a {@link Provider} annotation.
     *             <li>Two implementations with the same {@link Provider#name()} were found.
     *             <li>Another error occurred while reading the resources or instantiating a
     *             provider implementation.
     *             </ul>
     */
    public static <T> Map<String,T> find(ClassLoader classLoader, Class<T> providerType) throws ProviderFinderException {
        String resourceName = "META-INF/services/" + providerType.getName();
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(resourceName);
        } catch (IOException ex) {
            throw new ProviderFinderException("Unable to load resource " + resourceName);
        }
        Map<String,T> providers = new LinkedHashMap<String,T>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        if (line.length() != 0 && line.charAt(0) != '#') {
                            Class<? extends T> clazz;
                            try {
                                clazz = classLoader.loadClass(line).asSubclass(providerType);
                            } catch (ClassNotFoundException ex) {
                                throw new ProviderFinderException("Class " + line + " not found");
                            } catch (ClassCastException ex) {
                                throw new ProviderFinderException("Class " + line + " is not of type " + providerType.getName());
                            }
                            Provider providerAnnotation = clazz.getAnnotation(Provider.class);
                            if (providerAnnotation == null) {
                                throw new ProviderFinderException("Missing @Provider on " + clazz.getName());
                            }
                            T instance;
                            try {
                                instance = clazz.newInstance();
                            } catch (Exception ex) {
                                throw new ProviderFinderException("Unable to instantiate " + clazz, ex);
                            }
                            String name = providerAnnotation.name();
                            if (providers.containsKey(name)) {
                                throw new ProviderFinderException("Duplicate provider: name '" + name + "'");
                            }
                            providers.put(name, instance);
                        }
                    }
                } finally {
                    in.close();
                }
            } catch (IOException ex) {
                throw new ProviderFinderException("Error loading " + url, ex);
            }
        }
        return providers;
    }
}
