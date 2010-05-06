/*
 * Copyright 2009-2010 Andreas Veithen
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
        InputStream in = getClassDefinitionAsStream(classLoader, className);
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

    public static InputStream getClassDefinitionAsStream(ClassLoader classLoader, String className) throws ClassNotFoundException {
        String resourceName = getResourceNameForClassName(className);
        InputStream in = classLoader.getResourceAsStream(resourceName);
        if (in == null) {
            throw new ClassNotFoundException(className);
        } else {
            return in;
        }
    }
    
    /**
     * Get the URL corresponding to the root folder of the classpath entry from which a given
     * resource is loaded. This URL can be used to load other resources from the same classpath
     * entry (JAR file or directory).
     * 
     * @param classLoader the class loader from which the resource is available
     * @param resource the resource to inspect
     * @return the root URL or <code>null</code> if the resource can't be found or if it is not
     *         possible to determine the root URL
     */
    public static URL getRootUrlForResource(ClassLoader classLoader, String resource) {
        if (classLoader == null) {
            // A null class loader means the bootstrap class loader. In this case we use the
            // system class loader. This is safe since we can assume that the system class
            // loader uses parent first as delegation policy.
            classLoader = ClassLoader.getSystemClassLoader();
        }
        URL url = classLoader.getResource(resource);
        if (url == null) {
            return null;
        }
        String file = url.getFile();
        if (file.endsWith(resource)) {
            try {
                return new URL(url.getProtocol(), url.getHost(), url.getPort(),
                        file.substring(0, file.length()-resource.length()));
            } catch (MalformedURLException ex) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * Get the URL corresponding to the root folder of the classpath entry from which a given class
     * is loaded.
     * 
     * @param cls
     *            the class to inspect
     * @return the root URL or <code>null</code> if it is not possible to determine the root URL
     */
    public static URL getRootUrlForClass(Class<?> cls) {
        return getRootUrlForResource(cls.getClassLoader(),
                cls.getName().replace('.', '/') + ".class");
    }
}
