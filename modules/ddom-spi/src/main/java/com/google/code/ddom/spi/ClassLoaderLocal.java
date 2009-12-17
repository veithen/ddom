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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.io.IOUtils;

/**
 * Utility class that holds references to objects that must be local by class loader. It is thread
 * safe and makes sure that an instance linked to a class loader is garbage collected when that
 * class loader is no longer used.
 * <p>
 * <b>Implementation note</b>
 * <p>
 * The implementation of this class is based on an idea presented
 * <a href="http://dow.ngra.de/2009/06/15/classloaderlocal-how-to-avoid-classloader-leaks-on-application-redeploy/">here</a>.
 * 
 * @author Andreas Veithen
 */
public class ClassLoaderLocal<T> {
    private static final String holderClassName = ClassLoaderLocalMapHolder.class.getName();
    private static final byte[] holderClassDef;
    private static final Method defineClassMethod;
    
    static {
        try {
            InputStream in = ClassLoaderLocal.class.getClassLoader().getResourceAsStream(ClassLoaderUtils.getResourceNameForClassName(holderClassName));
            try {
                holderClassDef = IOUtils.toByteArray(in);
            } finally {
                in.close();
            }
        } catch (IOException ex) {
            // This should really never happen
            throw new Error(ex);
        }
        
        // TODO: should be a privileged action!
        try {
            defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            defineClassMethod.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            throw new NoSuchMethodError(ex.getMessage());
        }
    }

    private static Map<ClassLoaderLocal<?>,Object> getClassLoaderLocalMap(ClassLoader cl) {
        if (cl == ClassLoaderLocal.class.getClassLoader()) {
            return ClassLoaderLocalMapHolder.locals;
        } else {
            Class<?> holderClass;
            try {
                holderClass = cl.loadClass(holderClassName);
                if (holderClass.getClassLoader() != cl) {
                    holderClass = null;
                }
            } catch (ClassNotFoundException ex) {
                holderClass = null;
            }
            if (holderClass == null) {
                try {
                    holderClass = (Class<?>)defineClassMethod.invoke(cl, holderClassName, holderClassDef, 0, holderClassDef.length);
                } catch (Exception ex) {
                    // TODO: can we get here? maybe if two threads call getClassLoaderLocalMap concurrently?
                    throw new Error(ex);
                }
            }
            Field field;
            try {
                field = holderClass.getDeclaredField("locals");
            } catch (NoSuchFieldException ex) {
                throw new NoSuchFieldError(ex.getMessage());
            }
            try {
                return (Map<ClassLoaderLocal<?>,Object>)field.get(0);
            } catch (IllegalAccessException ex) {
                throw new Error(ex);
            }
        }
    }
    
    public T get(ClassLoader cl) {
        return (T)getClassLoaderLocalMap(cl).get(this);
    }

    public T put(ClassLoader cl, T value) {
        return (T)getClassLoaderLocalMap(cl).put(this, value);
    }
}
