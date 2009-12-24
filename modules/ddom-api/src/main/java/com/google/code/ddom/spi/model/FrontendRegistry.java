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
package com.google.code.ddom.spi.model;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.code.ddom.DocumentFactory;
import com.google.code.ddom.commons.cl.ClassLoaderLocal;
import com.google.code.ddom.commons.cl.ClassLoaderUtils;
import com.google.code.ddom.commons.cl.ClassUtils;
import com.google.code.ddom.spi.ProviderFinder;
import com.google.code.ddom.spi.ProviderFinderException;
import com.google.code.ddom.weaver.FrontendWeaver;

public final class FrontendRegistry {
    // TODO: should be taken from the backend definition
    private static final String DOCUMENT_FACTORY_IMPL_CLASS = "com.google.code.ddom.backend.linkedlist.NodeFactoryImpl";
    private static final ClassLoaderLocal<FrontendRegistry> registries = new ClassLoaderLocal<FrontendRegistry>();
    
    private final Map<String,DocumentFactory> documentFactories;
    
    private FrontendRegistry(Map<String,DocumentFactory> documentFactories) {
        this.documentFactories = documentFactories;
    }
    
    public static FrontendRegistry getInstance(ClassLoader classLoader) throws ProviderFinderException {
        FrontendRegistry registry = registries.get(classLoader);
        if (registry == null) {
            Map<String,DocumentFactory> factories = new LinkedHashMap<String,DocumentFactory>();
            // TODO: this should be done lazily
            for (Map.Entry<String,Frontend> entry : ProviderFinder.find(classLoader, Frontend.class).entrySet()) {
                try {
                    factories.put(entry.getKey(), loadFrontend(classLoader, entry.getValue()));
                } catch (Exception ex) { // TODO: do this properly
                    throw new ProviderFinderException(ex);
                }
            }
            registry = new FrontendRegistry(factories);
            registries.put(classLoader, registry);
        }
        return registry;
    }
    
    private static DocumentFactory loadFrontend(ClassLoader classLoader, Frontend frontend) throws Exception {
        FrontendWeaver weaver = new FrontendWeaver(classLoader, frontend);
        
        // The following code serves two purposes:
        //  * All classes are woven at this point, so that any errors will be reported here. This
        //    avoids obscure class loader error messages if there is any problem with the aspects.
        //  * The classes are loaded in superclass-first order to work around a bug in AspectJ
        //    (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=286473).
        // TODO: follow-up the bug with the AspectJ project
        Class<?>[] classes = ClassLoaderUtils.getClassesInPackage(classLoader, DOCUMENT_FACTORY_IMPL_CLASS);
        for (Class<?> cls : ClassUtils.sortHierarchically(Arrays.asList(classes))) {
            weaver.loadClass(cls.getName());
        }
        
        return (DocumentFactory)weaver.loadClass(DOCUMENT_FACTORY_IMPL_CLASS).newInstance();
    }
    
    public static FrontendRegistry getInstance() throws ProviderFinderException {
        return getInstance(AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        }));
    }
    
    public DocumentFactory getDocumentFactory(String model) {
        return documentFactories.get(model);
    }
}
