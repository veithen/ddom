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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class ClassLoaderUtils {
    private ClassLoaderUtils() {}
    
    /**
     * Translate a class name into the resource name of the corresponding <tt>.class</tt> file. E.g.
     * <tt>java.lang.String</tt> would be translated into <tt>java/lang/String.class</tt>.
     * 
     * @param className the class name
     * @return the resource name of the class file
     */
    public static String getResourceNameForClassName(String className) {
        return className.replace('.', '/') + ".class";
    }

    /**
     * Load the definition of a given class.
     * 
     * @param classLoader
     *            the class loader to load the class from
     * @param className
     *            the name of the class
     * @return the class definition, i.e. the content of the corresponding class file
     * @throws ClassNotFoundException
     *             if the class was not found or an error occurred when loading the class definition
     */
    public static byte[] getClassDefinition(ClassLoader classLoader, String className) throws ClassNotFoundException {
        String resourceName = getResourceNameForClassName(className);
        InputStream in = classLoader.getResourceAsStream(resourceName);
        if (in == null) {
            throw new ClassNotFoundException(className);
        }
        try {
            try {
                return IOUtils.toByteArray(in);
            } finally {
                in.close();
            }
        } catch (IOException ex) {
            throw new ClassNotFoundException(className, ex);
        }
    }
    
    /**
     * Get the URL of the module (JAR) that contains a given class.
     * 
     * @param classLoader
     *            the class loader to use
     * @param className
     *            the name of the class
     * @return the URL of the root folder of the module containing the class
     * @throws ClassNotFoundException
     *             if the class was not found or an error occurred when attempting to build the URL
     *             of the module
     */
    public static URL getModuleForClassName(ClassLoader classLoader, String className) throws ClassNotFoundException {
        String name = getResourceNameForClassName(className);
        URL url = classLoader.getResource(name);
        if (url == null) {
            throw new ClassNotFoundException(className);
        } else {
            String file = url.getFile();
            if (file.endsWith(name)) {
                try {
                    return new URL(url.getProtocol(), url.getHost(), url.getPort(),
                            file.substring(0, file.length()-name.length()));
                } catch (MalformedURLException ex) {
                    throw new ClassNotFoundException(className, ex);
                }
            } else {
                throw new ClassNotFoundException(className);
            }
        }
    }
}
