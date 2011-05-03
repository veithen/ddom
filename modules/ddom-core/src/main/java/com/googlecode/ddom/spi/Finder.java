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
package com.googlecode.ddom.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import com.googlecode.ddom.util.io.ClassLoaderObjectInputStream;

/**
 * Locates provider implementations and serialized object instances.
 * 
 * @author Andreas Veithen
 */
public class Finder {
    private Finder() {}
    
    /**
     * Find all named provider implementations visible to a given class loader. The method uses the
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
     * @throws FinderException
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
    public static <T> Map<String,T> findNamedProviders(ClassLoader classLoader, Class<T> providerType) throws FinderException {
        String resourceName = "META-INF/services/" + providerType.getName();
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(resourceName);
        } catch (IOException ex) {
            throw new FinderException("Unable to load resource " + resourceName);
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
                                throw new FinderException("Class " + line + " not found");
                            } catch (ClassCastException ex) {
                                throw new FinderException("Class " + line + " is not of type " + providerType.getName());
                            }
                            Provider providerAnnotation = clazz.getAnnotation(Provider.class);
                            if (providerAnnotation == null) {
                                throw new FinderException("Missing @Provider on " + clazz.getName());
                            }
                            T instance;
                            try {
                                instance = clazz.newInstance();
                            } catch (Exception ex) {
                                throw new FinderException("Unable to instantiate " + clazz, ex);
                            }
                            String name = providerAnnotation.name();
                            if (providers.containsKey(name)) {
                                throw new FinderException("Duplicate provider: name '" + name + "'");
                            }
                            providers.put(name, instance);
                        }
                    }
                } finally {
                    in.close();
                }
            } catch (IOException ex) {
                throw new FinderException("Error loading " + url, ex);
            }
        }
        return providers;
    }
    
    /**
     * Find all serialized objects of a given type.
     * The method attempts to load resources with name <tt>META-INF/services/<i>type</i>.ser</tt>
     * from the given class loader and deserializes them.
     * 
     * @param <T>
     *            the object type
     * @param classLoader
     *            the class loader to load the resources from
     * @param type
     *            the type of serialized object to locate
     * @return A collection containing the deserialized instances. If no instances are found, an
     *         empty collection is returned.
     * @throws FinderException
     *             if a resource found in <tt>META-INF/services/</tt> could not be deserialized or
     *             contained an object of the wrong type
     */
    public static <T> Collection<T> findSerializedInstances(ClassLoader classLoader, Class<T> type) throws FinderException {
        String resourceName = "META-INF/services/" + type.getName() + ".ser";
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(resourceName);
        } catch (IOException ex) {
            throw new FinderException("Unable to load resource " + resourceName);
        }
        Collection<T> instances = new ArrayList<T>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            try {
                InputStream in = url.openStream();
                try {
                    try {
                        instances.add(type.cast(new ClassLoaderObjectInputStream(classLoader, in).readObject()));
                    } catch (ClassNotFoundException ex) {
                        throw new FinderException(url + " could not be deserialized because a class was not found", ex);
                    } catch (ClassCastException ex) {
                        throw new FinderException(url + " doesn't contain an object of type " + type.getName());
                    }
                } finally {
                    in.close();
                }
            } catch (IOException ex) {
                throw new FinderException("Error loading " + url, ex);
            }
        }
        return instances;
    }
}
