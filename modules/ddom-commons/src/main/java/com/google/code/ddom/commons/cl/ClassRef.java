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

import org.apache.commons.io.IOUtils;

/**
 * A reference to a class. This immutable class allows to unambiguously identify a class without
 * actually loading it.
 * 
 * @author Andreas Veithen
 */
public class ClassRef {
    private final ClassLoader classLoader;
    private final String className;
    private Class<?> clazz;
    
    public ClassRef(ClassLoader classLoader, String className) {
        this.classLoader = classLoader;
        this.className = className;
    }
    
    public ClassRef(Class<?> clazz) {
        this.classLoader = clazz.getClassLoader();
        this.className = clazz.getName();
        this.clazz = clazz;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public String getClassName() {
        return className;
    }

    /**
     * Load the definition of the class identified by this object.
     * 
     * @return the class definition, i.e. the content of the corresponding class file
     * @throws ClassNotFoundException
     *             if the class was not found or an error occurred when loading the class definition
     */
    public byte[] getClassDefinition() throws ClassNotFoundException {
        InputStream in = getClassDefinitionAsStream();
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

    public InputStream getClassDefinitionAsStream() throws ClassNotFoundException {
        String resourceName = ClassLoaderUtils.getResourceNameForClassName(className);
        InputStream in = classLoader.getResourceAsStream(resourceName);
        if (in == null) {
            throw new ClassNotFoundException(className);
        } else {
            return in;
        }
    }
    
    public synchronized Class<?> load() throws ClassNotFoundException {
        if (clazz == null) {
            clazz = classLoader.loadClass(className);
        }
        return clazz;
    }
}