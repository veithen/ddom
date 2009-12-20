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


public abstract class TransformingClassLoader extends ClassLoader {
    public TransformingClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = findLoadedClass(name);
        if (clazz == null) {
            if (needsTransformation(name)) {
                String resourceName = ClassLoaderUtils.getResourceNameForClassName(name);
                InputStream in = super.getResourceAsStream(resourceName);
                if (in == null) {
                    throw new ClassNotFoundException(name);
                }
                try {
                    try {
                        byte[] classDef = transformClass(name, IOUtils.toByteArray(in));
                        clazz = defineClass(name, classDef, 0, classDef.length);
                    } finally {
                        in.close();
                    }
                } catch (IOException ex) {
                    throw new ClassNotFoundException(name, ex);
                }
            } else {
                clazz = super.loadClass(name, false);
            }
        }
        if (resolve) {
            resolveClass(clazz);
        }
        return clazz;
    }
    
    protected abstract boolean needsTransformation(String className);
    protected abstract byte[] transformClass(String className, byte[] classDef);
}
