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
}
