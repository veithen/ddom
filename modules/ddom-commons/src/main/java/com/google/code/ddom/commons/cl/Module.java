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
package com.google.code.ddom.commons.cl;

import java.net.MalformedURLException;
import java.net.URL;

// TODO: this should actually also be a ClassCollection
public class Module {
    private final ClassLoader classLoader;
    private final URL url;
    
    Module(ClassLoader classLoader, URL url) {
        this.classLoader = classLoader;
        this.url = url;
    }

    /**
     * Get the module that contains a given class.
     * 
     * @param classLoader
     *            the class loader to use
     * @param className
     *            the name of the class
     * @return the module containing the class
     * @throws ClassNotFoundException
     *             if the class was not found or an error occurred when attempting to build the URL
     *             of the module
     */
    public static Module forClassName(ClassLoader classLoader, String className) throws ClassNotFoundException {
        String name = ClassLoaderUtils.getResourceNameForClassName(className);
        URL url = classLoader.getResource(name);
        if (url == null) {
            throw new ClassNotFoundException(className);
        } else {
            String file = url.getFile();
            if (file.endsWith(name)) {
                try {
                    return new Module(classLoader, new URL(url.getProtocol(), url.getHost(), url.getPort(),
                            file.substring(0, file.length()-name.length())));
                } catch (MalformedURLException ex) {
                    throw new ClassNotFoundException(className, ex);
                }
            } else {
                throw new ClassNotFoundException(className);
            }
        }
    }
    
    public static Module forClass(Class<?> clazz) {
        try {
            return forClassName(clazz.getClassLoader(), clazz.getName());
        } catch (ClassNotFoundException ex) {
            // If we start from a Class object, we should never get a ClassNotFoundException,
            // except if something goes badly wrong. Thus throw an unchecked exception.
            throw new ClassCollectionException(ex);
        }
    }
    
    public Package getPackage(String name) {
        try {
            return new Package(classLoader, new URL(url, name.replace('.', '/')), name);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid package name", ex);
        }
    }
}
