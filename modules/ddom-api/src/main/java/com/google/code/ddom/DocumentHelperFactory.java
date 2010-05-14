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
package com.google.code.ddom;

public abstract class DocumentHelperFactory {
    public static final DocumentHelperFactory INSTANCE;
    
    static {
        try {
            INSTANCE = (DocumentHelperFactory)DocumentHelperFactory.class.getClassLoader().loadClass("com.google.code.ddom.impl.DocumentHelperFactoryImpl").newInstance();
        } catch (InstantiationException ex) {
            throw new InstantiationError(ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new IllegalAccessError(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }

    /**
     * Get the default instance. In a non OSGi environment, this instance will have access to all
     * frontend, backend and stream providers visible in the class loader from which the
     * {@link DocumentHelper} class is loaded. In an OSGi environment, this instance will use all
     * providers in registered bundles. TODO: make this more precise
     * 
     * @return the default instance
     */
    public abstract DocumentHelper getDefaultInstance();
    
    public abstract DocumentHelper newInstance(ClassLoader classLoader);
    
    public abstract DocumentHelper newInstance();
}
