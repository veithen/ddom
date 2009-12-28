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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.code.ddom.DocumentFactory;
import com.google.code.ddom.commons.cl.ClassLoaderLocal;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.spi.ProviderFinder;
import com.google.code.ddom.spi.ProviderFinderException;

public class ModelLoaderRegistry {
    private static final ClassLoaderLocal<ModelLoaderRegistry> registries = new ClassLoaderLocal<ModelLoaderRegistry>();
    
    private final List<ModelLoader> loaders;
    private final Map<ModelDefinition,DocumentFactory> modelCache = new ConcurrentHashMap<ModelDefinition,DocumentFactory>();

    private ModelLoaderRegistry(List<ModelLoader> loaders) {
        this.loaders = loaders;
    }
    
    public static ModelLoaderRegistry getInstance(ClassLoader classLoader) {
        ModelLoaderRegistry registry = registries.get(classLoader);
        if (registry == null) {
            List<ModelLoader> loaders = new ArrayList<ModelLoader>();
            for (Map.Entry<String,ModelLoaderFactory> entry : ProviderFinder.find(classLoader, ModelLoaderFactory.class).entrySet()) {
                loaders.add(entry.getValue().createModelLoader(classLoader));
            }
            registry = new ModelLoaderRegistry(loaders);
            registries.put(classLoader, registry);
        }
        return registry;
    }

    public static ModelLoaderRegistry getInstance() throws ProviderFinderException {
        return getInstance(AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        }));
    }
    
    public DocumentFactory getDocumentFactory(ModelDefinition model) {
        DocumentFactory documentFactory = modelCache.get(model);
        if (documentFactory == null) {
            for (ModelLoader loader : loaders) {
                try {
                    documentFactory = loader.loadModel(model);
                    if (documentFactory != null) {
                        break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); // TODO
                }
            }
            if (documentFactory == null) {
                throw new RuntimeException(); // TODO
            }
            modelCache.put(model, documentFactory);
        }
        return documentFactory;
    }
}
